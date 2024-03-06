package com.ringme.cms.controller.kakoak.game;

import com.ringme.cms.config.AppConfiguration;
import com.ringme.cms.dto.kakoak.game.GameCategoryDto;
import com.ringme.cms.model.kakoak.game.GameCategory;
import com.ringme.cms.service.kakoak.game.GameCategoryService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.internal.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/user/game/category")
public class GameCategoryController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    GameCategoryService gameCategoryService;

    @Autowired
    AppConfiguration appConfiguration;

    @GetMapping(value = {"/", "/index", "/index/{page}", "/search", "/search/{page}"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "gameCateName", required = false) String gameCateName,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize, Model model) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        Page<GameCategory> objects = gameCategoryService.getList(gameCateName, page, pageSize);
        List<GameCategory> gameCategories = objects.toList();
        model.addAttribute("gameCateName", gameCateName);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objects.getTotalPages());
        model.addAttribute("totalItems", objects.getTotalElements());
        model.addAttribute("models", gameCategories);
        model.addAttribute("title", messageSource.getMessage("title.game.category", null, LocaleContextHolder.getLocale()));
        log.info("LIST ICON PRIZE|" + objects.toList());
        return "user/game/category/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        GameCategoryDto dto = new GameCategoryDto();
        model.addAttribute("model", dto);
        model.addAttribute("statusForm", "0");
        model.addAttribute("title", messageSource.getMessage("title.game.category.create", null, LocaleContextHolder.getLocale()));
        return "user/game/category/create";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model) {
        GameCategory dto = gameCategoryService.findById(id);
        model.addAttribute("model", dto);
        model.addAttribute("statusForm", "1");
        model.addAttribute("title", messageSource.getMessage("title.game.category.update", null, LocaleContextHolder.getLocale()));
        log.info("UPDATE |" + dto);
        return "user/game/category/update";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model) {
        GameCategory object = gameCategoryService.findById(id);
        model.addAttribute("model", object);
        model.addAttribute("id", id);
        model.addAttribute("titleDetail", messageSource.getMessage("title.game.category.detail", null, LocaleContextHolder.getLocale()));
        log.info("DETAIL|" + object);
        String view = "user/game/category/index::gameCategory_view";
        return view;
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("model") GameCategoryDto dto, Errors error,
                       @ModelAttribute("statusForm") String statusForm,
                       @RequestParam String thumbUpload,
                       RedirectAttributes redirectAttributes) {
        log.info("---SAVE DTO---|" + dto);

        if(!error.hasErrors()){
            gameCategoryService.saveGameCate(dto, statusForm, thumbUpload);
        } else {
            log.error("ERROR|Save|" + error);
            if(statusForm.equals("0"))
                return "redirect:/user/game/category/create";
            if(statusForm.equals("1")){
                String redirectUrl = "/user/game/category/update/" + dto.getId();
                return "redirect:" + redirectUrl;
            }
        }
        if(statusForm.equals("0"))
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
        if(statusForm.equals("1"))
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/user/game/category/index";
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
            gameCategoryService.deletegameCate(id);
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("ERROR" + e.getMessage(), e);
        }
        return "redirect:/user/game/category/index/" + page + "?pageSize=" + pageSize;
    }
}
