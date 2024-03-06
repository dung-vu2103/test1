package com.ringme.cms.repository.sys;

import com.ringme.cms.model.sys.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "Select r.* from tbl_role r where r.id not in (?1) order by modified_date desc", nativeQuery = true)
    List<Role> findAllRoleNotInListIdRole(List<Long> id);

    @Query(value = "Select * from tbl_role r where r.role_name =?1 order by modified_date desc", nativeQuery = true)
    Optional<Role> findByRoleName(String roleName);

    @Override
    List<Role> findAllById(Iterable<Long> longs);

    @Override
    <S extends Role> S save(S entity);

    @Override
    Optional<Role> findById(Long aLong);

    @Override
    void delete(Role entity);

    @Override
    void deleteById(Long aLong);
}
