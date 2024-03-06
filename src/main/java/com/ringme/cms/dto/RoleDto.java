package com.ringme.cms.dto;

import com.ringme.cms.model.sys.Role;
import com.ringme.cms.validationfield.Name;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class RoleDto {
    private Long id;
    @Name
    private String roleName;

    private String description;

    public Role convertToEntity() {
        Role role = new Role();
        if (this.roleName != null) {
            role.setRoleName(this.roleName.trim().replaceAll("\s+", " "));
        }
        if (this.description != null) {
            role.setDescription(this.description.trim().replaceAll("\s+", " "));
        }
        return role;
    }
}
