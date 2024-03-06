package com.ringme.cms.model.kakoakcms.home_config;

import com.ringme.cms.model.sys.EntityBase;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@Table(name = "home_category")
@EntityListeners(AuditingEntityListener.class)
public class HomeCategory extends EntityBase implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "category")
    private String category;
    @Column(name = "priority")
    private Integer priority;
}