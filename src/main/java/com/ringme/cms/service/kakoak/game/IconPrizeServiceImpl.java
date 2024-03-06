//package com.ringme.cms.service.kakoak.game;
//
//import com.ringme.cms.common.UploadFile;
//import com.ringme.cms.dto.kakoakcms.game.IconPrizeDto;
//import com.ringme.cms.model.kakoakcms.game.IconPrize;
//import com.ringme.cms.repository.kakoak.game.IconPrizeRepository;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.File;
//import java.nio.file.Path;
//
//@Service
//@Log4j2
//@Transactional(value = "kakoakTransactionManager")
//public class IconPrizeServiceImpl implements IconPrizeService{
//    @Autowired
//    IconPrizeRepository iconPrizeRepository;
//
//    @Autowired
//    UploadFile uploadFile;
//
//    @Override
//    public Page<IconPrize> getListIconPrize(String iconName, String iconCode, Integer iconActive, String iconType, int pageNo, int pageSize) {
//        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
//
//        if (iconName != null) {
//            if (iconName.trim().equals("")) {
//                iconName = null;
//            } else {
//                iconName = iconName.trim().replaceAll("\s+", " ");
//            }
//        }
//
//        if (iconCode != null) {
//            if (iconCode.trim().equals("")) {
//                iconCode = null;
//            } else {
//                iconCode = iconCode.trim().replaceAll("\s+", " ");
//            }
//        }
//
//        if (iconActive != null) {
//            if (iconActive == 1) {
//                iconActive = 1;
//            } else if (iconActive == 0){
//                iconActive = 0;
//            } else {
//                iconActive = null;
//            }
//        }
//
//        if (iconType != null) {
//            if (iconType.trim().equals("Data")) {
//                iconType = iconType.trim().replaceAll("\s+", " ");
//            } else if (iconType.trim().equals("Money")) {
//                iconType = iconType.trim().replaceAll("\s+", " ");
//            } else if (iconType.trim().equals("Icon")) {
//                iconType = iconType.trim().replaceAll("\s+", " ");
//            } else if (iconType.trim().equals("")) {
//                iconType = null;
//            } else {
//                iconType = null;
//            }
//        }
//
//        return iconPrizeRepository.getListIconPrize(iconName, iconCode, iconActive, iconType, pageable);
//    }
//
//    @Override
//    public IconPrize findById(Long id) {
//        try {
//            IconPrize iconPrize = iconPrizeRepository.findById(id).orElse(null);
//            return iconPrize;
//        }
//        catch (Exception e){
//            log.info("Exception",e);
//        }
//        return null;
//    }
//
//    @Override
//    public void save(IconPrize object) {
//        iconPrizeRepository.save(object);
//    }
//
//    @Override
//    public void saveIconPrize(IconPrizeDto dto, String statusForm, String thumbUpload) {
//        try {
//            Long iconPrizeId = dto.getId();
//            IconPrize object;
//            //Check create or update
//            if (iconPrizeId == null) {
//                log.info("--------Icon Prize này đã tồn tại!------------");
//                object = new IconPrize();
//                object.setActive(0);
//                object.setValue(0L);
//            } else {
//                object = findById(dto.getId());
//            }
//            object.setName(dto.getName());
//            object.setCode(dto.getCode());
//            object.setType(dto.getType());
//            //Save images to server
//            Path fileNameIcon = uploadFile.createImageFile(thumbUpload, "image");
//            if (iconPrizeId == null || fileNameIcon != null) {
//                object.setLink_icon(File.separator + fileNameIcon);
//            }
//            log.info("SAVE OBJECT|" + object);
//            save(object);
//        } catch (Exception e) {
//            log.error("ERROR SAVE|" + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public void deleteIcon(Long id) {
//        try {
//            iconPrizeRepository.deleteIcon(id);
//        } catch (Exception e) {
//            log.error("----------ERROR DELETE ICON|----------" + e.getMessage(), e);
//            throw e;
//        }
//    }
//
//    @Override
//    public void active(Long id) {iconPrizeRepository.active(id);}
//
//    @Override
//    public void block(Long id) {iconPrizeRepository.block(id);}
//
//    @Override
//    public Integer countIconPrizeActive (){
//        return iconPrizeRepository.countIconPrizeActive();
//    }
//}
