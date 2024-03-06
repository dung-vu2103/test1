package com.ringme.cms.model.kakoak.game;

import com.ringme.cms.model.sys.EntityBaseKakoak;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "game_html5")
@EntityListeners(AuditingEntityListener.class)
public class GameHtml5 extends EntityBaseKakoak implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String link;

    @Column
    private String description;

    @Column
    private Integer recommend;

    @Column
    private String banner;

    @Column
    private String icon;

    @Column
    private Integer visible;

    @Column(name = "`order`")
    private String order;

    @Column
    private String font;

    @OneToMany(mappedBy = "gameHtml5")
    private List<GameStat> gameStats;
}
