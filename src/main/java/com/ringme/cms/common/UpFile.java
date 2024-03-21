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

    public Path createImg(String chuoi, String type) {
        try {
            if (chuoi == null) {
                return null;
            }
            String[] a = chuoi.trim().split(",");
            String imgBase64 = "";
            String fileExtension = "jpg";
            if (a.length > 1) {
                imgBase64 = a[1];
                fileExtension = a[0].replace("data:image/", "").replace(";base64", "");
            } else {
                imgBase64 = chuoi;
            }
            String name = Helper.generateRandomString(32);
            Path time = Helper.getPathByTime();
            Path staticPath = Paths.get(appConfiguration.getFileInDBPrefix()).resolve(type).resolve(time);
            staticPath = staticPath.resolve(name + "." + fileExtension);
            Path rootPa = Paths.get(appConfiguration.getRootPath()).resolve(staticPath);
            Path folderParent = rootPa.getParent();
            if (!Files.exists(folderParent)) {
                Files.createDirectories(folderParent);
            }
            try (OutputStream os = Files.newOutputStream(rootPa)) {
                os.write(Base64.getDecoder().decode(imgBase64));
                os.flush();
            } catch (Exception e) {
                log.error("error" + e.getMessage());
            }
            return staticPath;

        } catch (Exception e) {
            log.error("Error" + e.getMessage(), e);
        }
        return null;
    }
    public String[] filename(MultipartFile file,String type){
        try{
            String orignal=file.getOriginalFilename();
            String fileExtension = orignal.substring(orignal.lastIndexOf(".") + 1 );
            String name=Helper.generateRandomString(32);
            Path time=Helper.getPathByTime();
            Path dbPah=Paths.get(appConfiguration.getFileInDBPrefix());
            Path imgPath=Paths.get(type + "/" + time);
            String[] f=new String[3];
            f[0]=dbPah.resolve(imgPath).toString().replaceAll("\\\\","/");
            f[1]=dbPah.resolve(imgPath).resolve(name +"." + fileExtension ).toString().replaceAll("\\\\","/");
            f[2]=orignal;
            return f;
        } catch (Exception e){
            log.error("error" + e.getMessage(),e);
        }
        return null;
    }
    public void update(MultipartFile file,String[] filename){
        try{
            Path rot=Paths.get(appConfiguration.getRootPath());
            if(!Files.exists(rot.resolve(filename[0]))){
                Files.createDirectories(rot.resolve(filename[0]));
            }
            Path file1=rot.resolve(filename[1]);
            try (OutputStream os=Files.newOutputStream(file1)){
                os.write(file.getBytes());
            }
        }catch (Exception e){
            log.error("error" + e.getMessage());
        }
    }
}
