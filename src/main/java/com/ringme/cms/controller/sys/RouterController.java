package com.ringme.cms.controller.sys;


import com.ringme.cms.model.sys.Router;
import com.ringme.cms.service.sys.RouterService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Log4j2
@RequestMapping("/sys")
public class RouterController {
    @Autowired
    RouterService routerService;
    @Autowired
    private MessageSource messageSource;
//    @GetMapping("/router/index")
//    public String indexRouter(Model model) {
//        List<Router> routerActive = routerService.findAllRouterActive();
//        List<Router> routerUnActive = routerService.findAllRouterUnActive();
//        model.addAttribute("routerActive", routerActive);
//        model.addAttribute("routerUnActive", routerUnActive);
//        return "router";
//    }

    @PostMapping("/router/add")
    public String addRouter(@RequestParam("router") String router, @RequestParam("router-des") String routerDes, RedirectAttributes redirectAttributes) {
        if (router == null || router.equals("")) {
            redirectAttributes.addFlashAttribute("error", "Vui không để trống đường dẫn");
            return "redirect:/router/index";
        }
        if (routerDes == null || routerDes.equals("")) {
            redirectAttributes.addFlashAttribute("error", "Vui không để trống trường mô tả");
            return "redirect:/router/index";
        } else {
            Router r = new Router();
            r.setRouter_link(router.trim());
            r.setDescription(routerDes.trim());
            r.setActive(false);
            try {
                routerService.addRouter(r);
                redirectAttributes.addFlashAttribute("success", "Thêm đường dẫn thành công!");
                return "redirect:/router/index";
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Đường dẫn đã tồn tại!");
                return "redirect:/router/index";
            }
        }
    }

    @PostMapping("/router/active")
    public String activeRouter(@RequestParam("router") List<Long> routers) {
        for (Long i : routers) {
            routerService.updateStatus(true, i);
        }
        return "redirect:/router/index";
    }

    @PostMapping("/router/unactive")
    public String unActiveRouter(@RequestParam("router") List<Long> routers) {
        for (Long i : routers) {
            routerService.updateStatus(false, i);
        }
        return "redirect:/router/index";
    }
    @GetMapping(value = {"/router/index", "/router/index/{page}", "/router/search/{page}", "/router/search"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize, Model model) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        Page<Router> models = routerService.page(page, pageSize);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", models.getTotalPages());
        model.addAttribute("totalItems", models.getTotalElements());
        model.addAttribute("models", models.toList());
        return "sys/router";
    }
    @PostMapping("/router/save")
    public String save(@RequestParam(value = "id", required = false) Long id, @RequestParam("router") String router,
                       @RequestParam("router-des") String routerDes, RedirectAttributes redirectAttributes) {
        if (router == null || router.equals("")) {
            redirectAttributes.addFlashAttribute("error", "Vui không để trống đường dẫn");
            return "redirect:/sys/router/index";
        }
        if (routerDes == null || routerDes.equals("")) {
            redirectAttributes.addFlashAttribute("error", "Vui không để trống trường mô tả");
            return "redirect:/sys/router/index";
        } else {
            Router r = new Router();
            r.setId(id);
            r.setRouter_link(router.trim());
            r.setDescription(routerDes.trim());
            r.setActive(true);
            try {
                routerService.addRouter(r);
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("create.success", null, LocaleContextHolder.getLocale()));
                if(id == null)
                    redirectAttributes.addFlashAttribute("success", messageSource.getMessage("create.success", null, LocaleContextHolder.getLocale()));
                else
                    redirectAttributes.addFlashAttribute("success", messageSource.getMessage("update.success", null, LocaleContextHolder.getLocale()));
                return "redirect:/sys/router/index";
            } catch (Exception e) {
                log.error("ERROR|" + e.getMessage(), e);
                redirectAttributes.addFlashAttribute("error", "Đường dẫn đã tồn tại!");
                return "redirect:/sys/router/index";
            }
        }
    }
    @GetMapping("/router/active-block")
    public String activeBlock(@RequestParam("id") Long id, @RequestParam boolean status) {
        routerService.updateStatus(!status, id);
        return "redirect:/sys/router/index";
    }
    @GetMapping("/router/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        routerService.delete(id);
        redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/sys/router/index";
    }
}
