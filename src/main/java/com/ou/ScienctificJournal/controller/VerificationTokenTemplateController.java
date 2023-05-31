/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.controller;

import com.ou.ScienctificJournal.pojo.User;
import com.ou.ScienctificJournal.pojo.VerificationToken;
import com.ou.ScienctificJournal.user.UserService;
import com.ou.ScienctificJournal.verification_token.VerificationTokenService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author kien
 */
@Controller
public class VerificationTokenTemplateController {
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private UserService userService;
    
    @GetMapping("/verification_token")
    public String home(Model model, @RequestParam(name="token", required=true) String token) {
        VerificationToken veri_token = verificationTokenService.findByToken(token);
        Date expireDate = veri_token.getExpireDate();
        Date date = new Date();
        if(date.before(expireDate)){
            User user = veri_token.getUserId();
            user.setActive(true);
            userService.saveUser(user);

            return "verification_success";
        }else{
            return "verification_fail";
        }
    }
}
