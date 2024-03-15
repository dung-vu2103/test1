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

    public void upload(MultipartFile image, String[] fileName) throws IOException {
        Path rootPath=Paths.get(appConfiguration.getRootPath());
        if(fileName == null)
            return;
        if(!Files.exists(rootPath.resolve(fileName[0]))){
            Files.createDirectories(rootPath.resolve(fileName[0]));
        }
        Path file=rootPath.resolve(fileName[1]);
        try(OutputStream os=Files.newOutputStream(file)){
            os.write(image.getBytes());
        };



    }

    public String[] fileName(MultipartFile image, String type){
        try {
          String originalFilename=image.getOriginalFilename();
          if(originalFilename.equals("") || originalFilename == null)
              return null;
          String fileExtension=originalFilename.substring(originalFilename.lastIndexOf("." + 1));
          if(fileExtension == null || fileExtension.equals(""))
              return null;
          String name=Helper.generateRandomString(32);
          String date=Helper.getTimeNow();
          Path staticPath = Paths.get(appConfiguration.getFileInDBPrefix());
          Path imgaePath=Paths.get(type + "/" + date);
          String[] file=new String[3];
          file[0] =staticPath.resolve(imgaePath).toString().replace("\\\\","/");
          file[1]= staticPath.resolve(imgaePath).resolve( name + "." +fileExtension).toString().replace("\\\\","/");
          file[2]=originalFilename;
          return file;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }
    public Path createImageFile(String thumbUpload, String type) {
        try {
            if(thumbUpload == null || thumbUpload.equals("")){
                return null;
            }
            String[] Array = thumbUpload.trim().split(",");
            String imageBase64="";
            String fileExtension="jpg";
            if(Array.length >1 ){
                imageBase64=Array[1];
                fileExtension=Array[0].replace("data:image/","").replace(",base64","");
            }
            else{
                imageBase64=thumbUpload;
            }
            String name=Helper.generateRandomString(32);
            Path time=Helper.getPathByTime();
            Path relaitvePath=Paths.get(appConfiguration.getFileInDBPrefix());
            relaitvePath=relaitvePath.resolve(type).resolve(time);
            relaitvePath=relaitvePath.resolve(name +"." + fileExtension);
            Path rootPath=Paths.get(appConfiguration.getRootPath());
            rootPath=rootPath.resolve(relaitvePath);
           Path getPath=rootPath.getParent();
           if(!Files.exists(getPath)){
               Files.createDirectories(getPath);
           }
           try(OutputStream os=Files.newOutputStream(rootPath)){
               os.write(Base64.getDecoder().decode(imageBase64));
               os.flush();
           }catch(Exception e){
               log.error("ERROR|" + e.getMessage(), e);
           }

            } catch (Exception e) {
                log.error("ERROR|" + e.getMessage(), e);
            }

        return null;
    }
    public Path getSavedPath(UploadDto data, String type){
        try {
            String filename = data.getFileName();
            if(!StringUtils.hasLength(filename))
                return null;

            String fileExtension = filename.substring(filename.lastIndexOf(".") + 1);

            if(fileExtension == null || fileExtension.isEmpty())
                return null;

            String fileName = Helper.generateRandomString(32);
            Path timePath = Helper.getPathByTime();
            Path relativePath = Paths.get(appConfiguration.getFileInDBPrefix()).resolve(type).resolve(timePath);

            relativePath = relativePath.resolve(fileName + "." + fileExtension);

            return relativePath;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadBase64(String thumbUpload, String[] fileName) {
        try {
            Path CURRENT_FOLDER = processFilePath();
            if (fileName == null) {
                return;
            }

            if (!Files.exists(CURRENT_FOLDER.resolve(fileName[0]))) {
                Files.createDirectories(CURRENT_FOLDER.resolve(fileName[0]));
            }
            Path file = CURRENT_FOLDER.resolve(fileName[1]);
            try (OutputStream os = Files.newOutputStream(file)) {
                os.write(Base64.getDecoder().decode(thumbUpload.trim().replace("data:image/jpeg;base64,", "")));
            }
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }

}
