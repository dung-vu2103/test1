package com.ringme.cms.controller.sys;

import com.google.gson.Gson;

import com.ringme.cms.dto.MenuDto;
import com.ringme.cms.dto.UserSecurity;
import com.ringme.cms.model.sys.Menu;
import com.ringme.cms.repository.sys.MenuRepository;
import com.ringme.cms.service.sys.MenuService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@RestControllerAdvice
@Log4j2
public class ApiPrefixControllerAdvice implements WebMvcConfigurer {
    @Value("${server.servlet.context-path}")
    private String prefix;
    @Autowired
    MenuService menuService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private HttpSession session;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(prefix, c -> true);
    }

    @ModelAttribute("listMenu")
    public List<MenuDto> getMenuItems(Authentication authentication) {

        List<MenuDto> userMenus = new ArrayList<>();
        if (authentication != null && authentication.isAuthenticated()) {
            if (session.getAttribute("user-menu") != null) {
                return (List<MenuDto>) session.getAttribute("user-menu");
            }
            Locale localeContext = LocaleContextHolder.getLocale();
            UserSecurity userSecurity = (UserSecurity) authentication.getPrincipal();
            //AntPathMatcher antPathMatcher = new AntPathMatcher();
            Set<String> allowdRouter = userSecurity.getRouter();
            if (allowdRouter == null || allowdRouter.isEmpty()) {
                return new ArrayList<>();
            }
            List<Menu> firstLevel = menuService.getListMenuNoParent();


            for (Menu menu_1 : firstLevel) {
                List<Menu> secondLevelMenus = menuRepository.findByParentNameId(menu_1.getId());

                if (secondLevelMenus != null & secondLevelMenus.size() > 0) {
                    List<MenuDto> secondLevelFinal = new ArrayList<>();
                    for (Menu menu_2 : secondLevelMenus) {

                        List<Menu> thirdLevel = menuRepository.findByParentNameId(menu_2.getId());
                        if (thirdLevel != null & thirdLevel.size() > 0) {
                            List<MenuDto> thirdLevelFinal = new ArrayList<>();
                            for (Menu menu_3 : thirdLevel) {

                                if(allowAccess(menu_3.getRouter().getRouter_link(), allowdRouter)) {
                                    thirdLevelFinal.add(menu_3.convertToDto(localeContext.getLanguage()));
                                }
                            }
                            if(thirdLevelFinal.size() > 0) {
                                MenuDto menuDto = menu_2.convertToDto(localeContext.getLanguage());
                                menuDto.setLstChildMenus(thirdLevelFinal);
                                secondLevelFinal.add(menuDto);
                            }

                        } else {
                            if(allowAccess(menu_2.getRouter().getRouter_link(), allowdRouter)) {
                                MenuDto menuDto = menu_2.convertToDto(localeContext.getLanguage());
                                secondLevelFinal.add(menuDto);
                            }
                        }
                    }
                    if(secondLevelFinal.size() > 0) {
                        MenuDto menuDto = menu_1.convertToDto(localeContext.getLanguage());
                        menuDto.setLstChildMenus(secondLevelFinal);
                        userMenus.add(menuDto);
                    }
                } else {
                    if(allowAccess(menu_1.getRouter().getRouter_link(), allowdRouter)) {
                        MenuDto menuDto = menu_1.convertToDto(localeContext.getLanguage());
                        userMenus.add(menuDto);
                    }

                }
            }
            Gson gson = new Gson();
            log.debug("Allow link: {}", gson.toJson(allowdRouter));
            session.setAttribute("user-menu", userMenus);
            log.debug("After filter|" + gson.toJson(userMenus));
        }
        return userMenus;

    }

    private boolean allowAccess(String routerLink, Set<String> allowdRouter) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String userRouter: allowdRouter) {
            if(antPathMatcher.match(userRouter, routerLink)) {
                return  true;
            }
        }
        return false;
    }

//    private List<MenuDto> filter(List<MenuDto> rootMenuDto, Set<String> allowdRouter) {
//        AntPathMatcher antPathMatcher = new AntPathMatcher();
//        List<MenuDto> result = new ArrayList<>();
//        for (MenuDto menuItem : rootMenuDto) {
//            if (menuItem.getLstChildMenus() == null || menuItem.getLstChildMenus().size() == 0) {
//                for (String routerLink : allowdRouter) {
//                    if (antPathMatcher.match(routerLink, menuItem.getRouter().getRouter_link())) {
//                        log.debug("MATCHED: Menu: {} ==> Your Link: {}", menuItem.getRouter().getRouter_link(), routerLink);
//                        result.add(menuItem);
//                        break;
//                    }
//                }
//            } else {
//                menuItem.setLstChildMenus(filter(menuItem.getLstChildMenus(), allowdRouter));
//                result.add(menuItem);
//            }
//        }
//        return result;
//    }


//    @ModelAttribute("mapMenu")
//    public Map<String, List<Menu>> getMapMenu(Authentication authentication) {
//        if (authentication != null && authentication.isAuthenticated()) {
//            UserSecurity userSecurity = (UserSecurity) authentication.getPrincipal();
//            Set<String> routerLink = userSecurity.getRouter();
//
//            Map<String, List<Menu>> mapMenu = menuService.getMapMenuParent();
//            AntPathMatcher antPathMatcher = new AntPathMatcher();
//
//            List<String> keysToRemove = new ArrayList<>();
//
//
//            for (Map.Entry<String, List<Menu>> entry : mapMenu.entrySet()) {
//                List<Menu> subList = entry.getValue();
//                Iterator<Menu> iterator = subList.iterator();
//                while (iterator.hasNext()) {
//                    Menu m = iterator.next();
//                    boolean check = false;
//                    for (String str : routerLink) {
//                        boolean isMatched = antPathMatcher.match(str, m.getRouter().getRouter_link());
//                        if (isMatched) {
//                            check = true;
//                        }
//                    }
//                    if (!check) {
//                        iterator.remove();
//                    }
//                }
//                if (subList.isEmpty()) {
//                    keysToRemove.add(entry.getKey());
//                }
//                // Xóa các key trong danh sách xóa khỏi map
//                for (String key : keysToRemove) {
//                    mapMenu.remove(key);
//                }
//            }
//            return mapMenu;
//        } else {
//            return null;
//        }
//    }

//    @ModelAttribute("open")
//    public boolean openResetPass() {
//        return false;
//    }

}

