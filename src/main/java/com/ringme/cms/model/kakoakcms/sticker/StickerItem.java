package com.ringme.cms.model.kakoakcms.sticker;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "sticker_item")
@EntityListeners(AuditingEntityListener.class)
public class StickerItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "sticker_id")
    private Long stickerId;
    @Column(name = "iorder")
    private Integer iorder;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "voice_url")
    private String voiceUrl;
    @Column(name = "active")
    private Integer active;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "modified_date")
    private Date modifiedDate;
//    @ManyToOne
//    @JoinColumn(name = "sticker_id", referencedColumnName = "id", insertable = false, updatable = false)
//    private Sticker sticker;
}