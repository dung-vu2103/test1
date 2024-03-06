package com.ringme.cms.repository.kakoakcms.home_config;

import com.ringme.cms.model.kakoakcms.home_config.HomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeCategoryRepository extends JpaRepository<HomeCategory, Long> {
    @Query(value = """
                select * from home_category order by priority
                """,
            nativeQuery = true)
    List<HomeCategory> get();

    @Modifying
    @Query(value = """
                update home_category set priority = :priorityOld where priority = :priorityNew
                """,
            nativeQuery = true)
    void updateForOld(@Param("priorityOld") int priorityOld,
                      @Param("priorityNew") int priorityNew);

    @Modifying
    @Query(value = """
                update home_category set priority = :priorityNew where id = :id
                """,
            nativeQuery = true)
    void updateById(@Param("id") int id,
                    @Param("priorityNew") int priorityNew);
}
