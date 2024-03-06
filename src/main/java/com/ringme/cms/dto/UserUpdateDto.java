package com.ringme.cms.dto;


import com.ringme.cms.model.sys.User;
import com.ringme.cms.validationfield.Name;
import lombok.*;

import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class UserUpdateDto {
    private Long id;
    @Email
    private String email;
    @Name
    private String name;

//    private Long unit;

    private String type;

//    private String type_user;
    //    @Name
    private String username;
//    @Phone
    private String phone;
    private Long channelId;

    public User convertToEntity() {
        User user = new User();
        user.setId(this.id);
        if (this.email != null) {
            user.setEmail(this.email.trim().replaceAll("\s+", ""));
        }
        if (this.name != null) {
            user.setName(this.name.trim().replaceAll("\s+", " "));
        }
        if (this.phone != null) {
            user.setPhone(this.phone.trim().replaceAll("\s+", ""));
        }
        if (this.type != null) {
            user.setType(this.type.trim());
        }
//        if (this.type_user != null) {
//            user.setType_user(this.type_user.trim());
//        }
        if (username != null){
            user.setUsername(username);
        }
        if (channelId !=null){
            user.setChannelId(channelId);
        }
        return user;
    }
}
