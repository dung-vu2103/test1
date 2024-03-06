package com.ringme.cms.service.kakoakcms.notification;

import com.ringme.cms.dto.kakoakcms.notification.NotiEntityDto;
import com.ringme.cms.dto.kakoakcms.sms.OAFeedbackCampaignDto;
import com.ringme.cms.model.kakoakcms.notification.NotiEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NotiEntityService {
    Page<NotiEntity> get(NotiEntityDto dto, int pageNo, int pageSize);
    NotiEntity findById(Long id);
    NotiEntityDto processSearch(String title, String inputType, String msgType, Integer status, Integer processStatus,
                                String startAt, String endAt);
    void delete(Long id);
    void save(NotiEntityDto dto, MultipartFile filePath, String cronType, String cronMin, String cronHour,
              List<String> cronWeekDay, List<String> cronMonthDay, String thumbUpload);
    void handlerCronParam(List<NotiEntity> list);
    void handlerCronParamObj(NotiEntity object);
    String getLinkTo(String deeplink, String deeplinkParams);
}
