package com.ringme.cms.dto;


import com.ringme.cms.model.sys.Menu;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@ToString
public class UserSecurity extends User {
    private Long id;

    private Long unit;

    private String phone;

    private String name;

    private String email;
    private Set<String> router;

    private List<Menu> menus;

    private Map<String, List<Menu>> mapMenu;

    // TODO to be Continue
    public UserSecurity(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, String name) {
        super(username, password, authorities);
        this.id = id;
        this.name = name;
    }

    public UserSecurity(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public UserSecurity(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, String phone, String name) {
        super(username, password, authorities);
        this.id = id;
        this.phone = phone;
        this.name = name;
    }
}
