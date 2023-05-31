/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.magazine;

import com.ou.ScienctificJournal.pojo.MagazineNumber;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kien
 */
@Service
@Transactional
public class MagazineService {

    @Autowired
    private MagazineRepository magazineRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public MagazineNumber save(MagazineNumber magazine) {
        return this.magazineRepository.save(magazine);
    }

    public MagazineNumber getMagazineById(int id) {
        try {
            return magazineRepository.findById(id).get();
        } catch (Exception e) {
            // do nothing or add action code
            return null;
        }
    }

    public List<MagazineNumber> getMagazineNumberUnpublished() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MagazineNumber> query = cb.createQuery(MagazineNumber.class);
        Root<MagazineNumber> root = query.from(MagazineNumber.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isNull(root.get("releaseDate")));

        query = query.where(predicates.toArray(Predicate[]::new));

        return entityManager.createQuery(query).getResultList();
    }

    public List<MagazineNumber> getMagazineNumberPublished() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MagazineNumber> query = cb.createQuery(MagazineNumber.class);
        Root<MagazineNumber> root = query.from(MagazineNumber.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isNotNull(root.get("releaseDate")));

        query = query.where(predicates.toArray(Predicate[]::new));

        return entityManager.createQuery(query).getResultList();
    }
    
    public List<MagazineNumber> getMagazineNumberReleased() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MagazineNumber> query = cb.createQuery(MagazineNumber.class);
        Root<MagazineNumber> root = query.from(MagazineNumber.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.lessThanOrEqualTo(root.get("releaseDate"), new Date()));

        query = query.where(predicates.toArray(Predicate[]::new));
        query = query.orderBy(cb.desc(root.get("releaseDate")));

        return entityManager.createQuery(query).getResultList();
    }
}
