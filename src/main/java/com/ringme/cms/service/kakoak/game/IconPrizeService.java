//package com.ringme.cms.service.kakoak.game;
//
//import com.ringme.cms.dto.kakoakcms.game.IconPrizeDto;
//import com.ringme.cms.model.kakoakcms.game.IconPrize;
//import org.springframework.data.domain.Page;
//
//public interface IconPrizeService {
//    Page<IconPrize> getListIconPrize(String iconName, String iconCode, Integer iconActive, String iconType, int pageNo, int pageSize);
//
//    IconPrize findById(Long id);
//
//    void save(IconPrize object);
//
//    void saveIconPrize(IconPrizeDto dto, String statusForm, String thumbUpload);
//
//    void deleteIcon(Long id);
//
//    void active(Long id);
//
//    void block(Long id);
//
//    Integer countIconPrizeActive();
//}
