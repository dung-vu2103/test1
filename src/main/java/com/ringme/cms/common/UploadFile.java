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

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Log4j2
@Component
public class UploadFile {
    @Autowired
    AppConfiguration appConfiguration;
    public Path processFilePath() {
        log.info("appConfiguration|" + appConfiguration.getRootPath());
        return Paths.get(appConfiguration.getRootPath());
    }
    @Deprecated
    public void upload(MultipartFile image, String[] fileName) {
        try {
            Path ROOT_FOLDER = Paths.get(appConfiguration.getRootPath());
            log.info("ROOT_FOLDER|" + ROOT_FOLDER);
            if(fileName == null)
                return;
            if (!Files.exists(ROOT_FOLDER.resolve(fileName[0]))) {
                Files.createDirectories(ROOT_FOLDER.resolve(fileName[0]));
            }
            Path file = ROOT_FOLDER.resolve(fileName[1]);
            try (OutputStream os = Files.newOutputStream(file)) {
                os.write(image.getBytes());
            }
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }
    @Deprecated
    public String[] fileName(MultipartFile image, String type){
        try {
            log.info("appConfiguration|" + appConfiguration.getFileInDBPrefix());
            String originalFilename = image.getOriginalFilename();
            if(originalFilename == null || originalFilename.equals(""))
                return null;
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            if(fileExtension == null || fileExtension.isEmpty())
                return null;
            String fileName = Helper.generateRandomString(32);
            String time = Helper.getTimeNow();
            Path staticPath = Paths.get(appConfiguration.getFileInDBPrefix());
            log.info("staticPath|" + staticPath);
            Path imagePath = Paths.get(type + "/" + time);
            String[] file = new String[3];
            file[0] = staticPath.resolve(imagePath).toString().replaceAll("\\\\", "/");
            file[1] = staticPath.resolve(imagePath).resolve(fileName + "." + fileExtension).toString().replaceAll("\\\\", "/");
            file[2] = originalFilename;
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
            String imgBase64 = "";
            String fileExtension = "jpg";
            if (dataArray.length > 1) {
                imgBase64 = dataArray[1];
                fileExtension = dataArray[0].replace("data:image/", "").replace(";base64", "");
            } else {
                imgBase64 = thumbUpload;
            }
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

    public Path saveFileChunk2Storage(MultipartFile fileChunk, String type){
        try {
            Path ROOT_FOLDER = Paths.get(appConfiguration.getRootPath());

            String originalFilename = fileChunk.getOriginalFilename();

            if(!StringUtils.hasLength(originalFilename))
                return null;

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            if(!StringUtils.hasLength(fileExtension))
                return null;

            String fileName = Helper.generateRandomString(32);
            Path timePath = Helper.getPathByTime();

            Path relativePath = Paths.get(appConfiguration.getFileInDBPrefix()).resolve(type).resolve(timePath);

            relativePath = relativePath.resolve(fileName + "." + fileExtension);

            Path obsolutePath = ROOT_FOLDER.resolve(relativePath);

            log.info("CHUNK|" + fileChunk.getContentType() + "|relativePath = " + relativePath + "|obsolutePath = " + obsolutePath);
            Path folderPath = obsolutePath.getParent();
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            try (OutputStream os = Files.newOutputStream(obsolutePath)) {
                os.write(fileChunk.getBytes());
                os.flush();
            } catch (Exception e) {
                log.error("ERROR|" + e.getMessage(), e);
                return null;
            }

            return obsolutePath;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }

    public Path createGameFile(MultipartFile file, String type) {
        try {
            if (file == null || file.isEmpty())
                return null;

            // Lấy phần mở rộng từ tên tệp gốc
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "ttf";

            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            }

            String fileName = Helper.generateRandomString(32);

            String[] timePath = Helper.getTimeNowV2();

            Path pathOnFTPServer = Paths.get(appConfiguration.getGameDirectory()).resolve(type).resolve(timePath[0]);
            pathOnFTPServer = pathOnFTPServer.resolve(fileName + "." + fileExtension);

            log.info("PATH|" + "|pathOnFTPServer = " + pathOnFTPServer);

            //nếu uplaod FTP thì dùng đoạn này
//            try {
//                //upload file vtt dùng lại hàm audio
//                uploadFTPGameFile(file, pathOnFTPServer);
//            } catch (Exception e) {
//                log.error("ERROR|" + e.getMessage(), e);
//            }

            pathOnFTPServer = Paths.get(appConfiguration.getGameDirectory()).resolve(type).resolve(timePath[0]);
            pathOnFTPServer = pathOnFTPServer.resolve(fileName + "." + fileExtension);
            Path absoluteSavePath = Paths.get(appConfiguration.getRootPath()).resolve(pathOnFTPServer);
            Path folderParent = absoluteSavePath.getParent();
            if (!Files.exists(folderParent)) {
                Files.createDirectories(folderParent);
            }
            log.info("PATH|" + "|pathOnFTPServer = " + pathOnFTPServer + "|absolutePath = " + absoluteSavePath);

            file.transferTo(absoluteSavePath.toFile());

            return pathOnFTPServer;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }

    public String[] createImageFileInfo(String thumbUpload, String type) {
        try {
            if(thumbUpload == null || thumbUpload.equals(""))
                return null;
            String imgBase64 = thumbUpload.trim().replace("data:image/jpeg;base64,", "");
            byte[] imageData = Base64.getDecoder().decode(imgBase64);

            String fileExtension = "jpg"; // You can change this based on the image type
            String fileName = Helper.generateRandomString(32);
            String time = Helper.getTimeNow();
            Path staticPath = Paths.get(appConfiguration.getFileInDBPrefix());
            Path imagePath = Paths.get(type + "/" + time);

            String[] fileInfo = new String[3];
            fileInfo[0] = staticPath.resolve(imagePath).toString().replaceAll("\\\\", "/");
            fileInfo[1] = staticPath.resolve(imagePath).resolve(fileName + "." + fileExtension).toString().replaceAll("\\\\", "/");
            fileInfo[2] = fileName + "." + fileExtension;

            return fileInfo;
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