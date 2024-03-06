package com.ringme.cms.model.kakoakcms.customer;

import com.ringme.cms.model.sys.EntityBase;
import com.ringme.cms.model.sys.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "official_user_v2")
@EntityListeners(AuditingEntityListener.class)
public class OfficialUser extends EntityBase implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_official")
    private Long idOfficial;

    @Column(name = "id_user")
    private Long idUser;

    @ManyToOne
    @JoinColumn(name = "id_official", referencedColumnName = "id", insertable = false, updatable = false)
    private OfficialAccount officialAccount;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
}
