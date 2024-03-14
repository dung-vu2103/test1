package com.ringme.cms.repository.kakoakcms;

import com.ringme.cms.model.kakoakcms.User1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepoitory extends JpaRepository<User1,Integer> {
    @Query(value = """
            select * from user where (:name is null or name like CONCAT('%',:name,'%'))
            """,
            countQuery= """
                    SELECT COUNT(*) FROM user where (:name is null or name like CONCAT('%',:name,'%'))
                    """,
            nativeQuery = true)
    Page<User1> get(@Param("name") String name, Pageable pageable);
}
