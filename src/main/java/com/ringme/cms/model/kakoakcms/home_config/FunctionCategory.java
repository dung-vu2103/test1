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
@Table(name = "function_category")
@EntityListeners(AuditingEntityListener.class)
public class FunctionCategory extends EntityBase implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "icon")
    private String icon;
    @Column(name = "title")
    private String title;
    @Column(name = "type")
    private Integer type;
    @Column(name = "enable")
    private Integer enable;
    @Column(name = "deeplink")
    private String deeplink;
    @Column(name = "priority")
    private Integer priority;
}