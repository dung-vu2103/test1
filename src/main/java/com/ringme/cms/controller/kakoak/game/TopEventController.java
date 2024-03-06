package com.ringme.cms.controller.kakoak.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ringme.cms.config.AppConfiguration;
import com.ringme.cms.dto.kakoak.game.TopEventDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/user/game/top-event")
public class TopEventController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    AppConfiguration appConfiguration;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = {"/", "/index", "/search"})
    public String getData(@RequestParam(name = "startTime", required = false) String startTime,
                          @RequestParam(name = "endTime", required = false) String endTime,
                          @RequestParam(name = "publishedTime", required = false) String date,
                          Model model) throws UnsupportedEncodingException {
        String decodedDateStart = "";
        String decodedDateEnd = "";
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (startTime != null && endTime != null) {
            decodedDateStart = URLEncoder.encode(startTime, "UTF-8");
            decodedDateEnd = URLEncoder.encode(endTime, "UTF-8");
            log.info("Time search encode: " + decodedDateStart + " - " + decodedDateEnd);
        } else if (startTime == null || endTime == null) {
            startTime = currentDate.format(formatter);
            endTime = currentDate.format(formatter);
        }

        model.addAttribute("publishedTime", date);
        model.addAttribute("startTime", startTime);
        model.addAttribute("decodedDateStart", decodedDateStart);
        model.addAttribute("endTime", endTime);
        model.addAttribute("decodedDateEnd", decodedDateEnd);

        // Gọi API để lấy dữ liệu
        callApiTopGameEvent(model, startTime, endTime);

        // Trả về tên view
        return "user/game/top-event/index";
    }

    private void callApiTopGameEvent(Model model, String startTime, String endTime) {
        try {
            // Mã code hiện tại của bạn để gọi API...
            String apiEndpoint = appConfiguration.getApiTopGameEvent() + "start_date=" + startTime + "&end_date=" + endTime;
            ResponseEntity<String> response = restTemplate.getForEntity(apiEndpoint, String.class);

            // Giả sử "response" là đối tượng phản hồi từ API của bạn
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Media|API call api top game event successful");

                // Trích xuất và thêm dữ liệu vào model
                List<TopEventDto> weekData = extractWeekData(response.getBody());
                model.addAttribute("models", weekData);

                // Thêm các thuộc tính khác vào model nếu cần
                model.addAttribute("title", messageSource.getMessage("title.game.topEvent", null, LocaleContextHolder.getLocale()));
                model.addAttribute("success", messageSource.getMessage("retrySuccess", null, LocaleContextHolder.getLocale()));
            } else {
                log.error("Media|API call api top game event failed");
                model.addAttribute("error", messageSource.getMessage("retryError", null, LocaleContextHolder.getLocale()));
            }
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
            model.addAttribute("error", messageSource.getMessage("retryError", null, LocaleContextHolder.getLocale()));
        }
    }
    private List<TopEventDto> extractWeekData(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody).get("week");

            if (jsonNode.isArray()) {
                return objectMapper.convertValue(jsonNode, new TypeReference<List<TopEventDto>>() {});
            }
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON response: " + e.getMessage(), e);
        }

        return Collections.emptyList();
    }
}
