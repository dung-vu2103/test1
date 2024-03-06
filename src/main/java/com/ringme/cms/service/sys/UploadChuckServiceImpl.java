package com.ringme.cms.service.sys;

import com.ringme.cms.model.sys.UploadChunk;
import com.ringme.cms.repository.sys.UploadChunkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(value = "primaryTransactionManager")
public class UploadChuckServiceImpl implements UploadChunkService {

    @Autowired
    UploadChunkRepository uploadChuckRepository;

    @Override
    public void saveChunkUpload(UploadChunk uploadChunk) {
        uploadChuckRepository.save(uploadChunk);
    }

    @Override
    public List<UploadChunk> listFileChunk(Long uploadId) {
        return uploadChuckRepository.listFileChunk(uploadId);
    }
}
