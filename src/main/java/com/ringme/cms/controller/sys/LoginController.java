package com.ringme.cms.controller.sys;


import com.ringme.cms.common.Helper;
import com.ringme.cms.dto.*;
import com.ringme.cms.repository.sys.UserRepository;
import com.ringme.cms.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller

public class LoginController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AntPathMatcher antPathMatcher;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request,String username, String password) {
//        System.out.println("modellllllll" + request);
        System.out.println(passwordEncoder.encode("123456"));
//        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserSecurity);
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserSecurity) {
//            User user = userRepository.findById(1L).orElseThrow(() -> {
//                throw new InvalidParameterException("failed");
//            });
//
//            user.setIsLogin(1);
//            userRepository.save(user);
//        User user = userRepository.findByUsername(username);
//        if (user == null || !user.getPassword().equals(password)) {
//            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
//            return "redirect:/login";
//        }
//        return "login";
            return "redirect:/index";
        }
        return "login";
    }
//    @GetMapping("/login")
//    public String login(String username, String password, Model model){
//        User user = userRepository.findByUsername(username);
//        if (user == null || !user.getPassword().equals(password)) {
//            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
//            return "redirect:/login";
//        }
//        return "login";
//    }

    @GetMapping("/login-error")
    public String loginerror(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Tài khoản hoặc mật khẩu không chính xác. Vui lòng thử lại!");
        System.err.println("vl chu");
        return "redirect:/login";
    }

    @GetMapping("/")
    public String getHome() {
        return "redirect:/index";
    }

    @PostMapping("/login")
    public String login(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Captcha không chính xác!");
        return "redirect:/login";
    }

    @GetMapping({"/403"})
    public String error403() {
        System.out.println(passwordEncoder.encode("123456"));
        return "403";
    }

    @GetMapping({"/404"})
    public String error404() {
        System.out.println(passwordEncoder.encode("123456"));
        return "404";
    }

    @GetMapping("/index")
    public String index(HttpServletRequest httpServletRequest, Model model) {
        return "index";
    }
}