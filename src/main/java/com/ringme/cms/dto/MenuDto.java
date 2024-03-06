package com.ringme.cms.dto;

import com.ringme.cms.model.sys.Icon;
import com.ringme.cms.model.sys.Router;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor

public class MenuDto {

    private Long id;

    private String name;

    private Router router;

    private Integer order_num;

    private List<MenuDto> parentName;

    private List<MenuDto> lstChildMenus;

    private Icon icon;

}
