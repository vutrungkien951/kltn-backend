/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.controller;

import com.ou.ScienctificJournal.journal.JournalService;
import com.ou.ScienctificJournal.payload.JournalSubmissionRequest;
import com.ou.ScienctificJournal.pojo.Journal;
import com.ou.ScienctificJournal.pojo.User;
import com.ou.ScienctificJournal.user.UserService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class SubmissionController {

    @Autowired
    private JournalService journalService;
    @Autowired
    private UserService userService;

    @PostMapping(value = "/submission")
    public ResponseEntity<Journal> journalSubmission(@RequestBody JournalSubmissionRequest request) {
        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.getUserByUsername(authen.getName());

        //luu journal vao csdl truoc sau do them author journal
        Journal journal = new Journal();
        try {
            journal.setTitle(request.getTitle());
            journal.setDescription(request.getDescription());
            journal.setType(request.getType());
            journal.setState("ĐANG CHỜ XỬ LÝ");
            journal.setFileName(request.getFile_name());
            journal.setFileDownloadUrl(request.getFile_download_url());
            journal.setAuthorId(user);
            journal.setCreatedDate(new Date());
            journal.setLastModified(new Date());
            journal.setListAuthor(request.getListOfAuthors());
            journal.setListKeyword(request.getListOfKeywords());
            journal.setEmail(request.getEmail());
            journal.setOrganization(request.getOrganization());
            journal.setFileVersion(1);
            journal = this.journalService.saveJournal(journal);
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }

        //da luu xuong csdl
        //them author journal
        return new ResponseEntity<>(
                journal,
                HttpStatus.OK
        );
    }
}
