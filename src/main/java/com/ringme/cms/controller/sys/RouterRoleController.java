package com.ringme.cms.controller.sys;

import com.ringme.cms.dto.RoleDto;
import com.ringme.cms.model.sys.Role;
import com.ringme.cms.model.sys.Router;
import com.ringme.cms.model.sys.RouterRole;
import com.ringme.cms.service.sys.RoleRouterService;
import com.ringme.cms.service.sys.RoleService;
import com.ringme.cms.service.sys.RouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sys")
public class RouterRoleController {
    @Autowired
    RoleRouterService roleRouterService;
    @Autowired
    RouterService routerService;
    @Autowired
    RoleService roleService;

    @GetMapping("/role/view/{id}")
    public String addRouterToRole(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Role> role = roleService.findRoleById(id);
        if (role.isPresent()) {
            List<RouterRole> routerRoles = roleRouterService.findAllRouterRoleByRoleId(id);
            List<Long> listId = routerRoles.stream().map(e -> e.getRouter().getId()).collect(Collectors.toList());
            List<Router> routers;
            if (!listId.isEmpty()) {
                routers = routerService.findAllRouterNotInRole(listId);
            } else {
                routers = routerService.findAllRouterActive();
            }
            RoleDto roleDto = role.get().convertToDto();
            model.addAttribute("role", roleDto);
            model.addAttribute("routers", routers);
            model.addAttribute("routerRoles", routerRoles);
            return "sys/role-router";
        } else {
            redirectAttributes.addFlashAttribute("error", "Role không còn tồn tại!");
            return "redirect:/role/index";
        }
    }

    @PostMapping("/router-role/delete")
    public String deleteRouterRole(@RequestParam("id") Long id, @RequestParam(name = "router2", required = false) List<Long> idRouterRole, RedirectAttributes redirectAttributes) {
        if (idRouterRole == null || idRouterRole.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn 1 đường dẫn!");
            return "redirect:/role/view/" + id;
        }
        if (!idRouterRole.isEmpty()) {
            try {
                roleRouterService.deleteRoleRouter(idRouterRole);
                redirectAttributes.addFlashAttribute("success", "Success!");
                return "redirect:/role/view/" + id;
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Error!");
                return "redirect:/role/view/" + id;
            }
        }
        redirectAttributes.addFlashAttribute("error", "Error!");
        return "redirect:/role/view/" + id;
    }

    @PostMapping("/router-role/create")
    public String createRouterRole(@RequestParam("id") Long id, @RequestParam(name = "router1", required = false) List<Long> idRouterRole, RedirectAttributes redirectAttributes) {
        if (idRouterRole == null || idRouterRole.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn 1 đường dẫn!");
            return "redirect:/role/view/" + id;
        }
        if (!idRouterRole.isEmpty()) {
            try {
                roleRouterService.createRoleRouter(id, idRouterRole);
                redirectAttributes.addFlashAttribute("success", "Thành công!");
                return "redirect:/role/view/" + id;
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Xảy ra lỗi!");
                return "redirect:/role/view/" + id;
            }
        }
        redirectAttributes.addFlashAttribute("error", "Đường dẫn không còn tồn tại!");
        return "redirect:/role/view/" + id;
    }
}
