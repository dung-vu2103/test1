package com.ringme.cms.dto.kakoakcms.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
public class User1 {
    private Integer id;
    private String name;
    private String name1;
    private Integer age;
    private String email;
    private String address;
    private String phone;
    private String image;
    //@OneToMany(mappedBy = "user1",cascade = CascadeType.ALL)
    //private List<Book> books;
}
