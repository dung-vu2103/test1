package com.ringme.cms.model.sys;

import com.ringme.cms.dto.UploadDto;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tbl_upload")
public class Upload extends EntityBase implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    //0 là upload còn 1 là done
    @Column(name = "status")
    private int status;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "extension")
    private String extension;

    @Column(name = "file_path")
    private String filePath;

//    @Column(name = "file_path_name")
//    private String filePathName;

    @Column(name = "user_id")
    private Long userId;

    public UploadDto convertToDto() {
        UploadDto updateDto = new UploadDto();
        updateDto.setId(this.id);
        updateDto.setFileName(this.fileName);

        String strStatus = String.valueOf(this.status);
        updateDto.setStatus(strStatus);

        updateDto.setFileSize(this.fileSize);
        updateDto.setExtension(this.extension);
        updateDto.setFilePath(this.filePath);
//        updateDto.setFilePathName(this.filePathName);
        updateDto.setUserId(this.userId);
        return updateDto;
    }
}
