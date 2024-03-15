package com.ringme.cms.controller.kakoakcms;

import com.ringme.cms.common.UpFile;
import com.ringme.cms.dto.kakoakcms.UserDto;
import com.ringme.cms.model.kakoakcms.User1;
import com.ringme.cms.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@RequestMapping("/user1")
public class UserController1 {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    UserService userService;
    @Autowired
    UpFile upFile;
    @GetMapping(value={"/get","/get/{page}"})
    private String index(@PathVariable(required = false) Integer page,
                          @RequestParam(name = "pageSize", required = false) Integer pageSize,
                          @RequestParam(value = "name", required = false) String name
            , Model model) {
        if (page == null) {
            page = (Integer) 1;
        }
        if (pageSize == null) {
            pageSize = (Integer) 10;
        }
        UserDto userDto = userService.processSearch(name);
        Page<User1> oblectPage = userService.getAll(userDto, page, pageSize);
        List<User1> users = oblectPage.toList();
        model.addAttribute("currentPage", page);
        model.addAttribute("name", name);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", Optional.of(oblectPage.getTotalPages()));
        model.addAttribute("totalItems", Optional.of(oblectPage.getTotalElements()));
        model.addAttribute("users", users);
        return "index2";
    }
    @GetMapping("/create")
    public String create(Model model) {
        User1 dto = new User1();
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.sticker.create", null, LocaleContextHolder.getLocale()));
        return "form1";
    }
    @GetMapping("/view/{id}")
    public String detail(@PathVariable(name = "id") Integer id, Model model) {
        User1 object = userService.findById(id);
        log.info("objecttttt" + object);
        model.addAttribute("user", object);
        return "index2::view_detail";
    }
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("model") UserDto dto, Errors error,
                       @RequestParam(name = "iconUpload", required = false) String iconUpload
            , RedirectAttributes redirectAttributes) {
        log.info("---SAVE DTO---|" + dto);
        if(!error.hasErrors()){
            User1 object = new User1();
            if(dto.getId() == null) {
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
            } else {
                object = userService.findById(dto.getId());
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
            }
            Path icon=upFile.createImageFile(iconUpload,"image");
            if (icon != null) {
                object.setImage(File.separator +icon);
            }
            object.setName(dto.getName());
            object.setAge(dto.getAge());
            object.setAddress(dto.getAddress());
            object.setPhone(dto.getPhone());
            object.setEmail(dto.getEmail());
            log.info("object1111111111111111 " + object);
            userService.save(object);
        } else {
            log.error("ERROR|Save|" + error);
            if(dto.getId() == null)
                return "redirect:/user1/create";
            else
                return "redirect:/user1/update/" + dto.getId();
        }
        if(dto.getId() == null)
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
        else
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/user1/get";
    }
    @GetMapping(value = {"/delete", "/delete/{page}"})
    public String delete(@PathVariable(required = false) Integer page,
                         @RequestParam(name = "pageSize", required = false) Integer pageSize,
                         @RequestParam(name = "id", required = false) Integer id,
                         RedirectAttributes redirectAttributes) {
        if(page == null)
            page = (Integer) 1;
        if(pageSize == null)
            pageSize = (Integer) 10;
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("success",
                    messageSource.getMessage("title.delete.success", null,
                            LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("ERROR" + e.getMessage(), e);
        }
        return "redirect:/user1/get/";
    }
    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Integer id, Model model) {
        User1 min = userService.findById(id);
        model.addAttribute("model",min);
        return "form1";
    }
}
