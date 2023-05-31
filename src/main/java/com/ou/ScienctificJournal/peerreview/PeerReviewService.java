/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.peerreview;

import com.ou.ScienctificJournal.journal.JournalRepository;
import com.ou.ScienctificJournal.pojo.Journal;
import com.ou.ScienctificJournal.pojo.PeerReview;
import com.ou.ScienctificJournal.pojo.User;
import com.ou.ScienctificJournal.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

/**
 *
 * @author kien
 */
@Service
@Transactional
public class PeerReviewService {

    @Autowired
    private PeerReviewRepository peerReviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JournalRepository journalRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public PeerReview savePeerReview(PeerReview p) {
        return this.peerReviewRepository.save(p);
    }

    public PeerReview getPeerReviewById(int peerReviewId) {
        try {
            return peerReviewRepository.findById(peerReviewId).get();
        } catch (Exception e) {
            // do nothing or add action code
            return null;
        }
    }

    public List<PeerReview> getPeerReviewUnreviewByReviewer(User reviewer) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PeerReview> query = cb.createQuery(PeerReview.class);
        Root<PeerReview> peerReview = query.from(PeerReview.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(peerReview.get("reviewerId"), reviewer.getId()));
        predicates.add(cb.isNull(peerReview.get("recommendation")));

        query = query.where(predicates.toArray(Predicate[]::new));

        return entityManager.createQuery(query).getResultList();
    }
    
    public List<Object> getPeerReviewReviewed() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<PeerReview> peerReviewRoot = query.from(PeerReview.class);
        Root<Journal> journalRoot = query.from(Journal.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(peerReviewRoot.get("journalId"), journalRoot.get("id")));
        predicates.add(cb.equal(journalRoot.get("state"), "ĐANG CHỜ QUYẾT ĐỊNH"));
        predicates.add(cb.isNotNull(peerReviewRoot.get("recommendation")));
//        predicates.add()

        query.where(predicates.toArray(Predicate[]::new));
        query.groupBy(journalRoot.get("id"));
        
        query.multiselect(peerReviewRoot.get("id"), peerReviewRoot.get("report"),
                peerReviewRoot.get("recommendation"), 
                journalRoot.get("title"), journalRoot.get("fileName"), journalRoot.get("fileDownloadUrl"),
                journalRoot.get("state"), journalRoot.get("type"), journalRoot.get("description"), journalRoot.get("id"));

        return entityManager.createQuery(query).getResultList();
    }

    public List<Object> getPeerReviewUnreviewByReviewerCustome(User reviewer) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<PeerReview> peerReviewRoot = query.from(PeerReview.class);
        Root<Journal> journalRoot = query.from(Journal.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(peerReviewRoot.get("journalId"), journalRoot.get("id")));
        predicates.add(cb.equal(peerReviewRoot.get("reviewerId"), reviewer.getId()));
        predicates.add(cb.isNull(peerReviewRoot.get("recommendation")));

        query.where(predicates.toArray(Predicate[]::new));
        
        query.multiselect(peerReviewRoot.get("id"), peerReviewRoot.get("report"),
                peerReviewRoot.get("recommendation"), 
                journalRoot.get("title"), journalRoot.get("fileName"), journalRoot.get("fileDownloadUrl"),
                journalRoot.get("state"), journalRoot.get("type"), journalRoot.get("description"), journalRoot.get("id"));

        return entityManager.createQuery(query).getResultList();
    }
    
    public List<PeerReview> getAllPeerReviewUnreview() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PeerReview> query = cb.createQuery(PeerReview.class);
        Root<PeerReview> peerReview = query.from(PeerReview.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isNull(peerReview.get("recommendation")));

        query = query.where(predicates.toArray(Predicate[]::new));

        return entityManager.createQuery(query).getResultList();
    }
}
