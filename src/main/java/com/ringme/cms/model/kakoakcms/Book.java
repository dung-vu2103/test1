package com.ringme.cms.model.kakoakcms;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "book1")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "book_name")
    private String book_name;
    @Column(name = "price")
    private float price;
    @Column(name = "date")
    private Date createDate;
    // @ManyToOne()
    //@JoinColumn(name = "user1_id")
    //private User1 user1;
}
