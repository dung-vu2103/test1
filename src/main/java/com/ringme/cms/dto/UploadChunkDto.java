package com.ringme.cms.dto;

import com.ringme.cms.model.sys.UploadChunk;
import lombok.*;

import java.text.ParseException;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class UploadChunkDto {
    private Long id;

    private Long uploadId;

    private String chunkNo;

    private String status;

    private Long chunkSize;

    private Long fileSize;

    private String chunkExt;

    private String chunkPath;

    public UploadChunk convertToEntity() throws ParseException {
        UploadChunk uploadChunk = new UploadChunk();
        uploadChunk.setId(id);;
        if (chunkNo != null) {
            int intChunkNo = Integer.parseInt(chunkNo);
            uploadChunk.setChunkNo(intChunkNo);
        }
        if (status != null) {
            int intStatus = Integer.parseInt(status);
            uploadChunk.setStatus(intStatus);
        }
        uploadChunk.setChunkSize(chunkSize);
        uploadChunk.setFileSize(fileSize);
        if (chunkExt != null) {
            uploadChunk.setChunkExt(chunkExt.trim());
        }
        if (chunkPath != null) {
            uploadChunk.setChunkPath(chunkPath.trim());
        }
        return uploadChunk;
    }
}
