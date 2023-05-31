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
public class JournalSubmissionRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String type;
    @NotBlank
    private String file_name;
    @NotBlank
    private String file_download_url;
    @NotBlank
    private String listOfAuthors;
    @NotBlank
    private String listOfKeywords;
    @NotBlank
    private String email;
    @NotBlank
    private String organization;
}
