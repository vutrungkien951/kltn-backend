/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.magazine;

import com.ou.ScienctificJournal.pojo.MagazineNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kien
 */
@Repository
public interface MagazineRepository extends JpaRepository<MagazineNumber, Integer>, JpaSpecificationExecutor<MagazineNumber>{
    
}
