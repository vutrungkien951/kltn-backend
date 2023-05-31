/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.controller;

import com.ou.ScienctificJournal.journal.JournalService;
import com.ou.ScienctificJournal.payload.JournalSubmissionRequest;
import com.ou.ScienctificJournal.payload.PeerReviewRequest;
import com.ou.ScienctificJournal.payload.ReviewRequest;
import com.ou.ScienctificJournal.peerreview.PeerReviewService;
import com.ou.ScienctificJournal.pojo.Journal;
import com.ou.ScienctificJournal.pojo.PeerReview;
import com.ou.ScienctificJournal.pojo.User;
import com.ou.ScienctificJournal.user.UserService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class PeerReviewController {
    @Autowired
    private PeerReviewService peerReviewService;
    @Autowired
    private JournalService journalService;
    @Autowired
    private UserService userService;
    
    @PostMapping("/savePeerReview")
    public ResponseEntity<List<PeerReview>> journalSubmission(@RequestBody PeerReviewRequest request){
        List listPeerReview = new ArrayList();

        for(int i = 0; i < request.getReviewerId().length; i++){
            PeerReview p = new PeerReview();
            Journal j = journalService.getJournalById(request.getJournalId());
            j.setState("PHẢN BIỆN");
            journalService.saveJournal(j);
            p.setJournalId(j);
            p.setReviewerId(userService.getUserById(request.getReviewerId()[i]));
            p.setStartedDate(new Date());
            listPeerReview.add(this.peerReviewService.savePeerReview(p));
        }

        return new ResponseEntity<>(
                listPeerReview,
                HttpStatus.OK
        );
   }
    
   @GetMapping("/getPeerReviewByReviewer")
   public ResponseEntity<List<PeerReview>> getPeerReviewByReviewer(){
        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.getUserByUsername(authen.getName());
       
        List<PeerReview> listPeerReview = this.peerReviewService.getPeerReviewUnreviewByReviewer(user);
        

        return new ResponseEntity<>(
                listPeerReview,
                HttpStatus.OK
        );
   }
   
   @GetMapping("/getPeerReviewByReviewer-pagination")
   public Page<PeerReview> getPeerReviewByReviewerPagination(@RequestParam Optional<Integer> page){
        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.getUserByUsername(authen.getName());
       
        List<PeerReview> listPeerReview = this.peerReviewService.getPeerReviewUnreviewByReviewer(user);
        

         //1. PageListHolder
        PagedListHolder<PeerReview> pageListHolder = new PagedListHolder<PeerReview>(listPeerReview);
        pageListHolder.setPage(page.orElse(0));
        pageListHolder.setPageSize(5);
        //2. PropertyComparator
        List<PeerReview> pageSlice = pageListHolder.getPageList();
        boolean ascending = true;
        PropertyComparator.sort(pageSlice, new MutableSortDefinition());
        
        Pageable pageable = PageRequest.of(page.orElse(0), 5);
        
        Page<PeerReview> pageReview = new PageImpl<>(pageSlice, pageable, listPeerReview.size());

        return pageReview;
   }
   
   @GetMapping("/getPeerReviewByReviewer-test")
   public ResponseEntity<List<Object>> getPeerReviewByReviewerTest(){
        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.getUserByUsername(authen.getName());
       
        List<Object> listPeerReview = this.peerReviewService.getPeerReviewUnreviewByReviewerCustome(user);
        

        return new ResponseEntity<>(
                listPeerReview,
                HttpStatus.OK
        );
   }
   
   @GetMapping("/getPeerReviewByReviewer-test-pagination")
   public Page<Object> getPeerReviewByReviewerTestPagination(@RequestParam Optional<Integer> page){
        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.getUserByUsername(authen.getName());
       
        List<Object> listPeerReview = this.peerReviewService.getPeerReviewUnreviewByReviewerCustome(user);
        

        PagedListHolder<Object> pageListHolder = new PagedListHolder<Object>(listPeerReview);
        pageListHolder.setPage(page.orElse(0));
        pageListHolder.setPageSize(5);
        //2. PropertyComparator
        List<Object> pageSlice = pageListHolder.getPageList();
        boolean ascending = true;
        PropertyComparator.sort(pageSlice, new MutableSortDefinition());
        
        Pageable pageable = PageRequest.of(page.orElse(0), 5);
        
        Page<Object> pageReview = new PageImpl<>(pageSlice, pageable, listPeerReview.size());

        return pageReview;
   }
   
   @GetMapping("/getPeerReviewReviewed")
   public Page<Object> getPeerReviewReviewed(@RequestParam Optional<Integer> page){

  
        List<Object> listPeerReview = this.peerReviewService.getPeerReviewReviewed();
        

         //1. PageListHolder
        PagedListHolder<Object> pageListHolder = new PagedListHolder<Object>(listPeerReview);
        pageListHolder.setPage(page.orElse(0));
        pageListHolder.setPageSize(5);
        //2. PropertyComparator
        List<Object> pageSlice = pageListHolder.getPageList();
        boolean ascending = true;
        PropertyComparator.sort(pageSlice, new MutableSortDefinition());
        
        Pageable pageable = PageRequest.of(page.orElse(0), 5);
        
        Page<Object> pageReview = new PageImpl<>(pageSlice, pageable, listPeerReview.size());

        return pageReview;
   }
   
   @PostMapping("/review-journal/{peerReviewId}")
   public ResponseEntity<PeerReview> reviewJournal(@RequestBody ReviewRequest request,
           @PathVariable(value = "peerReviewId") int peerReviewId){
        PeerReview peerReview = this.peerReviewService.getPeerReviewById(peerReviewId);
        peerReview.setReport(request.getReportUrl());
        peerReview.setRecommendation(request.getRecommendation());
        peerReview = this.peerReviewService.savePeerReview(peerReview);
        Journal journal = peerReview.getJournalId();
        journal.setState("ĐANG CHỜ QUYẾT ĐỊNH");
        this.journalService.saveJournal(journal);
        
        return new ResponseEntity<>(
                peerReview,
                HttpStatus.OK
        );
   }
}
