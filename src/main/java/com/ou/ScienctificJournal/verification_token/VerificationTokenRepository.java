/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.verification_token;

import com.ou.ScienctificJournal.pojo.User;
import com.ou.ScienctificJournal.pojo.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kien
 */
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer>, JpaSpecificationExecutor<VerificationToken>{
    VerificationToken findByToken(String token);
    
    VerificationToken findByUserId(User user);
}
