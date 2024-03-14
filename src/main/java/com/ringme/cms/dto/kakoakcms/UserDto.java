package com.ringme.cms.dto.kakoakcms;

import lombok.*;

import javax.persistence.Column;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String name;
    private Integer age;
    private String email;
    private String address;
    private String phone;
    private String image;

}
