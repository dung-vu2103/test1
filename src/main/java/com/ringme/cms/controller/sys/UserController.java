package com.ringme.cms.controller.sys;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ringme.cms.dto.UserDto;
import com.ringme.cms.dto.UserUpdateDto;
import com.ringme.cms.model.sys.User;
import com.ringme.cms.repository.sys.UserRepository;
import com.ringme.cms.service.sys.UserService;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Controller
@Log4j2
@RequestMapping("/sys")
public class UserController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user/index")
    public String getAllUser(Model model) {
        return getAllUserPage(1, "0", null, null, null, null, model);
    }

    @GetMapping("/user/create")
    public String createUser(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        model.addAttribute("title", messageSource.getMessage("user.create",
                null, LocaleContextHolder.getLocale()));
        return "sys/user-create";
    }

    @GetMapping("/user/update/{id}")
    public String updateUser(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> user = userService.findByIdUser(id);
        if (user.isPresent()) {
            UserUpdateDto userDto = user.get().convertToDtoUpdate();
            System.out.println("userDto " + userDto);
            model.addAttribute("user", userDto);
            model.addAttribute("title", messageSource.getMessage("user.update",
                    null, LocaleContextHolder.getLocale()));
            return "sys/user-update";
        } else {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("user.noExist",
                    null, LocaleContextHolder.getLocale()));
            return "redirect:/sys/user/index";
        }
    }

    @PostMapping("/user/update")
    public String updateUser(@Valid @ModelAttribute("user") UserUpdateDto user, BindingResult bindingResult, Errors error, Model model, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        log.info("Vao Update|" + user);
        log.info("ERROR UPDATE|" + error);
        if (error.hasErrors()) {
            return "sys/user-update";
        } else {
            if (!user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("email.invalid",
                        null, LocaleContextHolder.getLocale()));
                return "redirect:/sys/user/update" + user.getId();
            }
            log.info("user1" + user);
            Optional<User> userDb = userRepository.findUserByUserName(user.getEmail().trim());
            String checkId = userRepository.checkId(user.getUsername());
            if(checkId != null && Long.parseLong(checkId) != userDb.get().getId()){
                redirectAttributes.addFlashAttribute("error", "Đã có username này");
                return "redirect:/sys/user/update/" + user.getId();
            }
            log.info("user-----2" + userDb);
            User userSave = user.convertToEntity();
            log.info("user-----" + userSave);
            Optional<User> userOptional = userRepository.findById(user.getId());
            if (userOptional.isPresent()) {
                if (!userSave.getEmail().equals(userOptional.get().getEmail())) {
                    Optional<User> userDbUpdate = userRepository.findUserByUserName(userSave.getEmail());
                    if (userDbUpdate.isPresent()) {
                        redirectAttributes.addFlashAttribute("error", messageSource.getMessage("user.sub", null, LocaleContextHolder.getLocale()));
                        return "redirect:/sys/user/update/" + userSave.getId();
                    }
                }
//                Optional<User> userAdmin = userRepository.findAllUserTypeAdmin(user.getName().trim());
//                if(userAdmin.isPresent()){
//                    if(userAdmin.get().getType_user().equals("0")){
//                        redirectAttributes.addFlashAttribute("error", "Phòng đã có Admin!");
//                        return "redirect:/sys/user/update/"+ userSave.getId();
//                    }
//                }
                userSave.setPassword(userOptional.get().getPassword());
                userSave.setActive(userOptional.get().isActive());
                log.info("userSave----" + userSave);
                userService.saveUser(userSave);
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
            } else {
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("accountNoExists", null, LocaleContextHolder.getLocale()));
            }
            return "redirect:/sys/user/index";
        }
    }
    @PostMapping("/user/save")
    public String saveUser(@Valid @ModelAttribute("user") UserDto user, BindingResult bindingResult, Errors error, Model model, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        log.info("Da Vao save|" + user);
        log.info(error);
        if (error.hasErrors()) {
            return "sys/user-create";
        } else {
            if (!user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("email.invalid", null, LocaleContextHolder.getLocale()));
                return "redirect:/sys/user/create";
            }
            Optional<User> userDb = userRepository.findUserByUserName(user.getEmail().trim());
            String checkId = userRepository.checkId(user.getUsername());
            if(checkId != null){
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("haveEmail", null, LocaleContextHolder.getLocale()));
                return "redirect:/sys/user/create";
            }
            System.out.println("userDb-----" + userDb);
            User userSave = user.convertToEntity();
            System.out.println("userSave-----" + userSave);
            if (user.getId() == null) {
//                if (!(user.getType_user().equals("0") || user.getType_user().equals("1"))) {
//                    redirectAttributes.addFlashAttribute("error", "Hãy chọn loại quyền!");
//                    return "redirect:/sys/user/create";
//                }
                if (userDb.isPresent()) {
                    redirectAttributes.addFlashAttribute("error", messageSource.getMessage("user.sub", null, LocaleContextHolder.getLocale()));
                    return "redirect:/sys/user/create";
                }
                if (user.getPassword().contains(" ")) {
                    redirectAttributes.addFlashAttribute("error", messageSource.getMessage("noSpacePass", null, LocaleContextHolder.getLocale()));
                    return "redirect:/sys/user/create";
                }
                if (StringUtils.isBlank(user.getPassword())) {
                    redirectAttributes.addFlashAttribute("error", messageSource.getMessage("passNotBlank", null, LocaleContextHolder.getLocale()));
                    return "redirect:/sys/user/create";
                }
                if (StringUtils.isBlank(user.getPassword2())) {
                    redirectAttributes.addFlashAttribute("error", messageSource.getMessage("passNotBlank", null, LocaleContextHolder.getLocale()));
                    return "redirect:/sys/user/create";
                }
                if (!user.getPassword().equals(user.getPassword2())) {

                    redirectAttributes.addFlashAttribute("error", messageSource.getMessage("passIncorrect", null, LocaleContextHolder.getLocale()));
                    return "redirect:/sys/user/create";
                }
//                Optional<User> userAdmin = userRepository.findAllUserTypeAdmin(user.getName().trim());
//                if(userAdmin.isPresent()){
//                    if(userAdmin.get().getType_user().equals("0")){
//                        redirectAttributes.addFlashAttribute("error", "Phòng đã có Admin!");
//                        return "redirect:/sys/user/create";
//                    }
//                }

                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
            }
            userSave.setPassword(passwordEncoder.encode(user.getPassword()));
            userSave.setActive(true);
            System.out.println("userSave2----" + userSave);
            userService.saveUser(userSave);
            return "redirect:/sys/user/index";
        }
    }

    @GetMapping("/user/index/{page}")
    public String getAllUserPage(@PathVariable("page") int page, @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "email", required = false) String email, @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "phone-number", required = false) String sdt, @RequestParam(name = "pageSize", required = false) Integer pageSize, Model model) {
        if(pageSize == null)
            pageSize = 10;
        Long idUser = 0L;
        Page<User> users;
        if (id != null) {
            if (id.trim().matches("\\d+")) idUser = Long.parseLong(id);
        }
        users = userService.pageUser(page, pageSize, idUser, email, name, sdt);
        List<User> userList = users.toList();
        log.info("userList|" + userList);
        List<String> accountType = userService.processAccountType(userList);
//        List<String> channelName = userService.processChannelName(userList);
//        log.info("channelName|" + channelName);
        model.addAttribute("id", id);
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("phone", sdt);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("totalItems", users.getTotalElements());
        model.addAttribute("accountType", accountType);
//        model.addAttribute("channelName", channelName);
        model.addAttribute("users", userList);
        return "sys/users";
    }

    @GetMapping("/user/search")
    public String searchUser(@RequestParam(name = "id", required = false) String id
            , @RequestParam(name = "email") String email, @RequestParam(name = "name") String name, @RequestParam(name = "phone-number") String sdt,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize, Model model) {
        return getAllUserPage(1, id, email, name, sdt, pageSize, model);
    }

    @GetMapping("/user/search/{page}")
    public String searchUser(@PathVariable(name = "page") Integer page, @RequestParam(name = "id", required = false) String id
            , @RequestParam(name = "email") String email, @RequestParam(name = "name") String name, @RequestParam(name = "phone-number") String sdt,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize, Model model) {
        return getAllUserPage(page, id, email, name, sdt, pageSize, model);
    }

    @GetMapping(value = {"/user/delete", "/user/delete/{page}"})
    public String delete(@PathVariable(required = false) Integer page,
                         @RequestParam(name = "pageSize", required = false) Integer pageSize,
                         @RequestParam(name = "id", required = false) Long id,
                         RedirectAttributes redirectAttributes) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        Optional<User> user = userService.findByIdUser(id);
        if (user.isPresent()) {
            try {
                userService.deleteUser(user.get().getId());
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.delete.success",
                        null, LocaleContextHolder.getLocale()));
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("title.delete.faile",
                        null, LocaleContextHolder.getLocale()));
            }
        } else {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("userrole.userNo",
                    null, LocaleContextHolder.getLocale()));
        }
        return "redirect:/sys/user/index/" + page + "?pageSize=" + pageSize;
    }

    @PostMapping("/user/resetpassword")
    public String resetPassword(@RequestParam(name = "passwordnew") String passwordnew
            , @RequestParam(name = "passwordnew2") String passwordnew2, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountName = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<User> user = userRepository.findUserByUserName(accountName);
        if (user.isPresent()) {
//            if (passwordEncoder.matches(password, user.get().getPassword())) {
                if (passwordnew.equals(passwordnew2)) {
                    if (!(passwordnew.matches("^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[^a-zA-Z]).{8,40}$") && passwordnew.matches("\\S+"))) {
                        redirectAttributes.addFlashAttribute("errorpass", messageSource.getMessage("errorpasss",
                                null, LocaleContextHolder.getLocale()));
                        redirectAttributes.addFlashAttribute("open", true);
                        return "redirect:/index";
                    } else {
                        user.get().setPassword(passwordEncoder.encode(passwordnew));
                        userService.saveUser(user.get());
                        redirectAttributes.addFlashAttribute("success", messageSource.getMessage("changePassword",
                                null, LocaleContextHolder.getLocale()));
                        return "redirect:/index";
                    }
                } else {
                    redirectAttributes.addFlashAttribute("errorpass", messageSource.getMessage("passwordConfirmation",
                            null, LocaleContextHolder.getLocale()));
                    redirectAttributes.addFlashAttribute("open", true);
                    return "redirect:/index";
                }
//            } else {
//                redirectAttributes.addFlashAttribute("errorpass", messageSource.getMessage("oldPasswordIncorrect",
//                        null, LocaleContextHolder.getLocale()));
//                redirectAttributes.addFlashAttribute("open", true);
//                return "redirect:/index";
//            }
        } else {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("accNoExists",
                    null, LocaleContextHolder.getLocale()));
            return "redirect:/index";
        }
    }

    public static String generatePassword() {
        String upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerChars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String allChars = upperChars + lowerChars + numbers;
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(allChars.length());
            password.append(allChars.charAt(index));
        }
        return password.toString();
    }
}
