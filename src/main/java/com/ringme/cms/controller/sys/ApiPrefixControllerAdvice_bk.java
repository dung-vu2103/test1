package com.ringme.cms.controller.sys;

import com.ringme.cms.repository.sys.MenuRepository;
import com.ringme.cms.service.sys.MenuService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RestControllerAdvice
@Log4j2
public class ApiPrefixControllerAdvice_bk implements WebMvcConfigurer {
    @Value("${server.servlet.context-path}")
    private String prefix;
    @Autowired
    MenuService menuService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MenuRepository menuRepository;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(prefix, c -> true);
    }

//    @ModelAttribute("listMenu")
//    public List<MenuDto> getMenuItems(Authentication authentication) {
//        if (authentication != null && authentication.isAuthenticated()) {
//            Locale localeContext = LocaleContextHolder.getLocale();
//            UserSecurity userSecurity = (UserSecurity) authentication.getPrincipal();
//            AntPathMatcher antPathMatcher = new AntPathMatcher();
//            Set<String> routerLink = userSecurity.getRouter();
//            List<Menu> menus = menuService.getListMenuNoParent();
//            List<MenuDto> menuDtos = new ArrayList<>();
//            for (Menu m : menus){
//                List<Menu> menuChildDb = menuRepository.findByParentNameId(m.getId());
//                if (!menuChildDb.isEmpty()){
//                    List<MenuDto> menuChild = new ArrayList<>();
//                    for (Menu menu:menuChildDb){
//                        boolean check = false;
//                        if (routerLink!=null){
//                            for (String str : routerLink) {
//                                boolean isMatched = antPathMatcher.match(str, menu.getRouter().getRouter_link());
//                                if (isMatched) {
//                                    check = true;
//                                }
//                            }
//                            if (check) {
//                                menuChild.add(menu.convertToDto(localeContext.getLanguage()));
//                            }
//                        }
//                    }
//                    if (!menuChild.isEmpty()){
//                        MenuDto menuDto = m.convertToDto(localeContext.getLanguage());
//                        menuDto.setParentName(menuChild);
//                        menuDtos.add(menuDto);
//                    }
//                }
//                else {
//                    boolean check = false;
//                    if (routerLink!=null){
//                        for (String str : routerLink) {
//                            boolean isMatched = antPathMatcher.match(str, m.getRouter().getRouter_link());
//                            if (isMatched) {
//                                check = true;
//                            }
//                        }
//                    }
//                    if (check) {
//                        MenuDto menuDto = m.convertToDto(localeContext.getLanguage());
//                        menuDtos.add(menuDto);
//                    }
//                }
//            }
////            while (menuIterator.hasNext()) {
////                Menu m = menuIterator.next();
////                boolean check = false;
////                for (String str : routerLink) {
////                    boolean isMatched = antPathMatcher.match(str, m.getRouter().getRouter_link());
////                    if (isMatched) {
////                        check = true;
////                    }
////                }
////                if (!check) {
////                    menuIterator.remove();
////                }
////            }
//            return menuDtos;
//        } else {
//            return null;
//        }
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

