/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.controller;

import com.ou.ScienctificJournal.comment.CommentService;
import static com.ou.ScienctificJournal.constants.UserRoleConst.ROLE_ADMIN;
import com.ou.ScienctificJournal.journal.JournalService;
import com.ou.ScienctificJournal.payload.CommentRequest;
import com.ou.ScienctificJournal.payload.RatingRequest;
import com.ou.ScienctificJournal.pojo.Comment;
import com.ou.ScienctificJournal.pojo.Journal;
import com.ou.ScienctificJournal.pojo.Rating;
import com.ou.ScienctificJournal.pojo.User;
import com.ou.ScienctificJournal.rating.RatingService;
import com.ou.ScienctificJournal.user.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class CommentRatingController {

    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private JournalService journalService;
    @Autowired
    private RatingService ratingService;

    @PostMapping("/comment")
    public ResponseEntity<Comment> sendComment(@RequestBody CommentRequest request) {
        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.getUserByUsername(authen.getName());

        if (user == null) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.BAD_REQUEST
            );
        } else {
            Journal journal = journalService.getJournalById(Integer.parseInt(request.getJournalId()));
            //journal phai co magazine
            if (journal.getMagazineId() == null) {
                return new ResponseEntity<>(
                        null,
                        HttpStatus.BAD_REQUEST
                );
            }else{
                Comment comment = this.commentService.save(user, journal, request.getContent());
                return new ResponseEntity<>(
                        comment,
                        HttpStatus.OK
                );
            }
        }
    }
    
    // method to delete comment (only use by ROLE_ADMIN)
    @PostMapping("/delete-comment/{commentId}")
    public ResponseEntity<Comment> deleteComment(@PathVariable(value = "commentId") int commentId) {
        
        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.getUserByUsername(authen.getName());

        if (user == null) {
            
            return new ResponseEntity<>(
                    null,
                    HttpStatus.BAD_REQUEST
            );
        } else {
            
            if(ROLE_ADMIN.equals(user.getUserRole())){
                
                Comment comment = this.commentService.getCommentById(commentId);
            
                if (this.commentService.deleteComment(comment)) {

                    return new ResponseEntity<>(
                            null,
                            HttpStatus.OK
                    );
                }else{

                    return new ResponseEntity<>(
                            null,
                            HttpStatus.NOT_MODIFIED
                    );
                }
            }else{
                
                return new ResponseEntity<>(
                            null,
                            HttpStatus.NOT_MODIFIED
                    );
            }
        }
    }
    
    @GetMapping("/comments/{journalId}")
    public ResponseEntity<List<Comment>> getCommentsInJournal(@PathVariable(value = "journalId") int journalId) {
        
        
        return new ResponseEntity<>(
                        commentService.findCommentByJournalId(journalId),
                        HttpStatus.OK
                );
    }
    
    @PostMapping("/rating")
    public ResponseEntity<Rating> rating(@RequestBody RatingRequest request) {
        
        // get authentication
        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        
        // get user by authentication
        User user = this.userService.getUserByUsername(authen.getName());

        // check user != null
        if (user == null) {
            
            return new ResponseEntity<>(
                    null,
                    HttpStatus.BAD_REQUEST
            );
        } else {
            
            Journal journal = journalService.getJournalById(Integer.parseInt(request.getJournalId()));
            
            // lay ra rating xong check neu != null thi update rating.rate con khong thi tao moi         
            if(ratingService.getRating(user, journal) == null){
                
                //journal phai co magazine
                if (journal.getMagazineId() != null) {

                    if(0 < request.getRate() && request.getRate() <= 5){

                        Rating rating = this.ratingService.save(user, journal, request.getRate());
                        return new ResponseEntity<>(
                            rating,
                            HttpStatus.OK
                        );
                    }
                }
            }else{
                
                // update rating
                if (journal.getMagazineId() != null) {

                    if(0 < request.getRate() && request.getRate() <= 5){
                        Rating oldRating = ratingService.getRating(user, journal);
                        oldRating.setRating(request.getRate());
                        
                        this.ratingService.updateRating(oldRating);
                        
                        //return oldRating after updated
                        return new ResponseEntity<>(
                            oldRating,
                            HttpStatus.OK
                        );
                    }
                }
            }

        }
        
        return new ResponseEntity<>(
                       null,
                       HttpStatus.NOT_MODIFIED
        );
    }
    
    @GetMapping("/ratingAvg/{journalId}")
    public ResponseEntity<Double> getAvgRatingByJournal(@PathVariable(value = "journalId") int journalId) {
        
        Double avgRating = journalService.getAvgRatingByJournalId(journalId);
        
        if(avgRating == Double.NEGATIVE_INFINITY){
            
            return new ResponseEntity<>(
                    avgRating,
                    HttpStatus.NO_CONTENT
            );
        }else{
            
            return new ResponseEntity<>(
                avgRating,
                HttpStatus.OK);
        }
    }
    
    @GetMapping("/countRating/{journalId}")
    public ResponseEntity<Integer> getCountRatingByJournal(@PathVariable(value = "journalId") int journalId) {
        
        int countRating = journalService.getCountRatingByJournalId(journalId);
        
        if(countRating == Integer.MIN_VALUE){
            
            return new ResponseEntity<>(
                    countRating,
                    HttpStatus.NO_CONTENT
            );
        }else{
            
            return new ResponseEntity<>(
                countRating,
                HttpStatus.OK);
        }
    }
    
    
}
