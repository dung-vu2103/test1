package com.ringme.cms.controller.sys;

import com.ringme.cms.dto.RoleDto;
import com.ringme.cms.model.sys.Role;
import com.ringme.cms.service.sys.RoleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@Log4j2
@RequestMapping("/sys")
public class RoleController {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    RoleService roleService;

    @GetMapping("/role/index")
    public String loadAllRole(Model model) {
        return pageRole(1, model);
    }

    @GetMapping("/role/index/{page}")
    public String pageRole(@PathVariable("page") Integer page, Model model) {
        Page<Role> roles = roleService.pageRole(page, 10);
        List<RoleDto> roleDtos = roles.stream().map(Role::convertToDto).toList();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", roles.getTotalPages());
        model.addAttribute("totalItems", roles.getTotalElements());
        model.addAttribute("roles", roleDtos);
        return "sys/role";
    }

    @GetMapping("/role/create")
    public String createRole(Model model) {
        model.addAttribute("role", new Role());
        return "sys/role-create";
    }

    @GetMapping("/role/update/{id}")
    public String updateRole(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Role> role = roleService.findRoleById(id);
        if (role.isPresent()) {
            String updateRole = "updateRole";
            role.get().setRoleName(role.get().getRoleName().split("ROLE_")[1]);
            model.addAttribute("role", role.get());
            model.addAttribute("updateRole", updateRole);
            return "sys/role-create";
        } else {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("role.noExist", null, LocaleContextHolder.getLocale()));
            return "redirect:/sys/role/index";
        }
    }

    @PostMapping("/role/update")
    public String saveRole(@Valid @ModelAttribute("role") Role role, BindingResult bindingResult, Errors error, RedirectAttributes redirectAttributes, Model model) {
        if (error.hasErrors()) {
            return "sys/role-create";
        } else {
            try {
                role.setRoleName(role.getRoleName().trim());
                role.setDescription(role.getDescription().trim().replace("\s+", " "));
                if (role.getId() != null) {
                    Optional<Role> roleCheck = roleService.findByRoleName(role.getRoleName());
                    if (roleCheck.isPresent() && !roleCheck.get().getId().equals(role.getId())) {
                        redirectAttributes.addFlashAttribute("error", "");
                        return "redirect:/sys/role/index";
                    }
                } else {
                    Optional<Role> roleCheck = roleService.findByRoleName(role.getRoleName().trim());
                    if (roleCheck.isPresent()) {
                        redirectAttributes.addFlashAttribute("error", messageSource.getMessage("role.exist", null, LocaleContextHolder.getLocale()));
                        return "redirect:/sys/role/index";
                    }
                }
                roleService.saveRole(role);
                if (role.getId() != null) {
                    redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success",
                            null, LocaleContextHolder.getLocale()));
                } else redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success",
                        null, LocaleContextHolder.getLocale()));

                return "redirect:/sys/role/index";
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("role.exist",
                        null, LocaleContextHolder.getLocale()));
                return "redirect:/sys/role/index";
            }
        }
    }

    @GetMapping("/role/delete/{id}")
    public String deleteRole(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            roleService.deleteRole(id);
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.delete.success",
                    null, LocaleContextHolder.getLocale()));
            return "redirect:/sys/role/index";
        } catch (Exception e) {
            log.error("Delete role error " + e.getMessage() + " " + System.currentTimeMillis());
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("role.error",
                    null, LocaleContextHolder.getLocale()));
            return "redirect:/sys/role/index";
        }
    }
}
