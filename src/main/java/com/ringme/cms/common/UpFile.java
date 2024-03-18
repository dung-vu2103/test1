package com.ringme.cms.common;


import com.ringme.cms.config.AppConfiguration;
import com.ringme.cms.dto.UploadDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
@Log4j2
@Component
public class UpFile {
    @Autowired
   AppConfiguration appConfiguration;
    public Path processFilePath() {
        log.info("appConfiguration|" + appConfiguration.getRootPath());
        return Paths.get(appConfiguration.getRootPath());
    }

    public void upload(MultipartFile image, String[] fileName){
        try{
            Path rootPath=Paths.get(appConfiguration.getRootPath());
            if(fileName == null)
                return;
            if(!Files.exists(rootPath.resolve(fileName[0]))){
                Files.createDirectories(rootPath.resolve(fileName[0]));
            }
            Path file=rootPath.resolve(fileName[1]);
            log.info("file 1111" + file);
            try(OutputStream os=Files.newOutputStream(file)) {
                os.write(image.getBytes());
            }

        }catch (Exception e)
        {
            log.error("Error" +e.getMessage(),e);
        }

    }
    public String[] fileName(MultipartFile image, String type){
        try {
          String originalFilename=image.getOriginalFilename();
          if(originalFilename.equals("") || originalFilename == null)
              return null;
          String fileExtension=originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
          if(fileExtension == null || fileExtension.equals(""))
              return null;
          String name=Helper.generateRandomString(32);
          String date=Helper.getTimeNow();
          Path staticPath = Paths.get(appConfiguration.getFileInDBPrefix());
          Path imgaePath=Paths.get(type + "/" + date);
          String[] file=new String[3];
          file[0] =staticPath.resolve(imgaePath).toString().replaceAll("\\\\","/");
          file[1]= staticPath.resolve(imgaePath).resolve( name + "." +fileExtension).toString().replaceAll("\\\\","/");
          file[2]=originalFilename;
          return file;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }
    public Path createImageFile(String thumbUpload, String type) {
        try {
            if(thumbUpload == null || thumbUpload.equals(""))
                return null;
            String[] dataArray = thumbUpload.trim().split(",");
            log.info("dhhhhhhhhhhhdhd" + dataArray.toString());
            String imgBase64 = "";
            String fileExtension = "jpg";
            if (dataArray.length > 1) {
                imgBase64 = dataArray[1];
                fileExtension = dataArray[0].replace("data:image/", "").replace(";base64", "");
            } else {
                imgBase64 = thumbUpload;
            }
            log.info("dkdkkdkkddkđk" +dataArray[0]);
            String fileName = Helper.generateRandomString(32);
            Path timePath = Helper.getPathByTime();
            Path relativePath = Paths.get(appConfiguration.getFileInDBPrefix()).resolve(type).resolve(timePath);
            relativePath = relativePath.resolve(fileName + "." + fileExtension);
            Path obsoluteSavePath = Paths.get(appConfiguration.getRootPath()).resolve(relativePath);
            Path folderParent = obsoluteSavePath.getParent();
            if(!Files.exists(folderParent)) {
                Files.createDirectories(folderParent);
            }
            log.info("PATH|" + "|relativePath = " + relativePath + "|obsolutePath = " + obsoluteSavePath);

            try (OutputStream os = Files.newOutputStream(obsoluteSavePath)) {
                os.write(Base64.getDecoder().decode(imgBase64));
                os.flush();
            } catch (Exception e) {
                log.error("ERROR|" + e.getMessage(), e);
            }
            return relativePath;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }

}
