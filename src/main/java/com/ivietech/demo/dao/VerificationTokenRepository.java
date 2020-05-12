package com.ivietech.demo.dao;



import com.ivietech.demo.entity.Accounts;
import com.ivietech.demo.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

}
