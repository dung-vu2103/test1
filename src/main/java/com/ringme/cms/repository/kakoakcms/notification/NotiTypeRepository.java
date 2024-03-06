package com.ringme.cms.repository.kakoakcms.notification;

import com.ringme.cms.model.kakoakcms.notification.NotiType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotiTypeRepository extends JpaRepository<NotiType, Long> {
    @Query(value = """
            SELECT * FROM noti_type
            where (:name is null or name LIKE CONCAT('%', :name, '%'))
            and (:des is null or des LIKE CONCAT('%', :des, '%'))
            """,
            countQuery = """
                    SELECT count(*) FROM noti_type
                    where (:name is null or name LIKE CONCAT('%', :name, '%'))
                    and (:des is null or des LIKE CONCAT('%', :des, '%'))
                    """,
            nativeQuery = true)
    Page<NotiType> get(@Param("name") String name, @Param("des") String des, Pageable pageable);
}
