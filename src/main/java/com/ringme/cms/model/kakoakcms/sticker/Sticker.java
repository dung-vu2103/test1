package com.ringme.cms.model.kakoakcms.sticker;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "sticker")
@EntityListeners(AuditingEntityListener.class)
public class Sticker implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "iorder")
    private Integer iorder;
    @Column(name = "prefix")
    private String prefix;
    @Column(name = "item_total")
    private Integer itemTotal;
    @Column(name = "sticker_url")
    private String stickerUrl;
    @Column(name = "icon_url")
    private String iconUrl;
    @Column(name = "preview_url")
    private String previewUrl;
    @Column(name = "active")
    private Integer active;
    @Column(name = "is_new")
    private Integer isNew;
    @Column(name = "sticky")
    private Integer sticky;
    @Column(name = "created_date")
    private String createdDate;
    @Column(name = "modified")
    private String modified;
    @Column(name = "modified_item_date")
    private String modifiedItemDate;
//    @OneToMany(mappedBy = "sticker")
//    private List<StickerItem> stickerItems;
}