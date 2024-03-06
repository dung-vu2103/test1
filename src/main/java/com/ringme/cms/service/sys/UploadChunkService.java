package com.ringme.cms.service.sys;

import com.ringme.cms.model.sys.UploadChunk;

import java.util.List;

public interface UploadChunkService {
    void saveChunkUpload(UploadChunk uploadChunk);

    List<UploadChunk> listFileChunk(Long uploadId);
}
