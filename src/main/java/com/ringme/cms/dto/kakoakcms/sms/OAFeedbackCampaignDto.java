package com.ringme.cms.dto.kakoakcms.sms;

import com.google.gson.Gson;
import com.ringme.cms.model.kakoakcms.sms.OAFeedbackCampaign;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAFeedbackCampaignDto {
    private Long id;
    private String title;
    private String officialAccountId;
    private String startAt;
    private String endAt;
    private String cronExpression;
    private String cronParams;
    private String inputType;
    private String phoneNoList;
    private String filePath;
    private String msgType;
    private String imagePath;
    private String imagePostPath;
    private String imageLink;
    private Integer deeplinkConfigureId;
    private String deeplinkParams;
    private String msgContent;
    private Integer status;
    private Integer processStatus;
    private Long createdBy;
    private Long updatedBy;
    private Date processTime;
    private Integer processId;
    private Integer total;
    private Date approvedAt;
    private Integer approvedBy;
    private CronParamDto cronParamDto;

    public OAFeedbackCampaignDto(OAFeedbackCampaign obj) {
        id = obj.getId();
        title = obj.getTitle();
        officialAccountId = obj.getOfficialAccountId();
        startAt = String.valueOf(obj.getStartAt());
        endAt = String.valueOf(obj.getEndAt());
        cronExpression = obj.getCronExpression();
        cronParams = obj.getCronParams();
        inputType = obj.getInputType();
        phoneNoList = obj.getPhoneNoList();
        filePath = obj.getFilePath();
        msgType = obj.getMsgType();
        imagePath = obj.getImagePath();
        imagePostPath = obj.getImagePostPath();
        imageLink = obj.getImageLink();
        deeplinkConfigureId = obj.getDeeplinkConfigureId();
        deeplinkParams = obj.getDeeplinkParams();
        msgContent = obj.getMsgContent();
        status = obj.getStatus();
        processStatus = obj.getProcessStatus();
        createdBy = obj.getCreatedBy();
        updatedBy = obj.getUpdatedBy();
        processTime = obj.getProcessTime();
        processId = obj.getProcessId();
        total = obj.getTotal();
        approvedAt = obj.getApprovedAt();
        approvedBy = obj.getApprovedBy();
        Gson gson = new Gson();
        cronParamDto = gson.fromJson(obj.getCronParams(), CronParamDto.class);
    }
}
