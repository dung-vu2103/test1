package com.ringme.cms.service.kakoakcms.sms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.ringme.cms.common.Helper;
import com.ringme.cms.common.UploadFile;
import com.ringme.cms.dto.kakoakcms.sms.OAFeedbackCampaignDto;
import com.ringme.cms.model.kakoakcms.sms.OAFeedbackCampaign;
import com.ringme.cms.model.sys.User;
import com.ringme.cms.repository.kakoakcms.sms.OAFeedbackCampaignRepository;
import com.ringme.cms.repository.sys.UserRepository;
import com.ringme.cms.service.sys.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.*;

@Service
@Log4j2
@Transactional(value = "primaryTransactionManager")
public class OAFeedbackCampaignServiceImpl implements OAFeedbackCampaignService {
    @Autowired
    UserService userService;
    @Autowired
    OAFeedbackCampaignRepository repository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UploadFile uploadFile;
    @Override
    public Page<OAFeedbackCampaign> get(OAFeedbackCampaignDto dto, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        String[] startAts = Helper.reportDate(dto.getStartAt());
        String[] endAts = Helper.reportDate(dto.getEndAt());
        log.info("dtoooo|" + dto);
        log.info("startAts|" + Arrays.toString(startAts));
        log.info("endAts|" + Arrays.toString(endAts));
        return repository.get(dto.getTitle(), dto.getOfficialAccountId(), dto.getInputType(), dto.getMsgType(),
                dto.getStatus(), dto.getProcessStatus(), startAts[0], startAts[1], endAts[0], endAts[1], pageable);
    }
    @Override
    public OAFeedbackCampaign findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public void handlerCronParam(List<OAFeedbackCampaign> list) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            for (OAFeedbackCampaign object : list) {
                if (object.getCronParams() != null) {
                    log.info("AAAAA|" + object.getCronParams());
                    JsonNode jsonNode = objectMapper.readTree(object.getCronParams());
                    object.setCronParams(jsonNode.get("cron_type").asText());
                }
            }
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void handlerCronParamObj(OAFeedbackCampaign object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
                if (object.getCronParams() != null) {
                    JsonNode jsonNode = objectMapper.readTree(object.getCronParams());
                    object.setCronParams(jsonNode.get("cron_type").asText());
                }
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }

