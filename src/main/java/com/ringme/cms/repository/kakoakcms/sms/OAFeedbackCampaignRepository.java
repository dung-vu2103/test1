package com.ringme.cms.repository.kakoakcms.sms;

import com.ringme.cms.model.kakoakcms.sms.OAFeedbackCampaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OAFeedbackCampaignRepository extends JpaRepository<OAFeedbackCampaign, Long> {
    @Query(value = "SELECT * FROM oa_feedback_campaign " +
            "WHERE (:title is null or title LIKE CONCAT('%', :title, '%')) " +
            "AND (:officialAccountId is null or official_account_id = :officialAccountId) " +
            "AND (:inputType is null or input_type = :inputType) " +
            "AND (:msgType is null or msg_type = :msgType) " +
            "AND (:status is null or status = :status) " +
            "AND (:processStatus is null or process_status = :processStatus) " +
            "AND (:startAt1 IS NULL OR start_at BETWEEN :startAt1 AND :startAt2) " +
            "AND (:endAt1 IS NULL OR end_at BETWEEN :endAt1 AND :endAt2) " +
            "ORDER BY created_at DESC",
            countQuery = "SELECT count(*) FROM oa_feedback_campaign " +
                    "WHERE (:title is null or title LIKE CONCAT('%', :title, '%')) " +
                    "AND (:officialAccountId is null or official_account_id = :officialAccountId) " +
                    "AND (:inputType is null or input_type = :inputType) " +
                    "AND (:msgType is null or msg_type = :msgType) " +
                    "AND (:status is null or status = :status) " +
                    "AND (:processStatus is null or process_status = :processStatus) " +
                    "AND (:startAt1 IS NULL OR start_at BETWEEN :startAt1 AND :startAt2) " +
                    "AND (:endAt1 IS NULL OR end_at BETWEEN :endAt1 AND :endAt2) " +
                    "ORDER BY created_at DESC",
            nativeQuery = true)
    Page<OAFeedbackCampaign> get(@Param("title") String title, @Param("officialAccountId") String officialAccountId,
                                 @Param("inputType") String inputType, @Param("msgType") String msgType,
                                 @Param("status") Integer status, @Param("processStatus") Integer processStatus,
                                 @Param("startAt1") String startAt1, @Param("startAt2") String startAt2,
                                 @Param("endAt1") String endAt1, @Param("endAt2") String endAt2, Pageable pageable);

    @Modifying
    @Query(value = "INSERT INTO oa_feedback_campaign (title, official_account_id, start_at, end_at, cron_expression, " +
            "cron_params, input_type, phone_no_list, file_path, msg_type, created_at, updated_at, created_by, updated_by) " +
            "VALUES (:title, :official_account_id, :start_at, :end_at, :cron_expression, :cron_params, :input_type, " +
            ":phone_no_list, :file_path, :msg_type, :created_at, :updated_at, :created_by, :updated_by)", nativeQuery = true)
    void insert(@Param("title") String title, @Param("official_account_id") String official_account_id, @Param("start_at") Date start_at,
                @Param("end_at") Date end_at, @Param("cron_expression") String cron_expression, @Param("cron_params") String cron_params,
                @Param("input_type") String input_type, @Param("phone_no_list") String phone_no_list, @Param("file_path") String file_path,
                @Param("msg_type") String msg_type, @Param("created_at") Date created_at, @Param("updated_at") Date updated_at,
                @Param("created_by") Long created_by, @Param("updated_by") Long updated_by);
}
