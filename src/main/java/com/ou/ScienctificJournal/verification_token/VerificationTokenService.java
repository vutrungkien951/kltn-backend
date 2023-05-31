/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.verification_token;

import com.ou.ScienctificJournal.pojo.User;
import com.ou.ScienctificJournal.pojo.VerificationToken;
import com.ou.ScienctificJournal.user.UserRepository;
import java.sql.Timestamp;
import java.util.Calendar;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kien
 */
@Service
@Transactional
public class VerificationTokenService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    
    public VerificationToken findByToken(String token){
        return verificationTokenRepository.findByToken(token);
    }
    
    public VerificationToken findByUser(User user){
        return verificationTokenRepository.findByUserId(user);
    }
    
    public VerificationToken save(User user, String token){
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUserId(user);
        verificationToken.setExpireDate(calculateExpireDate(60*24*3));
        verificationTokenRepository.save(verificationToken);
        
        return verificationToken;
    }
    
    private Timestamp calculateExpireDate(int expireTimeMinutes){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expireTimeMinutes);
        return new Timestamp(cal.getTime().getTime());
    }
}
