/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.controller;

import com.ou.ScienctificJournal.email.EmailDetails;
import com.ou.ScienctificJournal.email.EmailService;
import com.ou.ScienctificJournal.payload.VerificationTokenRequest;
import com.ou.ScienctificJournal.pojo.User;
import com.ou.ScienctificJournal.pojo.VerificationToken;
import com.ou.ScienctificJournal.user.UserService;
import com.ou.ScienctificJournal.verification_token.VerificationTokenService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kien
 */
@RestController
@RequestMapping("/api")
public class VerificationTokenController {
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
//    @GetMapping("/verification_token")
//    public ResponseEntity<VerificationToken> confirmEmail(@RequestParam String token){
//        VerificationToken verificationToken = verificationTokenService.findByToken(token);
//        
//        return new ResponseEntity<>(
//                verificationToken,
//                HttpStatus.BAD_REQUEST
//            );
//    }
    
    @PostMapping("/verification_token")
    public ResponseEntity<VerificationToken> sendEmailVerificationToken(@RequestBody VerificationTokenRequest request){
        
        if(userService.getUserByUsername(request.getUsername()) == null){
            User user = new User();
            user.setActive(false);
            user.setUsername(request.getUsername());
            String initialPassword = UUID.randomUUID().toString();
            user.setPassword(passwordEncoder.encode(initialPassword));
            user.setEmail(request.getEmail());
            user.setUserRole("ROLE_REVIEWER");
            user = userService.saveUser(user);
            
            
            String token = UUID.randomUUID().toString();
            VerificationToken verificationToken = verificationTokenService.save(user, token);
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(request.getEmail());
            emailDetails.setSubject("[OU Journal] thư mời làm reviewer OU Journal");
            String linkActive = "http://localhost:8080/verification_token?token=" + token;
            String msg = "Chúng tôi đã tạo tài khooản cho bạn\nUsername: " + user.getUsername();
            msg += ", password: " + initialPassword;
            msg += "\nĐường dẫn kích hoạt tài khooản: " + linkActive;
            
            emailDetails.setMsgBody(msg);
            emailService.sendSimpleMail(emailDetails);
            
            return new ResponseEntity<>(
                verificationToken,
                HttpStatus.OK
            );
        }
        
        return new ResponseEntity<>(
                null,
                HttpStatus.BAD_REQUEST
            );
    }
}
