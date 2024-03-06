package com.ringme.cms.repository.kakoakcms.sticker;

import com.ringme.cms.model.kakoakcms.sticker.StickerItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StickerItemRepository extends JpaRepository<StickerItem, Long> {
    @Query(value = """
            SELECT * FROM sticker_item where sticker_id = :sticker_id
            ORDER BY iorder
            """,
            countQuery = """
                    SELECT count(*) FROM sticker_item where sticker_id = :sticker_id
                    ORDER BY iorder
                    """,
            nativeQuery = true)
    Page<StickerItem> get(@Param("sticker_id") long stickerId, Pageable pageable);
}
