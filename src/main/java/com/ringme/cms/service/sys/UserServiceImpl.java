package com.ringme.cms.service.sys;

import com.ringme.cms.common.Helper;
import com.ringme.cms.dto.AjaxSearchDto;
import com.ringme.cms.model.sys.User;
import com.ringme.cms.model.sys.UserRole;
import com.ringme.cms.repository.sys.UserRepository;
import com.ringme.cms.repository.sys.UserRoleRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    public Long checkChannel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountName = ((UserDetails) authentication.getPrincipal()).getUsername();
        log.info("accountNamee|" + accountName);
        return userRepository.checkChannel(accountName);
    }

    @Override
    public String checkId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountName = ((UserDetails) authentication.getPrincipal()).getUsername();
        log.info("accountNamee|" + accountName);
        return userRepository.checkId(accountName);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) throws Exception {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    @Override
    public Optional<User> findByIdUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByUserName(email);
    }

    @Override
    public List<User> findAllUserType() {
        return userRepository.findAllUserType();
    }

    @Override
    public Optional<User> findUserByIdCheck(Long id) {
        return userRepository.findUserByIdCheck(id);
    }

    @Override
    public List<String> processAccountType(List<User> userList) {
        List<UserRole> userRoles = userRoleRepository.findUserRole();
        List<String> accountType = new ArrayList<>(Collections.nCopies(userList.size(), ""));
        for(int i=0; i<userList.size(); i++) {
            for(UserRole userRole : userRoles) {
                if(Objects.equals(userList.get(i).getId(), userRole.getUser().getId())) {
                    if(accountType.get(i).equals(""))
                        accountType.set(i,userRole.getRole().getRoleName().replace("ROLE_",""));
                    else
                        accountType.set(i,accountType.get(i) + ", " + userRole.getRole().getRoleName().replace("ROLE_",""));
                }
            }
        }
        return accountType;
    }

//    @Override
//    public List<User> findByUnitId(Long unitId){
//        return userRepository.findByUnitId(unitId);
//    }

    @Override
    public Page<User> pageUser(int pageNo, int pageSize, Long id, String email, String name, String phone) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        if (id == 0) {
            id = null;
        }
        if (email != null) {
            if (email.trim().equals("")) {
                email = null;
            } else {
                email = email.trim().replaceAll("\s+", "");
            }
        }
        if (name != null) {
            if (name.trim().equals("")) {
                name = null;
            } else {
                name = name.trim().replaceAll("\s+", " ");
            }
        }
        if (phone != null) {
            if (phone.trim().equals("")) {
                phone = null;
            } else {
                phone = phone.trim().replaceAll("\s+", "");
                ;
            }
        }
        return userRepository.search(id, email, name, phone, pageable);
    }

    @Override
    public String getTimeZone() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountName = ((UserDetails) authentication.getPrincipal()).getUsername();
        return userRepository.getTimeZone(accountName);
    }

//    @Override
//    public List<String> processChannelName(List<User> userList) {
//        List<VcsChannel> channels = channelRepository.getAll();
//        List<String> channelName = new ArrayList<>(Collections.nCopies(userList.size(), ""));
//        for(int i=0; i<userList.size(); i++) {
//            for(VcsChannel channel : channels) {
//                if(userList.get(i).getChannelId() != null && userList.get(i).getChannelId().equals(String.valueOf(channel.getId())))
//                    channelName.set(i, channel.getChannelName());
//            }
//        }
//        return channelName;
//    }

    @Override
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    @Override
    public Long getId() {
        return userRepository.getId(getUsername());
    }

    @Override
    public User getUser() {
        return userRepository.getUser(getUsername());
    }
    @Override
    public List<AjaxSearchDto> ajaxSearchUser(String input) {
        return Helper.listAjax(userRepository.ajaxSearchUser(Helper.processStringSearch(input)), 1);
    }

}
