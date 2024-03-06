package com.ringme.cms.model.sys;

import com.ringme.cms.dto.RoleDto;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tbl_role")
public class Role extends EntityBase implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "role_name", unique = true)
    @NotNull(message = "Không được để trống tên quyền")
    private String roleName;
    private String description;

    public RoleDto convertToDto() {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(this.id);
        String name = this.roleName;
        roleDto.setRoleName(name.replace("ROLE_", ""));
        roleDto.setDescription(this.description);
        return roleDto;
    }
}
