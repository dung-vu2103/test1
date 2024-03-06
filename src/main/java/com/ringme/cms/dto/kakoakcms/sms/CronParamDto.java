package com.ringme.cms.dto.kakoakcms.sms;

import lombok.Data;

import java.util.List;
@Data
public class CronParamDto {
    public Integer cron_hour;
    public String cron_type;
    public Integer cron_minute;
    public List<String> cron_week_days;
    public List<Integer> cron_month_days;
}
