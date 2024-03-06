package com.ringme.cms.repository.sys;

import com.ringme.cms.model.sys.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query(value = "Select * from tbl_user_role ur inner join tbl_role r on r.id = ur.role_id " +
            "inner join tbl_user u on u.id = ur.user_id where ur.user_id =?1", nativeQuery = true)
    List<UserRole> findUserRoleByUserId(Long id);

    @Override
    <S extends UserRole> S save(S entity);

    @Override
    void deleteById(Long aLong);

    @Override
    void delete(UserRole entity);

    @Override
    <S extends UserRole> List<S> saveAll(Iterable<S> entities);
    @Query(value = "Select * from tbl_user_role ur inner join tbl_role r on r.id = ur.role_id " +
            "inner join tbl_user u on u.id = ur.user_id", nativeQuery = true)
    List<UserRole> findUserRole();
}
