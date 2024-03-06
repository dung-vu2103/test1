package com.ringme.cms.dto.kakoakcms.report;

import lombok.Data;

@Data
public class TelemorReportDailyNewDto {
    private String dateReport;
    private Integer newUser;
    private Integer activeUser;
    private Integer calloutUser;
    private Integer calloutMinute;
    private Integer smsoutUser;
    private Integer numberSmsout;
    private Integer calldataUser;
    private Integer calldataMinute;
    private Integer newUserInMonth;
    private Integer activeUserInMonth;
    private Integer calloutUserInMonth;
    private Integer calloutMinuteInMonth;
    private Integer smsOutUserInMonth;
    private Integer numberSmsoutInMonth;
    private Integer calldataUserInMonth;
    private Integer calldataMinuteInMonth;
    private Integer totalUser;
}