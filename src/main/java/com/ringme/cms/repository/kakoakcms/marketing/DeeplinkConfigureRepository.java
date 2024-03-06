package com.ringme.cms.repository.kakoakcms.marketing;

import com.ringme.cms.model.kakoakcms.marketing.DeeplinkConfigure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DeeplinkConfigureRepository extends JpaRepository<DeeplinkConfigure, Long> {
    @Query(value = "SELECT * FROM deeplink_configure " +
            "WHERE (:title is null or title LIKE CONCAT('%', :title, '%')) " +
            "AND (:description is null or description LIKE CONCAT('%', :description, '%')) " +
            "AND (:isActive is null or is_active LIKE CONCAT('%', :isActive, '%')) " +
            "AND (:type is null or type LIKE CONCAT('%', :type, '%')) " +
            "AND (:startTime IS NULL OR :endTime IS NULL OR created_at BETWEEN :startTime AND :endTime) " +
            "ORDER BY created_at DESC",
            countQuery = "SELECT count(*) FROM deeplink_configure " +
                    "WHERE (:title is null or title LIKE CONCAT('%', :title, '%')) " +
                    "AND (:description is null or description LIKE CONCAT('%', :description, '%')) " +
                    "AND (:isActive is null or is_active LIKE CONCAT('%', :isActive, '%')) " +
                    "AND (:type is null or type LIKE CONCAT('%', :type, '%')) " +
                    "AND (:startTime IS NULL OR :endTime IS NULL OR created_at BETWEEN :startTime AND :endTime) " +
                    "ORDER BY created_at DESC",
            nativeQuery = true)
    Page<DeeplinkConfigure> getList(@Param("title") String title,
                                 @Param("description") String description,
                                 @Param("isActive") Integer isActive,
                                 @Param("type") Integer type,
                                 @Param("startTime") Date startTime,
                                 @Param("endTime") Date endTime,
                                 Pageable pageable);
    @Query(value = "SELECT id AS `id`, title AS `text`, deeplink AS `deeplink`, deeplink_params AS `deeplink_params` FROM deeplink_configure " +
            "WHERE (:input is null or (id = :input OR title LIKE CONCAT('%', :input, '%'))) LIMIT 20", nativeQuery = true)
    List<String[]> ajaxSearch(@Param("input") String input);
}
