package com.ringme.cms.repository.kakoakcms.notification;

import com.ringme.cms.model.kakoakcms.notification.NotiEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotiEntityRepository extends JpaRepository<NotiEntity, Long> {
    @Query(value = """
            SELECT * FROM noti_entity
            where (:title is null or title LIKE CONCAT('%', :title, '%'))
            AND (:inputType is null or input_type = :inputType)
            AND (:msgType is null or msg_type = :msgType)
            AND (:status is null or status = :status)
            AND (:processStatus is null or process_status = :processStatus)
            AND (:startAt1 IS NULL OR start_at BETWEEN :startAt1 AND :startAt2)
            AND (:endAt1 IS NULL OR end_at BETWEEN :endAt1 AND :endAt2)
            ORDER BY created_at DESC
            """,
            countQuery = """
                    SELECT count(*) FROM noti_entity
                    where (:title is null or title LIKE CONCAT('%', :title, '%'))
                    AND (:inputType is null or input_type = :inputType)
                    AND (:msgType is null or msg_type = :msgType)
                    AND (:status is null or status = :status)
                    AND (:processStatus is null or process_status = :processStatus)
                    AND (:startAt1 IS NULL OR start_at BETWEEN :startAt1 AND :startAt2)
                    AND (:endAt1 IS NULL OR end_at BETWEEN :endAt1 AND :endAt2)
                    ORDER BY created_at DESC
                    """,
            nativeQuery = true)
    Page<NotiEntity> get(@Param("title") String title, @Param("inputType") String inputType, @Param("msgType") String msgType,
                         @Param("status") Integer status, @Param("processStatus") Integer processStatus,
                         @Param("startAt1") String startAt1, @Param("startAt2") String startAt2,
                         @Param("endAt1") String endAt1, @Param("endAt2") String endAt2, Pageable pageable);
}
