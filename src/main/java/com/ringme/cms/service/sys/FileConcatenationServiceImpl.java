package com.ringme.cms.service.sys;

import com.ringme.cms.config.AppConfiguration;
import com.ringme.cms.model.sys.Upload;
import com.ringme.cms.model.sys.UploadChunk;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class FileConcatenationServiceImpl implements FileConcatenationService{
    @Autowired
    UploadChunkService uploadChunkService;
@Autowired AppConfiguration configuration;
    @Autowired
    UploadService uploadService;

    @Override
    public Upload concatenationAllFile(Long uploadId) {
        List<UploadChunk> listUploadChunks = uploadChunkService.listFileChunk(uploadId);

        Upload uploadInfo = uploadService.fileUpload(uploadId);

            try {
                concatenateBlobFiles(listUploadChunks, uploadInfo);

                // TODO xoa thong tin chunk trong DB

            } catch (Exception e) {
                log.error("ERROR|" + e.getMessage(), e);
            }
            return uploadInfo;
    }

    public void concatenateBlobFiles(List<UploadChunk> uploadChunks, Upload uploadInfo) throws IOException {
        Path obsoluteOutputPath = Paths.get(configuration.getRootPath()).resolve(uploadInfo.getFilePath());
        Path folderPath = obsoluteOutputPath.getParent();
        if(!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }
        try (OutputStream outputStream = Files.newOutputStream(obsoluteOutputPath)) {
            for (UploadChunk uploadChunk : uploadChunks) {
                Path obsoulteChunkPath = Paths.get(configuration.getRootPath()).resolve(uploadChunk.getChunkPath());
                log.info("MERGE|" + "|obsoulteChunkPath = " + obsoulteChunkPath + "|obsoluteOutputPath = " + obsoluteOutputPath);

                try (InputStream inputStream = Files.newInputStream(obsoulteChunkPath)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();

                } catch (Exception e) {
                    log.error("ERROR|" + e.getMessage(), e);
                }
                log.info("DELETE|" + "|obsoulteChunkPath = " + obsoulteChunkPath);
                Files.delete(obsoulteChunkPath);

            }
        }  catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }
}
