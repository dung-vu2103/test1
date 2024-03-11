package com.ringme.cms.model.kakoakcms;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="min1")
public class Min {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "img")
    private String img;
}
