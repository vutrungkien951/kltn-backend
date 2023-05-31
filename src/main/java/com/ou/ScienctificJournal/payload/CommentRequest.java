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
public class CommentRequest {
    @NotBlank
    private String content;
    @NotBlank
    private String journalId;
}
