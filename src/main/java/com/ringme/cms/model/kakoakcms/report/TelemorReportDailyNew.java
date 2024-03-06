package com.ringme.cms.model.kakoakcms.report;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "telemor_report_daily_new")
@EntityListeners(AuditingEntityListener.class)
public class TelemorReportDailyNew implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_report")
    private String dateReport;
    @Column(name = "new_user")
    private Integer newUser;
    @Column(name = "active_user")
    private Integer activeUser;
    @Column(name = "callout_user")
    private Integer calloutUser;
    @Column(name = "callout_minute")
    private Integer calloutMinute;
    @Column(name = "smsout_user")
    private Integer smsoutUser;
    @Column(name = "number_smsout")
    private Integer numberSmsout;
    @Column(name = "calldata_user")
    private Integer calldataUser;
    @Column(name = "calldata_minute")
    private Integer calldataMinute;
    @Column(name = "new_user_in_month")
    private Integer newUserInMonth;
    @Column(name = "active_user_in_month")
    private Integer activeUserInMonth;
    @Column(name = "callout_user_in_month")
    private Integer calloutUserInMonth;
    @Column(name = "callout_minute_in_month")
    private Integer calloutMinuteInMonth;
    @Column(name = "sms_out_user_in_month")
    private Integer smsOutUserInMonth;
    @Column(name = "number_smsout_in_month")
    private Integer numberSmsoutInMonth;
    @Column(name = "calldata_user_in_month")
    private Integer calldataUserInMonth;
    @Column(name = "calldata_minute_in_month")
    private Integer calldataMinuteInMonth;
    @Column(name = "total_user")
    private Integer totalUser;
    @Column(name = "created_at")
    private Date createdAt;
}
