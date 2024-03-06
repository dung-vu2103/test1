package com.ringme.cms.repository.kakoakcms.customer;

import com.ringme.cms.model.kakoakcms.customer.OfficialAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OfficialAccountRepository extends JpaRepository<OfficialAccount, Long> {
    @Query(value = "SELECT * FROM official_account " +
            "WHERE (:name is null or name LIKE CONCAT('%', :name, '%')) " +
            "AND (:status is null or status LIKE CONCAT('%', :status, '%')) " +
            "AND (:startTime IS NULL OR :endTime IS NULL OR created_at BETWEEN :startTime AND :endTime) " +
            "ORDER BY created_at DESC",
            countQuery = "SELECT count(*) FROM official_account " +
                    "WHERE (:name is null or name LIKE CONCAT('%', :name, '%')) " +
                    "AND (:status is null or status LIKE CONCAT('%', :status, '%')) " +
                    "AND (:startTime IS NULL OR :endTime IS NULL OR created_at BETWEEN :startTime AND :endTime) " +
                    "ORDER BY created_at DESC",
            nativeQuery = true)
    Page<OfficialAccount> getListOfficialAccount(@Param("name") String name,
                         @Param("status") Integer status,
                         @Param("startTime") Date startTime,
                         @Param("endTime") Date endTime,
                         Pageable pageable);

    @Modifying
    @Query(value = "UPDATE official_account SET status = -1 WHERE id = :id", nativeQuery = true)
    void delete(@Param("id") Long id);
    @Query(value = "SELECT official_account_id AS `id`, name AS `text` FROM official_account " +
            "WHERE (:input is null or (official_account_id = :input OR name LIKE CONCAT('%', :input, '%'))) LIMIT 20", nativeQuery = true)
    List<String[]> ajaxSearch(@Param("input") String input);
}
