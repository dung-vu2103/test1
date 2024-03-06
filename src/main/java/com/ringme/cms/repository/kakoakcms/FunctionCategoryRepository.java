package com.ringme.cms.repository.kakoakcms;

import com.ringme.cms.model.kakoakcms.home_config.FunctionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunctionCategoryRepository extends JpaRepository<FunctionCategory, Long> {
    @Query(value = """
                SELECT * FROM function_category
                where (:title is null or title = :title)
                and (:deeplink is null or deeplink = :deeplink)
                and (:enable is null or enable = :enable)
                and (:type is null or type = :type)
                order by created_at
                """,
            nativeQuery = true)
    List<FunctionCategory> get(@Param("title") String title,
                               @Param("deeplink") String deeplink,
                               @Param("enable") Integer enable,
                               @Param("type") Integer type);
}
