package com.ringme.cms.repository.sys;

import com.ringme.cms.model.sys.RouterRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouterRoleRepository extends JpaRepository<RouterRole, Long> {
    @Query(value = "SELECT * From tbl_router_role rr inner join tbl_router r on r.id = rr.router_id inner join tbl_role rl on rl.id = rr.role_id where rr.role_id=?1", nativeQuery = true)
    List<RouterRole> findAllRouterRoleByRoleId(Long roleId);

    @Query(value = "Select * from tbl_router_role rr where rr.role_id in (?1)", nativeQuery = true)
    List<RouterRole> findAllRouterRoleByListRoleId(List<Long> roleIds);

    @Override
    List<RouterRole> findAll();

    @Override
    <S extends RouterRole> S save(S entity);

    @Override
    Optional<RouterRole> findById(Long aLong);

    @Override
    void deleteById(Long aLong);
}
