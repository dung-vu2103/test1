package com.ringme.cms.common;

import com.google.gson.JsonArray;
import com.ringme.cms.dto.AjaxSearchDto;
import lombok.extern.log4j.Log4j2;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.text.DecimalFormat;

@Log4j2
public class Helper {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static String getTimeNow(){
        try {
            LocalDate currentDate = LocalDate.now();

            String time = currentDate.getYear() + "/" + currentDate.getMonthValue() + "/" + currentDate.getDayOfMonth();

            return time;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }

    public static Date convertDate(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(input, formatter);
        return java.sql.Timestamp.valueOf(dateTime);
    }

    public static String processStringNullEmptyToInt(String input){
        if(input == null || input.equals(""))
            input = "0";
        return input;
    }

    public static String processStringSearch(String input) {
        if(input != null && input.equals(""))
            input = null;
        else if (input != null) {
            input = input.trim();
        }
        return input;
    }
    public static String normalizeNumber(String number) {
        if(number == null || number.equals(""))
            return  "0";
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(Long.parseLong(number));
    }
    public static String normalizeNumber(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(number);
    }
    public static String convertMsToMinutes(Long input) {
        if(input == null)
            input = 0l;
        double minutes = (double) input / 60000;
        return normalizeNumber(minutes);
    }

    public static Long convertMsToMinutesV2(Long input) {
        if(input == null)
            input = 0l;
        long minutes = (long) input / 60000;
        return minutes;
    }
    public static Integer convertStringToInt(String input){
        try {
            if (input != null) {
                if (input.trim().equals(""))
                    input = null;
                else
                    input = input.trim().replaceAll("\s+", "");
            }
        } catch (Exception e) {
            log.error("Error|"+e.getMessage(),e);
        }
        if(input == null)
            return null;
        return Integer.parseInt(input);
    }

    public static String convertToSlug(String input) {
        if(input == null)
            return null;
        String slug = input.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
        slug = slug.replaceAll("\s+", "-");
        return slug;
    }
    public static String[] reportDate(String input) {
        String[] parts = new String[2];
        try {
            if (input == null || input.equals("")) {
                parts[0] = null;
                parts[1] = null;
            } else {
                parts = input.split("\\ - ");
                parts[0] += " 00:00:00";
                parts[1] += " 23:59:59";
            }
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return parts;
    }

    public static String[] reportDateV2(String input) {
        String[] parts = new String[2];
        try {
            if (input == null || input.equals("")) {
                parts[0] = null;
                parts[1] = null;
            } else {
                parts = input.split("\\ - ");
                parts[0] = convertDateSql(parts[0]) + " 00:00:00";
                parts[1] = convertDateSql(parts[1]) + " 23:59:59";
            }
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return parts;
    }

    public static Path getPathByTime() {
        LocalDate currentDate = LocalDate.now();
        return Paths.get(String.valueOf(currentDate.getYear()), String.valueOf(currentDate.getMonthValue()), String.valueOf(currentDate.getDayOfMonth()));
    }

    public static List<AjaxSearchDto> listAjax(List<String[]> input, int type) {
        log.info("List<String[]> input|" + input);
        List<AjaxSearchDto> listAjax = new ArrayList<>();
        for (String[] strings : input) {
            AjaxSearchDto dto = new AjaxSearchDto();
            dto.setId(strings[0]);
            dto.setDeeplink(strings[2]);
            dto.setDeeplinkParams(strings[3]);
            if(type == 0)
                dto.setText(strings[0] + " - " + strings[1]);
            else
                dto.setText(strings[1]);
            listAjax.add(dto);
        }
        log.info("List<AjaxSearchDto> listAjax|" + listAjax);
        return listAjax;
    }

    public static String[] getTimeNowV2(){
        try {
            LocalDate currentDate = LocalDate.now();
            String[] results = new String[4];
            results[0] = currentDate.getYear() + "/" + currentDate.getMonthValue() + "/" + currentDate.getDayOfMonth();
            results[1] = String.valueOf(currentDate.getYear());
            results[2] = String.valueOf(currentDate.getMonthValue());
            results[3] = String.valueOf(currentDate.getDayOfMonth());
            return results;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }

    public static String[] reportMonth(String input) {
        String[] parts = new String[2];
        try {
            if (input == null || input.equals("")) {
                parts[0] = null;
                parts[1] = null;
            } else {
                parts = input.split(" - ");
            }
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return parts;
    }

    public static List<String> generateDateArray(String firstDate, String secondDate) {
        List<String> dateArray = new ArrayList<>();

        LocalDate startDate = LocalDate.parse(firstDate);
        LocalDate endDate = LocalDate.parse(secondDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (!startDate.isAfter(endDate)) {
            dateArray.add(startDate.format(formatter));
            startDate = startDate.plusDays(1);
        }

        return dateArray;
    }

    public static String convertDateSql(String input) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String outputDate = "";
        try {
            Date date = inputFormat.parse(input);
            outputDate = outputFormat.format(date);
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return outputDate;
    }

    public static JsonArray covertListToArr(List<String> list) {
        JsonArray result = new JsonArray();
        if(list != null) for (String s : list) result.add(s);
        return result;
    }
}
