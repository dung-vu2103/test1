package com.ringme.cms.controller.kakoakcms.user;

import com.ringme.cms.common.UpFile;
import com.ringme.cms.common.UploadFile;
import com.ringme.cms.dto.kakoakcms.user.BookDto;

import com.ringme.cms.model.kakoakcms.user.Book;
import com.ringme.cms.model.kakoakcms.user.User1;

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
    UpFile upFile;
    @Autowired
    private MessageSource messageSource;

    @GetMapping(value = {"/index", "/index/{page}"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize,
                             @RequestParam(name = "userId") Integer userId,
                             Model model) {
        if(page == null)
            page =  1;
        if(pageSize == null)
            pageSize =  10;
        Page<Book> objectPage = bookService.get(userId, page, pageSize);
        User1 user1 = userService.findById(userId);
        model.addAttribute("currentPage", page);
        model.addAttribute("userId", userId);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        model.addAttribute("user1", user1);
        model.addAttribute("title", messageSource.getMessage("title.book", null, LocaleContextHolder.getLocale()));
        return "/book-list/index";
    }
    @GetMapping("/create")
    public String create(@RequestParam(name = "userId") Integer userId, Model model) {
        Book dto = new Book();
        dto.setUserId(userId);
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.book.create", null, LocaleContextHolder.getLocale()));
        return "/book-list/form";
    }
    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Integer id, Model model) {
        Book dto = bookService.findById(id);
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.sticker-item.update", null, LocaleContextHolder.getLocale()));
        return "/book-list/form";
    }
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("model") BookDto dto, Errors error,
                       @RequestParam(name = "imageUrlUpload",required = false) MultipartFile imageUrlUpload,
                       RedirectAttributes redirectAttributes) {

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
          String[] file=upFile.fileName(imageUrlUpload,"books");
          if(file !=null &&(dto.getId() == null || !file[2].equals(dto.getImage())) )
          {
              object.setImage("/" + file[1]);
             upFile.upload(imageUrlUpload,file);
          }
          log.info("date" + object.getCreateDate());
          log.info("1234" +object.getBook_name());
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
                         @RequestParam(name = "userId", required = false) Integer userId,
                         RedirectAttributes redirectAttributes) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        log.info("id|{}", id);
        bookService.delete(id);
        redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/book/index/" +page +"?pageSize=" +pageSize + "&userId=" +userId;
    }
}
