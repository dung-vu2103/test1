package com.ringme.cms.repository.sys;

import com.ringme.cms.model.sys.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadRepository extends JpaRepository<Upload, Long> {

    @Override
    <S extends Upload> S save(S entity);

    @Query(value = "SELECT * FROM tbl_upload l " +
            "WHERE (:upload_id IS NULL OR l.id = :upload_id)",
            countQuery = "SELECT * FROM tbl_upload l " +
                    "WHERE (:upload_id IS NULL OR l.id = :upload_id)", nativeQuery = true)
    Upload fileUpload(@Param("upload_id") Long uploadId);
}
