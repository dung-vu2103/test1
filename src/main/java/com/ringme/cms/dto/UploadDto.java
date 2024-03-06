package com.ringme.cms.dto;

import com.ringme.cms.model.sys.Upload;
import lombok.*;

import java.text.ParseException;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class UploadDto {
    private Long id;

    private String fileName;

    private String status;

    private Long fileSize;

    private String extension;

    private String filePath;

//    private String filePathName;

    private Long userId;

    public Upload convertToEntity() throws ParseException {
        Upload upload = new Upload();
        upload.setId(id);;
        if (fileName != null) {
            upload.setFileName(fileName.trim());
        }
        if (status != null) {
            int intStatus = Integer.parseInt(status);
            upload.setStatus(intStatus);
        }
        upload.setFileSize(fileSize);
        if (extension != null) {
            upload.setExtension(extension.trim());
        }
        if (filePath != null) {
            upload.setFilePath(filePath.trim());
        }
        upload.setUserId(userId);
        return upload;
    }
}
