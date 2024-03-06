package com.ringme.cms.repository.sys;


import com.ringme.cms.model.sys.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MenuRepository extends PagingAndSortingRepository<Menu, Long> {

//    @Query(value = "SELECT * FROM menu where menu.name =?1 order by modified_date desc", nativeQuery = true)
//    Optional<Menu> findMenuByName(String name);

    @Query(value = "SELECT * FROM tbl_menu WHERE menu.name = (SELECT value FROM message WHERE key = CONCAT('menu.', ?1)) ORDER BY modified_date DESC", nativeQuery = true)
    Optional<Menu> findMenuByName(String name);

    List<Menu> findByParentNameIsNull();
    @Query(value = "SELECT * FROM tbl_menu where parent_name_id = :parentId order by order_num",nativeQuery = true)
    List<Menu> findByParentNameId(@Param("parentId") Long parentId);

    @Override
    List<Menu> findAllById(Iterable<Long> longs);

    @Override
    <S extends Menu> S save(S entity);

    @Override
    void deleteById(Long aLong);

    @Override
    void delete(Menu entity);

    @Query(value = "SELECT name_en FROM tbl_menu where id = :id order by order_num",nativeQuery = true)
    String getNameEn(@Param("id") Long id);
}
