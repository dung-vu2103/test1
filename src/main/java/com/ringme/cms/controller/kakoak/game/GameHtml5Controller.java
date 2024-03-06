package com.ringme.cms.controller.kakoak.game;

import com.ringme.cms.config.AppConfiguration;
import com.ringme.cms.dto.kakoak.game.GameHtml5Dto;
import com.ringme.cms.model.kakoak.game.GameHtml5;
import com.ringme.cms.service.kakoak.game.GameHtml5Service;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.internal.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/user/game/html5")
public class GameHtml5Controller {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    GameHtml5Service gameHtml5Service;

    @Autowired
    AppConfiguration appConfiguration;

    @GetMapping(value = {"/", "/index", "/index/{page}", "/search", "/search/{page}"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "gameHtml5Name", required = false) String gameHtml5Name,
                             @RequestParam(name = "gameHtml5Visible", required = false) Integer visible,
                             @RequestParam(name = "gameHtml5Font", required = false) String font,
                             @RequestParam(name = "gameHtml5Order", required = false) Integer order,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize, Model model) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        Page<GameHtml5> objects = gameHtml5Service.getList(gameHtml5Name, visible, font, order, page, pageSize);
        List<GameHtml5> gameHtml5s = objects.toList();
        model.addAttribute("gameHtml5Name", gameHtml5Name);
        model.addAttribute("gameHtml5Visible", visible);
        model.addAttribute("gameHtml5Font", font);
        model.addAttribute("gameHtml5Order", order);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objects.getTotalPages());
        model.addAttribute("totalItems", objects.getTotalElements());
        model.addAttribute("models", gameHtml5s);
        model.addAttribute("title", messageSource.getMessage("title.game.html5", null, LocaleContextHolder.getLocale()));
        log.info("LIST ICON PRIZE|" + objects.toList());
        return "user/game/html5/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        GameHtml5Dto dto = new GameHtml5Dto();
        model.addAttribute("model", dto);
        model.addAttribute("statusForm", "0");
        model.addAttribute("title", messageSource.getMessage("title.game.html5.create", null, LocaleContextHolder.getLocale()));
        return "user/game/html5/create";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model) {
        GameHtml5 dto = gameHtml5Service.findById(id);
        model.addAttribute("model", dto);
        model.addAttribute("statusForm", "1");
        model.addAttribute("title", messageSource.getMessage("title.game.html5.update", null, LocaleContextHolder.getLocale()));
        log.info("UPDATE |" + dto);
        return "user/game/html5/update";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model) {
        GameHtml5 object = gameHtml5Service.findById(id);
        model.addAttribute("model", object);
        model.addAttribute("id", id);
        model.addAttribute("titleDetail", messageSource.getMessage("title.game.html5.detail", null, LocaleContextHolder.getLocale()));
        log.info("DETAIL|" + object);
        String view = "user/game/html5/index::gameHtml5_view";
        return view;
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("model") GameHtml5Dto dto, Errors error,
                       @ModelAttribute("statusForm") String statusForm,
                       @RequestParam(name = "thumbUpload", required = false) String thumbUpload,
                       @RequestParam(name = "thumbUpload2", required = false) String thumbUpload2,
                       @RequestParam(name = "ttfUpfileload", required = false) MultipartFile file,
                       RedirectAttributes redirectAttributes) {
        log.info("---SAVE DTO---|" + dto);

        if(!error.hasErrors()){
            gameHtml5Service.saveGameHtml5(dto, statusForm, thumbUpload, thumbUpload2, file);
        } else {
            log.error("ERROR|Save|" + error);
            if(statusForm.equals("0"))
                return "redirect:/user/game/html5/create";
            if(statusForm.equals("1")){
                String redirectUrl = "/user/game/html5/update/" + dto.getId();
                return "redirect:" + redirectUrl;
            }
        }
        if(statusForm.equals("0"))
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
        if(statusForm.equals("1"))
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/user/game/html5/index";
    }

    @GetMapping(value = {"/delete", "/delete/{page}"})
    public String delete(@PathVariable(required = false) Integer page,
                         @RequestParam(name = "pageSize", required = false) Integer pageSize,
                         @RequestParam(name = "id", required = false) Long id,
                         RedirectAttributes redirectAttributes) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        try {
            log.info("id|{}", id);
            gameHtml5Service.deleteGameHtml5(id);
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("ERROR" + e.getMessage(), e);
        }
        return "redirect:/user/game/html5/index/" + page + "?pageSize=" + pageSize;
    }
}
