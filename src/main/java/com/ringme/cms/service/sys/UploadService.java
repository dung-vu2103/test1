package com.ringme.cms.service.sys;

import com.ringme.cms.model.sys.Upload;

public interface UploadService {
    void saveUpload(Upload upload);

    Upload fileUpload(Long uploadId);
}
