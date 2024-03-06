package com.ringme.cms.model.kakoakcms.sms;

import com.ringme.cms.model.sys.EntityBase;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "oa_feedback_campaign")
@EntityListeners(AuditingEntityListener.class)
public class OAFeedbackCampaign extends EntityBase implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "official_account_id")
    private String officialAccountId;
    @Column(name = "start_at")
    private Date startAt;
    @Column(name = "end_at")
    private Date endAt;
    @Column(name = "cron_expression")
    private String cronExpression;
    @Column(name = "cron_params", columnDefinition = "jsonb")
    private String cronParams;
    @Column(name = "input_type")
    private String inputType;
    @Column(name = "phone_no_list")
    private String phoneNoList;
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "msg_type")
    private String msgType;
    @Column(name = "image_path")
    private String imagePath;
    @Column(name = "image_post_path")
    private String imagePostPath;
    @Column(name = "image_link")
    private String imageLink;
    @Column(name = "deeplink_configure_id")
    private Integer deeplinkConfigureId;
    @Column(name = "deeplink_params")
    private String deeplinkParams;
    @Column(name = "msg_content")
    private String msgContent;
    @Column(name = "status")
    private Integer status;
    @Column(name = "process_status")
    private Integer processStatus;
    @Column(name = "created_by")
    private Long createdBy;
    @Column(name = "updated_by")
    private Long updatedBy;
    @Column(name = "process_time")
    private Date processTime;
    @Column(name = "process_id")
    private Integer processId;
    @Column(name = "total")
    private Integer total;
    @Column(name = "approved_at")
    private Date approvedAt;
    @Column(name = "approved_by")
    private Integer approvedBy;
}
