package com.ringme.cms.service;

import com.ringme.cms.common.Helper;
import com.ringme.cms.dto.kakoakcms.user.UserDto;
import com.ringme.cms.model.kakoakcms.user.User1;
import com.ringme.cms.repository.kakoakcms.user.UserRepoitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    UserRepoitory userRepoitory;
    @Override
    public UserDto processSearch(String name,String address) {
        UserDto userDto=new UserDto();
        userDto.setName(Helper.processStringSearch(name));
        userDto.setAddress(Helper.processStringSearch(address));
        return userDto;
    }

    @Override
    public Page<User1> getAll(UserDto userDto, int pageNO, int pageSize) {
        Pageable pageable= PageRequest.of(pageNO-1,pageSize);

        return userRepoitory.get(userDto.getName(),userDto.getAddress(),pageable);
    }
    @Override
    public User1 findById(Integer id) {
        return userRepoitory.findById(id).orElse(null);
    }
    @Override
    public void save(User1 user1) {
        userRepoitory.save(user1);
    }
    @Override
    public void delete(Integer id) {
        userRepoitory.deleteById(id);
    }

    @Override
    public UserDto getAddress(String address) {
        UserDto userDto=new UserDto();
        userDto.setAddress(Helper.processStringSearch(address));
        return userDto;
    }
}
