package com.ringme.cms.service.sys;

import com.ringme.cms.model.sys.Upload;
import com.ringme.cms.repository.sys.UploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    UploadRepository uploadRepository;

    @Override
    public void saveUpload(Upload upload) {
        uploadRepository.save(upload);
    }

    @Override
    public Upload fileUpload(Long uploadId) {
        return uploadRepository.fileUpload(uploadId);
    }
}
