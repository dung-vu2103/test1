package com.ringme.cms.repository.kakoak.game;

import com.ringme.cms.model.kakoak.game.GameHtml5;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameHtml5Repository extends JpaRepository<GameHtml5, Long> {
    @Query(value = "SELECT * FROM game_html5 " +
            "WHERE (:gameHtml5Name is null or name LIKE CONCAT('%', :gameHtml5Name, '%')) " +
            "AND (:visible is null or visible LIKE CONCAT('%', :visible, '%')) " +
            "AND (:font is null or font LIKE CONCAT('%', :font, '%')) " +
            "AND (:order is null or `order` LIKE CONCAT('%', :order, '%')) " +
            "ORDER BY created_date DESC",
            countQuery = "SELECT count(*) FROM game_html5 " +
                    "WHERE (:gameHtml5Name is null or name LIKE CONCAT('%', :gameHtml5Name, '%')) " +
                    "AND (:visible is null or visible LIKE CONCAT('%', :visible, '%')) " +
                    "AND (:font is null or font LIKE CONCAT('%', :font, '%')) " +
                    "AND (:order is null or `order` LIKE CONCAT('%', :order, '%')) " +
                    "ORDER BY created_date DESC",
            nativeQuery = true)
    Page<GameHtml5> getList(@Param("gameHtml5Name") String gameHtml5Name,
                            @Param("visible") Integer visible,
                            @Param("font") String font,
                            @Param("order") Integer order,
                            Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM game_html5 WHERE id = :id", nativeQuery = true)
    void deleteGameHtml5(@Param("id") Long id);

    @Query(value = "SELECT name FROM game_html5 WHERE id = :id", nativeQuery = true)
    String getGameName(@Param("id") Long id);

    @Query(value = "SELECT id AS `id`, `name` AS `text` FROM game_html5 " +
            "WHERE (:input is null or (id = :input OR `name` LIKE CONCAT('%', :input, '%'))) ORDER BY `name` DESC", nativeQuery = true)
    List<String[]> ajaxSearchGameId(@Param("input") String input);
}