    @Override
    public OAFeedbackCampaignDto processSearch(String titleSMS, String officialAccountId, String inputType, String msgType,
                                               Integer status, Integer processStatus, String startAt, String endAt) {
        OAFeedbackCampaignDto dto = new OAFeedbackCampaignDto();
        dto.setTitle(Helper.processStringSearch(titleSMS));
        dto.setOfficialAccountId(Helper.processStringSearch(officialAccountId));
        dto.setInputType(Helper.processStringSearch(inputType));
        dto.setMsgType(Helper.processStringSearch(msgType));
        dto.setStatus(status);
        dto.setProcessStatus(processStatus);
        dto.setStartAt(Helper.processStringSearch(startAt));
        dto.setEndAt(Helper.processStringSearch(endAt));
        return dto;
    }
    @Override
    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            log.error("----------ERROR DELETE ICON|----------" + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void save(OAFeedbackCampaignDto dto, MultipartFile filePath, String cronType, String cronMin, String cronHour,
                     List<String> cronWeekDay, List<String> cronMonthDay) {
        log.info("dtoooo|" + dto);
        log.info("cronType|" + cronType + "|cronMin|" + cronMin + "|cronHour|" + cronHour);
        log.info("cronWeekDayList|" + cronWeekDay);
        log.info("cronMonthDayList|" + cronMonthDay);
        OAFeedbackCampaign object = new OAFeedbackCampaign();
        User user = userService.getUser();
        log.info("id user|" + user.getId());
        if(dto.getId() == null) {
            log.info("vào đây");
            object.setCreatedDate(new Date());
            object.setCreatedBy(user.getId());
            object.setProcessStatus(0);
            object.setStatus(0);
        } else {
            object = findById(dto.getId());
            object.setUpdatedBy(user.getId());
            object.setModifiedDate(new Date());
        }
        object.setMsgContent(dto.getMsgContent());
        object.setMsgType("SMS");
        object.setOfficialAccountId("support");
        object.setId(dto.getId());
        object.setTitle(dto.getTitle());
        object.setStartAt(Helper.convertDate(dto.getStartAt()));
        object.setEndAt(Helper.convertDate(dto.getEndAt()));
        object.setInputType(dto.getInputType());
        object.setPhoneNoList(handlerPhoneNoList(dto, filePath));
        object.setCronParams(makeJsonCronParam(cronType, cronMin, cronHour, cronWeekDay, cronMonthDay));
        object.setCronExpression(handlerCronType(cronType, cronMin, cronHour, cronWeekDay, cronMonthDay));
        if(object.getInputType().equals("file"))
            uploadFile(object, filePath);
        log.info("objectttt|" + object);
        repository.save(object);
    }

    private String handlerPhoneNoList(OAFeedbackCampaignDto dto, MultipartFile filePath) {
        String result = "";
        if(dto.getInputType().equals("text"))
            result = dto.getPhoneNoList();
        else if (dto.getInputType().equals("file"))
            result = handlerExcel(filePath);
        else // active_users
            result = null;
        return result;
    }

    private void uploadFile(OAFeedbackCampaign object, MultipartFile filePath) {
        String[] fileNameExcel = uploadFile.fileName(filePath, "excels");
        if(fileNameExcel != null && (object.getId() == null || !fileNameExcel[2].equals(object.getFilePath()))) {
            object.setFilePath("/" + fileNameExcel[1]);
            uploadFile.upload(filePath, fileNameExcel);
        }
    }

    private String handlerCronType(String cronType, String cronMin, String cronHour, List<String> cronWeekDay, List<String> cronMonthDay) {
        String cronExpression;
        switch (cronType) {
//            case "onetime" -> cronExpression = null;
            case "hourly" -> cronExpression = "0 " + cronMin + " * * * ?";
            case "daily" -> cronExpression = "0 " + cronMin + " " + cronHour + " * * ?";
            case "weekly" ->
                    cronExpression = "0 " + cronMin + " " + cronHour + " ? * " + handlerWeeklyOrMonthly(cronWeekDay);
            case "monthly" ->
                    cronExpression = "0 " + cronMin + " " + cronHour + " " + handlerWeeklyOrMonthly(cronMonthDay) + " * ?";
            default -> cronExpression = null;
        }
        return cronExpression;
    }

    private String handlerWeeklyOrMonthly(List<String> list) {
        StringBuilder days = new StringBuilder();
        if(list != null && list.size() > 0) {
            for(int i=0; i<list.size(); i++) {
                if(i==0)
                    days.append(list.get(i));
                else
                    days.append(",").append(list.get(i));
            }
        } else {
            days = new StringBuilder("*");
        }
        return days.toString();
    }

    private String makeJsonCronParam(String cronType, String cronMin, String cronHour, List<String> cronWeekDay, List<String> cronMonthDay) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("cron_type", cronType);
        jsonObject.addProperty("cron_minute", cronMin);
        jsonObject.addProperty("cron_hour", cronHour);
        jsonObject.add("cron_week_days", Helper.covertListToArr(cronWeekDay));
        jsonObject.add("cron_month_days", Helper.covertListToArr(cronMonthDay));
        log.info("jsonStringjsonString|" + jsonObject);
        return jsonObject.toString();
    }
    private String handlerExcel(MultipartFile fileExcel) {
        StringBuilder results = new StringBuilder();
        int count = 0;
        try (Workbook workbook = new XSSFWorkbook(fileExcel.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row currentRow : sheet) {
                if(count == 0) {
                    count++;
                    continue;
                }
                if(count == 1)
                    results.append(currentRow.getCell(0).getStringCellValue());
                else
                    results.append(",").append(currentRow.getCell(0).getStringCellValue());
                count++;
            }
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return results.toString();
    }
}
