package com.ringme.cms.service.kakoakcms.customer;

import com.ringme.cms.common.Helper;
import com.ringme.cms.common.UploadFile;
import com.ringme.cms.dto.AjaxSearchDto;
import com.ringme.cms.dto.kakoakcms.customer.OfficialAccountDto;
import com.ringme.cms.model.kakoakcms.customer.OfficialAccount;
import com.ringme.cms.model.kakoakcms.customer.OfficialUser;
import com.ringme.cms.model.sys.User;
import com.ringme.cms.repository.kakoakcms.customer.OfficialAccountRepository;
import com.ringme.cms.repository.kakoakcms.customer.OfficialUserRepository;
import com.ringme.cms.repository.sys.UserRepository;
import com.ringme.cms.service.sys.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
@Transactional(value = "primaryTransactionManager")
public class OfficialAccountServiceImpl implements OfficialAccountService {

    @Autowired
    OfficialAccountRepository officialAccountRepository;

    @Autowired
    OfficialUserRepository officialUserRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UploadFile uploadFile;

    @Autowired
    UserService userService;

    @Override
    public Page<OfficialAccount> getListOfficialAccount(String name, Integer status, String createdAt, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Date startTime = null;
        Date endTime = null;

        if (name != null) {
            if (name.trim().equals("")) {
                name = null;
            } else {
                name = name.trim().replaceAll("\s+", " ");
            }
        }

        if (status != null) {
            if (status == 1) {
                status = 1;
            } else if (status == 0){
                status = 0;
            } else {
                status = null;
            }
        }

        if (createdAt != null) {
            if (!createdAt.trim().equals("")) {
                String[] parts = createdAt.split(" - ");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate zonedDateTime = LocalDate.parse(parts[0], formatter);
                LocalDate zonedDateTime2 = LocalDate.parse(parts[1], formatter);
                startTime = Date.from(zonedDateTime.atStartOfDay(ZoneId.systemDefault()).toInstant());
                endTime = Date.from(zonedDateTime2.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
        }
        log.info("===publishedTime Search===>startTime2 {} endTime2 {}", startTime, endTime);

        return officialAccountRepository.getListOfficialAccount(name, status, startTime, endTime, pageable);
    }

    @Override
    public OfficialAccount findById(Long id) {
        try {
            OfficialAccount officialAccount = officialAccountRepository.findById(id).orElse(null);
            return officialAccount;
        }
        catch (Exception e){
            log.info("Exception",e);
        }
        return null;
    }

    @Override
    public void save(OfficialAccount object) {
        officialAccountRepository.save(object);
    }

//    @Override
//    public void save(OfficialAccountDto dto, String statusForm, List<String> listUser, String thumbUpload) {
//        try {
//            Long officialAccountId = dto.getId();
//            OfficialAccount object;
//            User user = userService.getUser();
//            //Check create or update
//            if (officialAccountId == null) {
//                object = new OfficialAccount();
//                log.info("Đã vào thêm");
//            } else {
//                object = findById(dto.getId());
//            }
//
//            object.setName(dto.getName());
//            object.setOfficialAccountId(dto.getOfficialAccountId());
//            object.setDescription(dto.getDescription());
//            object.setStatus(dto.getStatus());
//
//            //Save images to server
//            Path fileNameAvatar = uploadFile.createImageFile(thumbUpload, "image");
//            if (officialAccountId == null || fileNameAvatar != null) {
//                object.setImagePath(File.separator + fileNameAvatar);
//            }
//
//            // Add officialUser to officialUsers list
//            List<OfficialUser> officialUsers = object.getOfficialUsers();
//            if (officialUsers == null) {
//                officialUsers = new ArrayList<>();
//                object.setOfficialUsers(officialUsers);
//            }
//            OfficialUser officialUser = new OfficialUser();
//            officialUser.setOfficialAccount(object);
//            officialUser.setIdUser(0L);
//            officialUsers.add(officialUser);
//
//            log.info("objectSave|" + object);
//            save(object);
//
//            officialAccountId = object.getId();
//            processListUser(listUser, officialAccountId);
//
//        } catch (Exception e) {
//            log.error("ERROR SAVE|" + e.getMessage(), e);
//        }
//    }

    @Override
    public void save(OfficialAccountDto dto, String statusForm, List<String> listUser, String thumbUpload) {
        try {
            Long officialAccountId = dto.getId();
            OfficialAccount object;
            User user = userService.getUser();

            //Check create or update
            if (officialAccountId == null) {
                object = new OfficialAccount();
                log.info("Đã vào thêm");
            } else {
                object = findById(dto.getId());
            }

            object.setName(dto.getName());
            object.setOfficialAccountId(dto.getOfficialAccountId());
            object.setDescription(dto.getDescription());
            object.setStatus(dto.getStatus());

            //Save images to server
            Path fileNameAvatar = uploadFile.createImageFile(thumbUpload, "image");
            if (officialAccountId == null || fileNameAvatar != null) {
                object.setImagePath(File.separator + fileNameAvatar);
            }
//
//            OfficialUser officialUser = new OfficialUser();
//            officialUser.setOfficialAccount(object);
//            officialUser.setIdOfficial(officialAccountId);
//
//            object.getOfficialUsers().add(officialUser);

            log.info("objectSave|" + object);
            save(object);

            officialAccountId = object.getId();
            processListUser(listUser, officialAccountId);

        } catch (Exception e) {
            log.error("ERROR SAVE|" + e.getMessage(), e);
        }
    }


    private void processListUser(List<String> listUser, Long officialAccountId){
        officialUserRepository.deleteAllByOfficialAccountId(officialAccountId);
        List<String> officialUserListId = userRepository.getAllId();
        for (String s : listUser) {
            for (int j = 0; j < officialUserListId.size(); j++) {
                if (s.equals(officialUserListId.get(j))) {
                    OfficialUser officialUser = new OfficialUser();
                    officialUser.setIdOfficial(officialAccountId);
                    officialUser.setIdUser(Long.valueOf(s));
                    officialUserRepository.save(officialUser);
                    break;
                }
                if (j == officialUserListId.size() - 1) {
                    User user = new User();
                    user.setName(s);
                    user.setActive(false);
                    userRepository.save(user);
                    OfficialUser officialUser = new OfficialUser();
                    officialUser.setIdOfficial(officialAccountId);
                    officialUser.setIdUser(Long.valueOf(s));
                    officialUserRepository.save(officialUser);
                }
            }
        }
    }

    @Override
    public void delete(Long id) {
        try {
            officialAccountRepository.delete(id);
        } catch (Exception e) {
            log.error("----------ERROR DELETE ICON|----------" + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<AjaxSearchDto> ajaxSearch(String input) {
        return Helper.listAjax(officialAccountRepository.ajaxSearch(Helper.processStringSearch(input)), 0);
    }
}
