package com.ringme.cms.repository.kakoakcms.report;

import com.ringme.cms.model.kakoakcms.report.TelemorReportDailyNew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelemorReportDailyNewRepository extends JpaRepository<TelemorReportDailyNew, Long> {
    @Query(value = "SELECT * FROM telemor_report_daily_new " +
            "WHERE (:startTime IS NULL OR :endTime IS NULL OR date_report BETWEEN :startTime AND :endTime) " +
            "ORDER BY date_report DESC",
            countQuery = "SELECT count(*) FROM telemor_report_daily_new " +
                    "WHERE (:startTime IS NULL OR :endTime IS NULL OR date_report BETWEEN :startTime AND :endTime) " +
                    "ORDER BY date_report DESC",
            nativeQuery = true)
    Page<TelemorReportDailyNew> getPage(@Param("startTime") String startTime,
                                        @Param("endTime") String endTime,
                                        Pageable pageable);
    @Query(value = "SELECT * FROM telemor_report_daily_new " +
            "WHERE (:startTime IS NULL OR :endTime IS NULL OR date_report BETWEEN :startTime AND :endTime) " +
            "ORDER BY date_report DESC", nativeQuery = true)
    List<TelemorReportDailyNew> getList(@Param("startTime") String startTime,
                                        @Param("endTime") String endTime);
}
