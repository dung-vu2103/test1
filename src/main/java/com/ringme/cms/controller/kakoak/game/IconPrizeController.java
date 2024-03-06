//package com.ringme.cms.controller.kakoak.game;
//
//import com.ringme.cms.config.AppConfiguration;
//import com.ringme.cms.dto.kakoakcms.game.IconPrizeDto;
//import com.ringme.cms.model.kakoakcms.game.IconPrize;
//import com.ringme.cms.service.kakoak.game.IconPrizeService;
//import lombok.extern.log4j.Log4j2;
//import org.modelmapper.internal.Errors;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import javax.validation.Valid;
//import java.util.List;
//
//@Controller
//@Log4j2
//@RequestMapping("/user/game/icon-prize")
//public class IconPrizeController {
//    @Autowired
//    private MessageSource messageSource;
//
//    @Autowired
//    IconPrizeService iconPrizeService;
//
//    @Autowired
//    AppConfiguration appConfiguration;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @GetMapping(value = {"/", "/index", "/index/{page}", "/search", "/search/{page}"})
//    public String getAllPage(@PathVariable(required = false) Integer page,
//                             @RequestParam(name = "iconName", required = false) String iconName,
//                             @RequestParam(name = "iconCode", required = false) String iconCode,
//                             @RequestParam(name = "iconActive", required = false) Integer iconActive,
//                             @RequestParam(name = "iconType", required = false) String iconType,
//                             @RequestParam(name = "pageSize", required = false) Integer pageSize, Model model) {
//        if(page == null)
//            page = 1;
//        if(pageSize == null)
//            pageSize = 10;
//        Page<IconPrize> objects = iconPrizeService.getListIconPrize(iconName, iconCode, iconActive, iconType, page, pageSize);
//        List<IconPrize> iconPrizes = objects.toList();
//        model.addAttribute("iconName", iconName);
//        model.addAttribute("iconCode", iconCode);
//        model.addAttribute("iconActive", iconActive);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("pageSize", pageSize);
//        model.addAttribute("totalPages", objects.getTotalPages());
//        model.addAttribute("totalItems", objects.getTotalElements());
//        model.addAttribute("models", iconPrizes);
//        model.addAttribute("title", messageSource.getMessage("title.game.icon-prize", null, LocaleContextHolder.getLocale()));
//        log.info("LIST ICON PRIZE|" + objects.toList());
//        return "user/game/icon-prize/index";
//    }
//
//    @GetMapping("/create")
//    public String create(Model model) {
//        IconPrizeDto dto = new IconPrizeDto();
//        model.addAttribute("model", dto);
//        model.addAttribute("statusForm", "0");
//        model.addAttribute("title", messageSource.getMessage("title.game.icon-prize.create", null, LocaleContextHolder.getLocale()));
//        return "user/game/icon-prize/create";
//    }
//
//    @GetMapping("/update/{id}")
//    public String update(@PathVariable(name = "id") Long id, Model model) {
//        IconPrize dto = iconPrizeService.findById(id);
//        model.addAttribute("model", dto);
//        model.addAttribute("statusForm", "1");
//        model.addAttribute("title", messageSource.getMessage("title.game.icon-prize.update", null, LocaleContextHolder.getLocale()));
//        log.info("UPDATE ICON PRIZE|" + dto);
//        return "user/game/icon-prize/update";
//    }
//
//    @GetMapping("/detail/{id}")
//    public String detail(@PathVariable(name = "id") Long id, Model model) {
//        IconPrize object = iconPrizeService.findById(id);
//        model.addAttribute("model", object);
//        model.addAttribute("id", id);
//        model.addAttribute("titleDetail", messageSource.getMessage("title.game.icon-prize.detail", null, LocaleContextHolder.getLocale()));
//        log.info("DETAIL ICON PRIZE|" + object);
//        String view = "user/game/icon-prize/index::iconPrize_view";
//        return view;
//    }
//
//    @PostMapping("/save")
//    public String save(@Valid @ModelAttribute("model") IconPrizeDto dto, Errors error,
//                       @ModelAttribute("statusForm") String statusForm,
//                       @RequestParam String thumbUpload,
//                       RedirectAttributes redirectAttributes) {
//        log.info("---SAVE DTO---|" + dto);
//
//        if(!error.hasErrors()){
//            iconPrizeService.saveIconPrize(dto, statusForm, thumbUpload);
//        } else {
//            log.error("ERROR|Save|" + error);
//            if(statusForm.equals("0"))
//                return "redirect:/user/game/icon-prize/create";
//            if(statusForm.equals("1")){
//                String redirectUrl = "/user/game/icon-prize/update/" + dto.getId();
//                return "redirect:" + redirectUrl;
//            }
//        }
//        if(statusForm.equals("0"))
//            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
//        if(statusForm.equals("1"))
//            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
//        return "redirect:/user/game/icon-prize/index";
//    }
//
//    @GetMapping(value = {"/delete", "/delete/{page}"})
//    public String delete(@PathVariable(required = false) Integer page,
//                         @RequestParam(name = "pageSize", required = false) Integer pageSize,
//                         @RequestParam(name = "id", required = false) Long id,
//                         RedirectAttributes redirectAttributes) {
//        if(page == null)
//            page = 1;
//        if(pageSize == null)
//            pageSize = 10;
//
//        try {
//            log.info("id|{}", id);
//            iconPrizeService.deleteIcon(id);
//            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));
//        } catch (Exception e) {
//            log.error("ERROR" + e.getMessage(), e);
//        }
//        return "redirect:/user/game/icon-prize/index/" + page + "?pageSize=" + pageSize;
//    }
//
//    @GetMapping(value = {"/active-block", "/active-block/{page}"})
//    public String activeBlock(@PathVariable(required = false) Integer page,
//                              @RequestParam(name = "pageSize", required = false) Integer pageSize,
//                              @RequestParam(name = "id", required = false) Long id,
//                              @RequestParam(name = "status", required = false) Integer status,
//                              RedirectAttributes redirectAttributes) {
//        if(page == null)
//            page = 1;
//        if(pageSize == null)
//            pageSize = 10;
//
//        try {
//            if(status == 1) {
//                iconPrizeService.block(id);
//                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("block.success",
//                        null, LocaleContextHolder.getLocale()));
//                //call api clear cache
//                callApiAndHandleResponse(redirectAttributes);
//            } else if(status == 0) {
//                //kiểm tra xem có icon nào active không mới đc active
//                Integer coutnIcon = iconPrizeService.countIconPrizeActive();
//                if (coutnIcon == 0) {
//                    iconPrizeService.active(id);
//                    redirectAttributes.addFlashAttribute("success", messageSource.getMessage("active.success",
//                            null, LocaleContextHolder.getLocale()));
//                    //call api clear cache
//                    callApiAndHandleResponse(redirectAttributes);
//                } else {
//                    redirectAttributes.addFlashAttribute("error", messageSource.getMessage("active.iconPrize.error",
//                            null, LocaleContextHolder.getLocale()));
//                }
//            } else {
//                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("active.error",
//                        null, LocaleContextHolder.getLocale()));
//            }
//        } catch (Exception e) {
//            log.error("ERROR" + e.getMessage(), e);
//        }
//        return "redirect:/user/game/icon-prize/index/" + page + "?pageSize=" + pageSize;
//    }
//
//    public void callApiAndHandleResponse(RedirectAttributes redirectAttributes) {
//        try {
//            // Sau khi lưu thành công, thực hiện gọi API
//            String apiEndpoint = appConfiguration.getApiClearCache();
//            ResponseEntity<String> response = restTemplate.postForEntity(apiEndpoint, null, String.class);
//
//            // Kiểm tra kết quả gọi API
//            if (response.getStatusCode() == HttpStatus.OK) {
//                log.info("Media|API call api convert video successful");
//                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("retrySuccess", null, LocaleContextHolder.getLocale()));
//            } else {
//                log.error("Media|API call api convert video failed");
//                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("retryError", null, LocaleContextHolder.getLocale()));
//            }
//        } catch (Exception e) {
//            log.error("ERROR|" + e.getMessage(), e);
//            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("retryError", null, LocaleContextHolder.getLocale()));
//        }
//    }
//
//}
