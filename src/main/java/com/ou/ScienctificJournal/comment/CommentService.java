/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.comment;

import com.ou.ScienctificJournal.SearchCriteria;
import com.ou.ScienctificJournal.pojo.Comment;
import com.ou.ScienctificJournal.pojo.Journal;
import com.ou.ScienctificJournal.pojo.User;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kien
 */
@Service
@Transactional
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    
    public Comment save(User user, Journal journal, String content){
        Comment comment = new Comment();
        comment.setCreatedDate(new Date());
        comment.setUserId(user);
        comment.setJournalId(journal);
        comment.setContent(content);
        this.commentRepository.save(comment);
        
        return comment;
    }
    
    public List<Comment> findCommentByJournalId(int journalId){
        CommentSpecification spec
                = new CommentSpecification(new SearchCriteria("journalId", ":", journalId));

        List<Comment> results = this.commentRepository.findAll(spec);

        return results;
    }
    
    // method to delete comment
    public boolean deleteComment(Comment comment){
        try {
            
            this.commentRepository.delete(comment);
            return true;
        }catch(Exception ex){
            
            return false;
        }
    }
    
    public Comment getCommentById(int commentId){
        
        Comment commnet = this.commentRepository.findById(commentId).get();
        
        if(commnet != null){
            return commnet;
        } else {
            return null;
        }
    }
}
