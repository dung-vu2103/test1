package com.ringme.cms.repository.kakoakcms.game;

import com.ringme.cms.model.kakoakcms.game.IconPrize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IconPrizeRepository extends JpaRepository<IconPrize, Long> {
    @Query(value = "SELECT * FROM icon_prize " +
            "WHERE (:iconName is null or name LIKE CONCAT('%', :iconName, '%')) " +
            "AND (:iconCode is null or code LIKE CONCAT('%', :iconCode, '%')) " +
            "AND (:iconActive is null or active LIKE CONCAT('%', :iconActive, '%')) " +
            "AND (:iconType is null or type LIKE CONCAT('%', :iconType, '%')) " +
            "AND active != -1 " +
            "ORDER BY created_at DESC",
            countQuery = "SELECT count(*) FROM icon_prize " +
                    "WHERE (:iconName is null or name LIKE CONCAT('%', :iconName, '%')) " +
                    "AND (:iconCode is null or code LIKE CONCAT('%', :iconCode, '%')) " +
                    "AND (:iconActive is null or active LIKE CONCAT('%', :iconActive, '%')) " +
                    "AND (:iconType is null or type LIKE CONCAT('%', :iconType, '%')) " +
                    "AND active != -1 " +
                    "ORDER BY created_at DESC",
            nativeQuery = true)
    Page<IconPrize> getListIconPrize(@Param("iconName") String iconName,
                                     @Param("iconCode") String iconCode,
                                     @Param("iconActive") Integer iconActive,
                                     @Param("iconType") String iconType,
                                     Pageable pageable);

    @Modifying
    @Query(value = "UPDATE icon_prize SET active = -1 WHERE id = :id", nativeQuery = true)
    void deleteIcon(@Param("id") Long id);

    @Modifying
    @Query(value = "update icon_prize set active = 1 where id = :id", nativeQuery = true)
    void active(@Param("id") Long id);

    @Modifying
    @Query(value = "update icon_prize set active = 0 where id = :id", nativeQuery = true)
    void block(@Param("id") Long id);

    @Query(value = "SELECT count(*) FROM icon_prize WHERE active = 1 ", nativeQuery = true)
    Integer countIconPrizeActive();
}
