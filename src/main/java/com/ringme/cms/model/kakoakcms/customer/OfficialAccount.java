package com.ringme.cms.model.kakoakcms.customer;

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
@Table(name = "official_account")
@EntityListeners(AuditingEntityListener.class)
public class OfficialAccount extends EntityBase implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "official_account_id")
    private String officialAccountId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "status")
    private Integer status;

    @OneToMany(mappedBy = "officialAccount")
    private List<OfficialUser> officialUsers;
}
