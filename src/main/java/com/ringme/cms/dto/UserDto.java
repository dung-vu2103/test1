package com.ringme.cms.dto;

import com.ringme.cms.model.sys.User;
import com.ringme.cms.validationfield.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class UserDto {
    private Long id;
    @Email
    private String email;
    private String username;
    @Name
    private String name;

    @Password
    private String password;

    @Password
    private String password2;

//    private Long unit;

    private String type;

    //    @NotNullCus
//    private String type_user;
    private Long channelId;

    //    @Phone
    private String phone;

    public User convertToEntity() {
        User user = new User();
        user.setId(this.id);
        if (this.email != null) {
            user.setEmail(this.email.trim().replaceAll("\s+", " "));
        }
        if (this.name != null) {
            user.setName(this.name.trim().replaceAll("\s+", " "));
        }
        if (this.phone != null) {
            user.setPhone(this.phone.trim().replaceAll("\s+", " "));
        }
        if (this.type != null) {
            user.setType(this.type.trim());
        }
//        if (this.type_user != null) {
//            user.setType_user(this.type_user.trim());
//        }
        if (this.password != null) {
            user.setPassword(this.password.trim());
        }
        if (this.username !=null  && this.id ==null){
            user.setUsername(this.username);
        }
        if (channelId !=null){
            user.setChannelId(channelId);
        }
        return user;
    }
}
