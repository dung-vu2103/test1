package com.ringme.cms.model.kakoakcms.marketing;

import com.ringme.cms.model.kakoakcms.notification.NotiEntity;
import com.ringme.cms.model.sys.EntityBase;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "deeplink_configure")
@EntityListeners(AuditingEntityListener.class)
public class DeeplinkConfigure  extends EntityBase implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "deeplink")
    private String deeplink;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "description")
    private String description;

    @Column(name = "deeplink_params")
    private String deeplinkParams;

    @Column(name = "type")
    private String type;

//    @OneToMany(mappedBy = "deeplinkConfigure")
//    private List<NotiEntity> notiEntities;
}
