package com.ringme.cms.repository.kakoakcms;

import com.ringme.cms.model.kakoakcms.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    @Query(value = """
            select * from book1 where (:user_id is null or user_id=:user_id)
            """,
            countQuery= """
                    SELECT COUNT(*) FROM book1 where :user_id is null or user_id=:user_id)
                    """,
            nativeQuery = true)
    Page<Book> get(@Param("user_id") Integer userId, Pageable pageable);
}
