package com.ringme.cms.model.sys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class EntityBaseKakoak  implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @CreatedDate
    @Temporal(TemporalType.DATE)
    @Column(name = "created_date", nullable = false, updatable = false, columnDefinition = "date DEFAULT CURRENT_TIMESTAMP")
    private Date createdDate;

    @LastModifiedDate
    @Temporal(TemporalType.DATE)
    @Column(name = "uploaded_date", columnDefinition = "date DEFAULT CURRENT_TIMESTAMP")
    private Date uploadedDate;
}
