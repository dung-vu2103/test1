package com.ringme.cms.model.sys;


import com.ringme.cms.dto.UserDto;
import com.ringme.cms.dto.UserUpdateDto;
import com.ringme.cms.model.kakoakcms.customer.OfficialUser;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tbl_user")
public class User extends EntityBase implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;

    @Column(name = "fullname")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "type")
    private String type;    // 0 : admin , 1 : user

    @Column(name = "active")
    private boolean active;

    @Column(name = "channel_id")
    private Long channelId;

    @Column(name = "time_zone")
    private String timeZone;

    @OneToMany(mappedBy = "user")
    private List<OfficialUser> officialUsers;

    public UserDto convertToDto() {
        UserDto userDto = new UserDto();
        userDto.setId(this.id);
        userDto.setEmail(this.email);
        userDto.setName(this.name);
        userDto.setPhone(this.phone);


        if (type.equals("0")) {
            userDto.setType("Admin");
        } else if (type.equals("1")) {
            userDto.setType("User");
        }
        return userDto;
    }

    public UserUpdateDto convertToDtoUpdate() {
        UserUpdateDto userDto = new UserUpdateDto();
        userDto.setId(this.id);
        userDto.setEmail(this.email);
        userDto.setName(this.name);
        userDto.setPhone(this.phone);
        userDto.setType(this.type);
        userDto.setUsername(this.username);
        userDto.setChannelId(this.channelId);
        return userDto;
    }
}
