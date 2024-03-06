package com.ringme.cms.repository.sys;

import com.ringme.cms.model.sys.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   @Query(value = "Select * from tbl_user u where u.email = ?1", nativeQuery = true)
   Optional<User> findUserByUserName(String email);

   @Query(value = "SELECT * FROM tbl_user u where u.fullname = ?1 AND u.type_user = 0", nativeQuery = true)
   Optional<User> findAllUserTypeAdmin(String name);

   @Query(value = "UPDATE tbl_unit SET represent = ?1 WHERE name = ?2", nativeQuery = true)
   Optional<User> updateRepresent(String fullname,String name);

   List<User> findByChannelId(Long id);

   @Query(value = "Select * from tbl_user u where u.email = ?1 and u.phone = ?2", nativeQuery = true)
   Optional<User> findUserByEmailPhone(String email, String phone);

   @Query(value = "SELECT * FROM tbl_user l WHERE (:id IS NULL OR l.id = :id)" +
           "AND (:email IS NULL OR l.email like %:email%) " +
           "AND (:fullname IS NULL OR l.fullname like %:fullname%) " + "AND (:sdt IS NULL OR l.phone like %:sdt%) " +
           "AND (l.active = 1) order by l.id desc ",
           countQuery = "SELECT COUNT(*) FROM tbl_user l WHERE (:id IS NULL OR l.id = :id)" +
                   "AND (:email IS NULL OR l.email like %:email%) " +
                   "AND (:fullname IS NULL OR l.fullname like %:fullname%) " + "AND (:sdt IS NULL OR l.phone like %:sdt%) " +
                   "AND (l.active = 1) order by l.email", nativeQuery = true)
   Page<User> search(@Param("id") Long id, @Param("email") String email, @Param("fullname") String fullname, @Param("sdt") String sdt, Pageable pageable);

   @Query(value = "Select * from tbl_user u where u.unit_id = ?1 ", nativeQuery = true)
   Optional<User> findUserByIdCheck(Long id);

   @Query(value = "SELECT * FROM tbl_user u where u.type = 1", nativeQuery = true)
   List<User> findAllUserType();



   @Override
   List<User> findAll();

   @Override
   List<User> findAllById(Iterable<Long> longs);

   @Override
   <S extends User> S save(S entity);

   @Override
   void deleteById(Long aLong);

   @Override
   void delete(User entity);

   @Override
   void deleteAllById(Iterable<? extends Long> longs);

   @Query(value = "SELECT channel_id FROM tbl_user where username = :username", nativeQuery = true)
   Long checkChannel(@Param("username") String username);

   @Query(value = "SELECT id FROM tbl_user where username = :username", nativeQuery = true)
   String checkId(@Param("username") String username);

   @Modifying
   @Query(value = "UPDATE tbl_user SET time_zone = :timeZone WHERE id = :id", nativeQuery = true)
   void setTimeZoneLogin(@Param("timeZone") String timeZone, @Param("id") Long id);

   @Query(value = "SELECT time_zone FROM tbl_user where username = :username", nativeQuery = true)
   String getTimeZone(@Param("username") String username);

   @Query(value = "SELECT id FROM tbl_user where username = :username", nativeQuery = true)
   Long getId(@Param("username") String username);

   @Query(value = "SELECT * FROM tbl_user where username = :username", nativeQuery = true)
   User getUser(@Param("username") String username);

   @Query(value = "SELECT id FROM tbl_user", nativeQuery = true)
   List<String> getAllId();

   @Query(value = "SELECT id AS `id`, username AS `text` FROM tbl_user " +
           "WHERE (:input is null or (id = :input OR username LIKE CONCAT('%', :input, '%'))) AND active = 1 ORDER BY username DESC LIMIT 20", nativeQuery = true)
   List<String[]> ajaxSearchUser(@Param("input") String input);
}
