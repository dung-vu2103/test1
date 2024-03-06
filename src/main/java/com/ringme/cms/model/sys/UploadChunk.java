package com.ringme.cms.model.sys;

import com.ringme.cms.dto.UploadChunkDto;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tbl_upload_chunk")
public class UploadChunk {
    private static final long serialVersionUID = -297553281792804396L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "upload_id")
    private Long uploadId;

    @Column(name = "chunk_no")
    private int chunkNo;

    @Column(name = "status")
    private int status;

    @Column(name = "chunk_size")
    private Long chunkSize;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "chunk_extension")
    private String chunkExt;

    @Column(name = "chunk_path")
    private String chunkPath;

    public UploadChunkDto convertToDto(Upload upload) {
        UploadChunkDto updateChunkDto = new UploadChunkDto();
        updateChunkDto.setId(this.id);
        updateChunkDto.setUploadId(this.uploadId);

        String strChunkNo = String.valueOf(this.chunkNo);
        updateChunkDto.setChunkNo(strChunkNo);

        String strStatus = String.valueOf(this.status);
        updateChunkDto.setStatus(strStatus);

        updateChunkDto.setChunkSize(this.chunkSize);
        updateChunkDto.setFileSize(this.fileSize);
        updateChunkDto.setChunkExt(this.chunkExt);
        updateChunkDto.setChunkPath(this.chunkPath);
        return updateChunkDto;
    }
}
