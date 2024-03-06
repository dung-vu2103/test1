package com.ringme.cms.controller.kakoak.game;

import com.ringme.cms.common.ExportExcel;
import com.ringme.cms.config.AppConfiguration;
import com.ringme.cms.dto.AjaxSearchDto;
import com.ringme.cms.model.kakoak.game.GameCategory;
import com.ringme.cms.model.kakoak.game.GameStat;
import com.ringme.cms.service.kakoak.game.GameHtml5Service;
import com.ringme.cms.service.kakoak.game.GameStatService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/user/game/stat")
public class GameStatController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    GameStatService gameStatService;

    @Autowired
    AppConfiguration appConfiguration;

    @Autowired
    GameHtml5Service gameHtml5Service;

    @Autowired
    ExportExcel excel;

    @GetMapping(value = {"/", "/index", "/index/{page}", "/search", "/search/{page}"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "gameId", required = false) Long gameId,
                             @RequestParam(name = "publishedTime", required = false) String date,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize,
                             Model model) throws UnsupportedEncodingException {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;

        String decodedDate = null;

        if (date != null) {
            decodedDate = URLEncoder.encode(date, "UTF-8");
            log.info("Time search encode: " + decodedDate);
        }

        Page<GameStat> objects = gameStatService.getList(gameId, date, page, pageSize);
        List<GameStat> gameStats = objects.toList();
        model.addAttribute("gameId", gameId);
        model.addAttribute("gameName", gameHtml5Service.getGameName(gameId));
        model.addAttribute("publishedTime", date);
        model.addAttribute("dateEncode", decodedDate);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objects.getTotalPages());
        model.addAttribute("totalItems", objects.getTotalElements());
        model.addAttribute("models", gameStats);
        model.addAttribute("title", messageSource.getMessage("title.game.stat", null, LocaleContextHolder.getLocale()));
        log.info("LIST ICON PRIZE|" + objects.toList());
        return "user/game/stat/index";
    }

    @GetMapping("/gameId-ajax-search")
    public ResponseEntity<List<AjaxSearchDto>> cateIdAjaxSearch(@RequestParam(name = "input_", required = false) String input) {
        return new ResponseEntity<>(gameHtml5Service.ajaxSearchGameId(input), HttpStatus.OK);
    }

    @PostMapping("/export")
    public void export(@RequestParam(name = "gameId", required = false) Long gameId,
                       @RequestParam(name = "dateRanger", required = false) String date,
                       HttpServletResponse response) {
        response = excel.setResponse(response);
        gameStatService.exportExcel(gameId, date, response);
    }
}
