package com.ringme.cms.repository.kakoak.game;

import com.ringme.cms.dto.kakoak.game.GameStatDto;
import com.ringme.cms.model.kakoak.game.GameStat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GameStatRepository extends JpaRepository<GameStat, Long> {

    @Query(value = "SELECT * FROM game_stat " +
            "WHERE (:gameId is null or game_id LIKE CONCAT('%', :gameId, '%')) " +
            "AND (:startTime IS NULL OR :endTime IS NULL OR `date` BETWEEN :startTime AND :endTime) " +
            "ORDER BY `date` DESC",
            countQuery = "SELECT count(*) FROM game_stat " +
                    "WHERE (:gameId is null or game_id LIKE CONCAT('%', :gameId, '%')) " +
                    "AND (:startTime IS NULL OR :endTime IS NULL OR `date` BETWEEN :startTime AND :endTime) " +
                    "ORDER BY `date` DESC",
            nativeQuery = true)
    Page<GameStat> getList(@Param("gameId") Long gameId,
                          @Param("startTime") Date startTime,
                          @Param("endTime") Date endTime,
                          Pageable pageable);

    @Query(value = "SELECT * FROM game_stat " +
            "WHERE (:startTime IS NULL OR :endTime IS NULL OR `date` BETWEEN :startTime AND :endTime) " +
            "AND (:gameId is null or game_id LIKE CONCAT('%', :gameId, '%')) " +
            "ORDER BY created_at DESC", nativeQuery = true)
    List<GameStat> getDataExportExcel(@Param("gameId") Long gameId,
                                         @Param("startTime") String timeStart,
                                         @Param("endTime") String timeEnd);
}
