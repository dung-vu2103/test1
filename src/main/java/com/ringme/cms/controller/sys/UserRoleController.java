package com.ringme.cms.controller.sys;

import com.ringme.cms.model.sys.Role;
import com.ringme.cms.model.sys.User;
import com.ringme.cms.model.sys.UserRole;
import com.ringme.cms.repository.sys.RoleRepository;
import com.ringme.cms.service.sys.RoleService;
import com.ringme.cms.service.sys.UserRoleService;
import com.ringme.cms.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sys")
public class UserRoleController {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    RoleService roleService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserController userController;

    @GetMapping("user/view/{id}")
    public String getUserRoleById(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        Optional<User> user = userService.findByIdUser(id);
        if (user.isPresent()) {
            List<UserRole> roles = userRoleService.findAllUserRoleByIdUser(id);
            if (roles.isEmpty()) {
                List<Role> roleList = roleService.findAllRole();
                model.addAttribute("roles", roleList);
            } else {
                List<Long> idRole = roles.stream().map(e -> e.getRole().getId()).collect(Collectors.toList());
                List<Role> roleList = roleService.findAllRoleNotInListIdRole(idRole);
                model.addAttribute("roles", roleList);
            }
            model.addAttribute("user", user.get());
            model.addAttribute("user_roles", roles);
            return "sys/user-role";
        }
        attributes.addFlashAttribute("error", messageSource.getMessage("userrole.userNo",
                null, LocaleContextHolder.getLocale()));
        return "redirect:/user/index";
    }

    @PostMapping("/user-role/create")
    public String updateUserRole(@RequestParam("id") Long id, @RequestParam(name = "role1", required = false) List<Long> roles, Model model, RedirectAttributes redirectAttributes) {
        if (roles == null) {
            model.addAttribute("error", messageSource.getMessage("userrole.choose",
                    null, LocaleContextHolder.getLocale()));
            return getUserRoleById(id, model, redirectAttributes);
        }
        if (roles.isEmpty()) {
            model.addAttribute("error", messageSource.getMessage("userrole.choose",
                    null, LocaleContextHolder.getLocale()));
            return getUserRoleById(id, model, redirectAttributes);
        }
        Optional<User> user = userService.findByIdUser(id);
        if (user.isPresent()) {
            List<Role> rolePresent = roleRepository.findAllById(roles);
            List<UserRole> userRoles = rolePresent.stream().map((e) -> {
                return new UserRole(user.get(), e);
            }).collect(Collectors.toList());
            try {
                userRoleService.saveAllUserRole(userRoles);
            } catch (Exception e) {
                model.addAttribute("error", messageSource.getMessage("userrole.update.error",
                        null, LocaleContextHolder.getLocale()));
                return getUserRoleById(id, model, redirectAttributes);
            }
            model.addAttribute("success", messageSource.getMessage("title.update.success",
                    null, LocaleContextHolder.getLocale()));
            return getUserRoleById(id, model, redirectAttributes);
        }
        model.addAttribute("error", messageSource.getMessage("userrole.userNo",
                null, LocaleContextHolder.getLocale()));
        return getUserRoleById(id, model, redirectAttributes);

    }

    @PostMapping("/user-role/delete")
    public String deleteUserRole(@RequestParam("id") Long id, @RequestParam(name = "role2", required = false) List<Long> userroles, Model model, RedirectAttributes redirectAttributes) {
        if (userroles == null) {
            model.addAttribute("error", messageSource.getMessage("userrole.choose.rm",
                    null, LocaleContextHolder.getLocale()));
            return getUserRoleById(id, model, redirectAttributes);
        }
        if (userroles.isEmpty()) {
            model.addAttribute("error", messageSource.getMessage("userrole.choose.rm",
                    null, LocaleContextHolder.getLocale()));
            return getUserRoleById(id, model, redirectAttributes);
        }
        Optional<User> user = userService.findByIdUser(id);
        if (user.isPresent()) {
            try {
                userRoleService.deleteUserRoleById(userroles);
                model.addAttribute("success", messageSource.getMessage("title.delete.success",
                        null, LocaleContextHolder.getLocale()));
                return getUserRoleById(id, model, redirectAttributes);
            } catch (Exception e) {
                model.addAttribute("error", messageSource.getMessage("title.delete.faile",
                        null, LocaleContextHolder.getLocale()));
                return getUserRoleById(id, model, redirectAttributes);
            }
        } else {
            model.addAttribute("error", messageSource.getMessage("userrole.userNo",
                    null, LocaleContextHolder.getLocale()));
            return getUserRoleById(id, model, redirectAttributes);
        }
    }

}
