/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.rating;

import com.ou.ScienctificJournal.SearchCriteria;
import com.ou.ScienctificJournal.pojo.Journal;
import com.ou.ScienctificJournal.pojo.Rating;
import com.ou.ScienctificJournal.pojo.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

/**
 *
 * @author kien
 */
@Service
@Transactional
public class RatingService {
    @Autowired 
    RatingRepository ratingRepository;
    @PersistenceContext
    private EntityManager entityManager;
    
    public Rating getRating(User user, Journal journal){
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rating> query = cb.createQuery(Rating.class);
        Root<Rating> root = query.from(Rating.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        predicates.add(cb.equal(root.get("userId"), user.getId()));
        predicates.add(cb.equal(root.get("journalId"), journal.getId()));
        
        query = query.where(predicates.toArray(Predicate[]::new));
        
        try{
            
            Rating rating = entityManager.createQuery(query).getSingleResult();
            return rating;
        }catch(NoResultException ex){
            return null;
        }
        
    }
    
    public Rating updateRating(Rating rating){
        return this.ratingRepository.save(rating);
    }
    
    public Rating save(User user, Journal journal, int rate){
        Rating rating = new Rating();
        rating.setRating(rate);
        rating.setCreatedDate(new Date());
        rating.setJournalId(journal);
        rating.setUserId(user);
        this.ratingRepository.save(rating);
        
        return rating;
    }
    
    public List<Rating> findListRatingByJournalId(int journalId){
        RatingSpecification spec = new RatingSpecification(new SearchCriteria("journalId", ":", journalId));

        List<Rating> results = this.ratingRepository.findAll(spec);

        return results;
    }
    
}
