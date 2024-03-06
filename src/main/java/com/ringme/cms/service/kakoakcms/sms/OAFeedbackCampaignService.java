package com.ringme.cms.service.kakoakcms.sms;

import com.ringme.cms.dto.kakoakcms.sms.OAFeedbackCampaignDto;
import com.ringme.cms.model.kakoakcms.sms.OAFeedbackCampaign;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OAFeedbackCampaignService {
    Page<OAFeedbackCampaign> get(OAFeedbackCampaignDto dto, int pageNo, int pageSize);
    OAFeedbackCampaign findById(Long id);
    void handlerCronParam(List<OAFeedbackCampaign> list);
    void handlerCronParamObj(OAFeedbackCampaign obj);
    OAFeedbackCampaignDto processSearch(String titleSMS, String officialAccountId, String inputType, String msgType,
                                        Integer status, Integer processStatus, String startAt, String endAt);
    void delete(Long id);
    void save(OAFeedbackCampaignDto dto, MultipartFile filePath, String cronType, String cronMin, String cronHour,
              List<String> cronWeekDay, List<String> cronMonthDay);
}
