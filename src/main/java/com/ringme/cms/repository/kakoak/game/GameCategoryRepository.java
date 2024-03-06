package com.ringme.cms.repository.kakoak.game;

import com.ringme.cms.model.kakoak.game.GameCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameCategoryRepository extends JpaRepository<GameCategory, Long> {
    @Query(value = "SELECT * FROM game_category " +
            "WHERE (:gameCateName is null or name LIKE CONCAT('%', :gameCateName, '%')) " +
            "AND status != -1 " +
            "ORDER BY created_date DESC",
            countQuery = "SELECT count(*) FROM game_category " +
                    "WHERE (:gameCateName is null or name LIKE CONCAT('%', :gameCateName, '%')) " +
                    "AND status != -1 " +
                    "ORDER BY created_date DESC",
            nativeQuery = true)
    Page<GameCategory> getList(@Param("gameCateName") String gameCateName,
                                     Pageable pageable);

    @Modifying
    @Query(value = "UPDATE game_category SET status = -1 WHERE id = :id", nativeQuery = true)
    void deletegameCate(@Param("id") Long id);
}
