package com.akb.chit_fund.repository;

import com.akb.chit_fund.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByMobileNumber(String mobileNumber);
}
