/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal;

import com.ou.ScienctificJournal.jwt.JwtTokenProvider;
import com.ou.ScienctificJournal.payload.LoginRequest;
import com.ou.ScienctificJournal.payload.LoginResponse;
import com.ou.ScienctificJournal.payload.RandomStuff;
import com.ou.ScienctificJournal.payload.SignUpRequest;
import com.ou.ScienctificJournal.pojo.User;
import com.ou.ScienctificJournal.user.CustomerUserDetails;
import com.ou.ScienctificJournal.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kien
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class APIRestController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

//    @GetMapping("/users")
//    public ResponseEntity<List<User>> getUsers() {
//        List<User> users = new ArrayList<>();
//        User user = new User();
//        user.setUserRole("ROLE_ADMIN");
//
//        users.add(user);
//
//        return new ResponseEntity<>(
//                users,
//                HttpStatus.OK
//        );
//    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@Validated @RequestBody LoginRequest loginRequest) {

        // Xác thực từ username và password.
        Authentication authentication;
        try {
            
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
            );
        } catch (AuthenticationException ex){
            
            return new ResponseEntity<>(
                    new LoginResponse(""),
                    HttpStatus.NO_CONTENT
            );
        }
        

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        User user = userService.getUserByUsername(loginRequest.getUsername());
        
        if(user != null) {
            if(!user.getActive()){
                return new ResponseEntity<>(
                    new LoginResponse(""),
                    HttpStatus.ACCEPTED
                );
            }
            // Set thông tin authentication vào Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Trả về jwt cho người dùng.
            String jwt = tokenProvider.generateToken((CustomerUserDetails) authentication.getPrincipal());
            return new ResponseEntity<>(
                    new LoginResponse(jwt),
                    HttpStatus.OK
            );
        }else {
            return new ResponseEntity<>(
                    new LoginResponse(""),
                    HttpStatus.NO_CONTENT
            );
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<User> signUpUser(@RequestBody @Validated SignUpRequest signUpRequest) {
        
        User user = this.userService.createUser(signUpRequest);

        if(user != null){
            
            return new ResponseEntity<>(
                user,
                HttpStatus.OK
            );
        } else {
            
            return new ResponseEntity<>(
                null,
                HttpStatus.NO_CONTENT
            );
        }

    }

    @PostMapping("/current-user")
    public ResponseEntity<UserDetails> getCurrentUser() {
        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = this.userService.loadUserByUsername(authen.getName());
//                this.userService.loadUserByUsername("abc");

        return new ResponseEntity<>(
                user,
                HttpStatus.OK
        );
    }

    // Api /api/random yêu cầu phải xác thực mới có thể request
    @GetMapping("/random")
    public RandomStuff randomStuff() {
        return new RandomStuff("JWT Hợp lệ mới có thể thấy được message này");
    }
}
