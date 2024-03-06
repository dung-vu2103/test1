package com.ringme.cms.repository.kakoakcms.customer;

import com.ringme.cms.model.kakoakcms.customer.OfficialUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficialUserRepository extends JpaRepository<OfficialUser, Long> {
    @Modifying
    @Query(value = "DELETE FROM official_user where id_official = :id_official", nativeQuery = true)
    void deleteAllByOfficialAccountId(@Param("id_official") Long officialAccountId);
}
