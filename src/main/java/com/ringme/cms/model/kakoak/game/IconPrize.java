//package com.ringme.cms.model.kakoak.game;
//
//import com.ringme.cms.model.sys.EntityBase;
//import lombok.Data;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.sql.Timestamp;
//import java.util.Date;
//
//@Entity
//@Getter
//@Setter
//@Table(name = "icon_prize")
//@EntityListeners(AuditingEntityListener.class)
//public class IconPrize extends EntityBase implements Serializable {
//    private static final long serialVersionUID = -297553281792804396L;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column
//    private String code;
//
//    @Column
//    private String name;
//
//    @Column
//    private String type;
//
//    @Column
//    private String link_icon;
//
//    @Column
//    private Integer active;
//
//    @Column
//    private Long value;
//}
