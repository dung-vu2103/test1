package com.ringme.cms.controller.kakoakcms;

import com.ringme.cms.common.UploadFile;
import com.ringme.cms.dto.kakoakcms.BookDto;
import com.ringme.cms.dto.kakoakcms.sticker.StickerItemDto;
import com.ringme.cms.model.kakoakcms.Book;
import com.ringme.cms.model.kakoakcms.User1;
import com.ringme.cms.model.kakoakcms.sticker.Sticker;
import com.ringme.cms.model.kakoakcms.sticker.StickerItem;
import com.ringme.cms.service.BookService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("book")
@Log4j2
public class BookController {
    @Autowired
    UploadFile uploadFile;
    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;
    @Autowired
    private MessageSource messageSource;

    @GetMapping(value = {"/index", "/index/{page}"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize,
                             @RequestParam(name = "userId") Integer userId, Model model) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        Page<Book> objectPage = bookService.get(userId, page, pageSize);
        User1 user1 = userService.findById(userId);
        model.addAttribute("currentPage", page);
        model.addAttribute("userId", userId);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        model.addAttribute("user1", user1);
        model.addAttribute("title", messageSource.getMessage("title.stickerItem", null, LocaleContextHolder.getLocale()));
        return "index3";
    }
    @GetMapping("/create")
    public String create(@RequestParam(name = "userId") Integer userId, Model model) {
        Book dto = new Book();
        dto.setUserId(userId);
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.sticker-item.create", null, LocaleContextHolder.getLocale()));
        return "form3";
    }
    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Integer id, Model model) {
        Book dto = bookService.findById(id);
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.sticker-item.update", null, LocaleContextHolder.getLocale()));
        return "form3";
    }
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("model") BookDto dto, Errors error, RedirectAttributes redirectAttributes) {
        log.info("---SAVE DTO---|" + dto);
        if(!error.hasErrors()){
            Book object = new Book();
            if(dto.getId() == null) {
                object.setCreateDate(new Date());
            } else {
                object = bookService.findById(dto.getId());
            }
            object.setUserId(dto.getUserId());
            object.setBook_name(dto.getBook_name());
            object.setPrice(dto.getPrice());

            bookService.save(object);
        } else {
            log.error("ERROR|Save|" + error);
            if(dto.getId() == null)
                return "redirect:/book/create?userId=" + dto.getUserId();
            else
                return "redirect:/book/update/" + dto.getId() + "userId=" + dto.getUserId();
        }
        if(dto.getId() == null)
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
        else
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/book/index?userId=" + dto.getUserId();
    }
    @GetMapping(value = {"/delete", "/delete/{page}"})
    public String delete(@PathVariable(required = false) Integer page,
                         @RequestParam(name = "pageSize", required = false) Integer pageSize,
                         @RequestParam(name = "id", required = false) Integer id,
                         RedirectAttributes redirectAttributes) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        log.info("id|{}", id);
        bookService.delete(id);
        redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/book/index/";
    }
}
