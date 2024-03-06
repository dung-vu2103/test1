package com.ringme.cms.model.kakoak.game;

import com.ringme.cms.model.sys.EntityBase;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "game_stat")
@EntityListeners(AuditingEntityListener.class)
public class GameStat extends EntityBase implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long id;

    @Column(name = "game_id", columnDefinition = "int NOT NULL")
    private Integer gameId;

    @LastModifiedDate
    @Temporal(TemporalType.DATE)
    @Column(name = "`date`", columnDefinition = "date NOT NULL")
    private Date date;

    @Column(name = "getScore", columnDefinition = "int NOT NULL DEFAULT 0")
    private Integer getScore;

    @Column(name = "submit", columnDefinition = "int NOT NULL DEFAULT 0")
    private Integer submit;

    @Column(name = "user_number", columnDefinition = "int NOT NULL DEFAULT 0")
    private Integer userNumber;

    @ManyToOne
    @JoinColumn(name = "game_id", referencedColumnName = "id", insertable = false, updatable = false)
    private GameHtml5 gameHtml5;
}
