package com.ringme.cms.dto.kakoakcms;

import lombok.*;

import javax.persistence.Column;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private Integer id;
    private Integer userId;
    private String book_name;
    private float price;
    private String createDate;
}
