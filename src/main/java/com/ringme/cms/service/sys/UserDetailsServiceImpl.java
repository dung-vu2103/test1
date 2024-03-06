package com.ringme.cms.service.sys;

import com.ringme.cms.dto.UserSecurity;
import com.ringme.cms.model.sys.Menu;
import com.ringme.cms.model.sys.User;
import com.ringme.cms.model.sys.UserRole;
import com.ringme.cms.repository.sys.UserRepository;
import com.ringme.cms.repository.sys.UserRoleRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    RoleRouterService roleRouterService;
    @Autowired
    MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("log loadUserByUsername impl");
        Optional<User> user = userRepository.findUserByUserName(username);
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if (user.isPresent()) {

            log.info("log loadUserByUsername " + user.get().getEmail() + "  " + user.get().getPassword());
            List<UserRole> userRoles = userRoleRepository.findUserRoleByUserId(user.get().getId());

            List<GrantedAuthority> grandList = userRoles.stream().map((e) -> {
                return new SimpleGrantedAuthority(e.getRole().getRoleName());
            }).collect(Collectors.toList());

            List<Long> roleIds = userRoles.stream().map(e -> e.getRole().getId()).collect(Collectors.toList());
            Set<String> routerLink = roleRouterService.findAllRouterRoleByListRoleId(roleIds).stream()
                    .map(e -> e.getRouter().getRouter_link()).collect(Collectors.toSet());
            UserSecurity useSercurity = new UserSecurity(user.get().getUsername(), user.get().getPassword(), grandList, user.get().getId(), user.get().getName());
            useSercurity.setRouter(routerLink);
            useSercurity.setEmail(user.get().getEmail());
//            List<Menu> menus = menuService.getListMenuNoParent();
//            Iterator<Menu> menuIterator = menus.iterator();
//            List<String> keysToRemove = new ArrayList<>();
//            while (menuIterator.hasNext()) {
//                Menu m = menuIterator.next();
//                boolean check = false;
//                for (String str : routerLink) {
//                    boolean isMatched = antPathMatcher.match(str, m.getRouter().getRouter_link());
//                    if (isMatched) {
//                        check = true;
//                    }
//                }
//                if (!check) {
//                    menuIterator.remove();
//                }
//            }
//            useSercurity.setMenus(menus);
            return useSercurity;
        } else {
            throw new UsernameNotFoundException("User " + username + " was not found in database");
        }
    }
}
