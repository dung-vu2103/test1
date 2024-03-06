package com.ringme.cms.repository.kakoakcms.sticker;

import com.ringme.cms.model.kakoakcms.sticker.Sticker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StickerRepository extends JpaRepository<Sticker, Long> {
    @Query(value = """
            SELECT * FROM sticker where (:name is null or name LIKE CONCAT('%', :name, '%'))
            ORDER BY created_date DESC
            """,
            countQuery = """
                    SELECT count(*) FROM sticker where (:name is null or name LIKE CONCAT('%', :name, '%'))
                    ORDER BY created_date DESC
                    """,
            nativeQuery = true)
    Page<Sticker> get(@Param("name") String name, Pageable pageable);
}
