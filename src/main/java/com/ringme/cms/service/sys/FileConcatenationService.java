package com.ringme.cms.service.sys;

import com.ringme.cms.model.sys.Upload;

import java.io.IOException;

public interface FileConcatenationService {
    Upload concatenationAllFile (Long uploadId) throws IOException;
}
