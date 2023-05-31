/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.journal;

import com.ou.ScienctificJournal.SearchCriteria;
import com.ou.ScienctificJournal.pojo.Comment;
import com.ou.ScienctificJournal.pojo.Journal;
import com.ou.ScienctificJournal.pojo.Rating;
import com.ou.ScienctificJournal.pojo.User;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author kien
 */
@Service
@Transactional
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;
    
    public Journal saveJournal(Journal j) {
        Journal journal = this.journalRepository.save(j);

        return journal;
    }

    public List<Journal> getJournalByRoleAuthor(String state) {
        JournalSpecification spec
                = new JournalSpecification(new SearchCriteria("state", ":", state));

        List<Journal> results = this.journalRepository.findAll(spec);

        return results;
    }
    

    public List<Journal> getJournalByAuthor(User user) {
        JournalSpecification spec
                = new JournalSpecification(new SearchCriteria("authorId", ":", user));

        List<Journal> results = this.journalRepository.findAll(spec);

        return results;
    }
    

    public Journal getJournalById(int id) {
        try {
            return journalRepository.findById(id).get();
        } catch (Exception e) {
            // do nothing or add action code
            return null;
        }
    }
    
    public Page<Journal> getAllJournal(){
        Pageable firstPageWithTwoElements = PageRequest.of(0, 2);
        
//        Page<Journal> allJournals = journalRepository.findAll(firstPageWithTwoElements);
        
        Page<Journal> allJournals = journalRepository.findAll(firstPageWithTwoElements);
        
        return allJournals;
    }
    
    public Double getAvgRatingByJournalId(int journalId){
        
        double result = Double.NEGATIVE_INFINITY;
        
        Journal journal = this.getJournalById(journalId);
        
        int count = 0;
        double sum = 0;
        
        if(journal != null){
            if(!CollectionUtils.isEmpty(journal.getRatingSet())){
            
                for (Rating rating : journal.getRatingSet()) {

                sum += Double.valueOf(rating.getRating()); 
                count++;
                }

                result = sum / count;
            }
        }
        return result;
    }
    
    public int getCountRatingByJournalId(int journalId){
           
        Journal journal = this.getJournalById(journalId);
        
        int count = 0;
        
        if(journal != null){
            
            count = journal.getRatingSet().size();
        }
        
        return count;
    }
    
}
