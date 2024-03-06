package com.ringme.cms.repository.sys;

import com.ringme.cms.model.sys.Router;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RouterRepository extends PagingAndSortingRepository<Router, Long> {
    @Query(value = "SELECT * from tbl_router r where r.active = 1", nativeQuery = true)
    List<Router> findAllRouterActive();

    @Query(value = "SELECT * from tbl_router r where r.active = 0", nativeQuery = true)
    List<Router> findAllRouterUnActive();

    @Modifying
    @Query(value = "UPDATE tbl_router r set r.active =?1 where r.id =?2", nativeQuery = true)
    void updateStatus(boolean check, Long id);

    @Query(value = "SELECT r.* from tbl_router r where r.id not in (?1) and r.active =1", nativeQuery = true)
    List<Router> findAllRouterNotInRole(List<Long> roleIds);

    @Query(value = "SELECT r.* from tbl_router r where r.id in (?1) and r.active =1", nativeQuery = true)
    List<Router> findAllRouterByListId(List<Long> id);

    @Override
    List<Router> findAll();

    @Override
    List<Router> findAllById(Iterable<Long> longs);

    @Override
    <S extends Router> List<S> saveAll(Iterable<S> entities);

    @Override
    <S extends Router> S save(S entity);

    @Override
    Optional<Router> findById(Long aLong);

    @Override
    void deleteById(Long aLong);

    @Query(value = "SELECT * FROM tbl_router order by router_link asc", countQuery = "SELECT COUNT(*) FROM tbl_router", nativeQuery = true)
    Page<Router> search(Pageable pageable);
}
