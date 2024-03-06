package com.ringme.cms.dto.kakoakcms.notification;

import com.google.gson.Gson;
import com.ringme.cms.dto.kakoakcms.sms.CronParamDto;
import com.ringme.cms.model.kakoakcms.marketing.DeeplinkConfigure;
import com.ringme.cms.model.kakoakcms.notification.NotiEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotiEntityDto {
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
    private Integer createdBy;
    private Integer updatedBy;
    private Date processTime;
    private Integer processId;
    private Integer total;
    private Date approvedAt;
    private Integer approvedBy;
    private Integer gameId;
    private Integer channelId;
    private Integer packageId;
    private CronParamDto cronParamDto;
    private DeeplinkConfigureDto deeplinkConfigureDto;
    private String linkTo;
    public NotiEntityDto(NotiEntity noti, DeeplinkConfigure deeplinkConfigure, String linkTo) {
        id = noti.getId();
        title = noti.getTitle();
        officialAccountId = noti.getOfficialAccountId();
        startAt = String.valueOf(noti.getStartAt());
        endAt = String.valueOf(noti.getEndAt());
        cronExpression = noti.getCronExpression();
        cronParams = noti.getCronParams();
        inputType = noti.getInputType();
        phoneNoList = noti.getPhoneNoList();
        filePath = noti.getFilePath();
        msgType = noti.getMsgType();
        imagePath = noti.getImagePath();
        imagePostPath = noti.getImagePostPath();
        imageLink = noti.getImageLink();
        deeplinkConfigureId = noti.getDeeplinkConfigureId();
        deeplinkParams = noti.getDeeplinkParams();
        msgContent = noti.getMsgContent();
        status = noti.getStatus();
        processStatus = noti.getProcessStatus();
        createdBy = noti.getCreatedBy();
        updatedBy = noti.getUpdatedBy();
        processTime = noti.getProcessTime();
        processId = noti.getProcessId();
        total = noti.getTotal();
        approvedAt = noti.getApprovedAt();
        approvedBy = noti.getApprovedBy();
        gameId = noti.getGameId();
        channelId = noti.getChannelId();
        packageId = noti.getPackageId();
        Gson gson = new Gson();
        cronParamDto = gson.fromJson(noti.getCronParams(), CronParamDto.class);

        ModelMapper modelMapper = new ModelMapper();
        deeplinkConfigureDto = modelMapper.map(deeplinkConfigure, DeeplinkConfigureDto.class);

        this.linkTo = linkTo;
    }
}
