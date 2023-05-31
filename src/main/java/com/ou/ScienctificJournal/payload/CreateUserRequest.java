/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.payload;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 *
 * @author kien
 */
@Data
public class CreateUserRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
    
    @NotBlank
    private String firstName;
    
    @NotBlank
    private String lastName;
    @NotBlank
    private String degree;
    @NotBlank
    private String scientificTitle;
    @NotBlank
    private String workPlace;
    @NotBlank
    private String email;
    @NotBlank
    private String phone;
    @NotBlank
    private String userRole;
}
