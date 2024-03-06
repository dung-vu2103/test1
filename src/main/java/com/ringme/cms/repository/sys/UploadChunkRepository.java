package com.ringme.cms.repository.sys;

import com.ringme.cms.model.sys.UploadChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadChunkRepository extends JpaRepository<UploadChunk, Long> {

    @Query(value = "SELECT SUM(chunk_size) FROM tbl_upload_chunk l " +
            "WHERE (:upload_id IS NULL OR l.upload_id = :upload_id)",
            countQuery = "SELECT SUM(chunk_size) FROM tbl_upload_chunk l " +
                    "WHERE (:upload_id IS NULL OR l.upload_id = :upload_id)",
            nativeQuery = true)
    Long checkFileSize(@Param("upload_id") Long uploadId);

    @Override
    <S extends UploadChunk> S save(S entity);

    @Query(value = "SELECT * FROM tbl_upload_chunk l " +
            "WHERE (:upload_id IS NULL OR l.upload_id = :upload_id)",
            countQuery = "SELECT * FROM tbl_upload_chunk l " +
                    "WHERE (:upload_id IS NULL OR l.upload_id = :upload_id)",
            nativeQuery = true)
    List<UploadChunk> listFileChunk(@Param("upload_id") Long uploadId);
}
