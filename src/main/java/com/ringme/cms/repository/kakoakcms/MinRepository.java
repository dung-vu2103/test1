package com.ringme.cms.repository.kakoakcms;

import com.ringme.cms.model.kakoakcms.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MinRepository extends JpaRepository<Min, Integer> {
    @Query(value = """
            select * from min1
            """, nativeQuery = true)
    List<Min> search();

    @Query(value = """
            select * from min1 where (:name is null or name like CONCAT('%',:name,'%'))
            """,
            countQuery= """
                    SELECT COUNT(*) FROM min1 where (:name is null or name like CONCAT('%',:name,'%'))
                    """,
            nativeQuery = true)
    Page<Min> get(@Param("name") String name, Pageable pageable);

}
