/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.controller;

import com.ou.ScienctificJournal.magazine.MagazineService;
import com.ou.ScienctificJournal.payload.MagazineNumberCreateRequest;
import com.ou.ScienctificJournal.payload.PublishMagazineRequest;
import com.ou.ScienctificJournal.pojo.MagazineNumber;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.GetMapping;
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
public class MagazineController {
    @Autowired
    private MagazineService magazineService;
    
    @PostMapping("/create-magazine-number")
    public ResponseEntity<MagazineNumber> createMagazineNumber(
            @RequestBody MagazineNumberCreateRequest request){
        MagazineNumber newMagazine = new MagazineNumber();
        newMagazine.setMagazineNumberName(request.getMagazineNumberName());
        newMagazine = this.magazineService.save(newMagazine);
        
        return new ResponseEntity<>(
                newMagazine,
                HttpStatus.CREATED
        );
    }
    
    @GetMapping("/get-magazine-number-unpublished")
    public ResponseEntity<List<MagazineNumber>> getAllMagazineNumberUnpublished(){
        
        return new ResponseEntity<>(
                this.magazineService.getMagazineNumberUnpublished(),
                HttpStatus.CREATED
        );
    }
    
    @GetMapping("/get-magazine-number-published")
    public ResponseEntity<List<MagazineNumber>> getAllMagazineNumberPublished(){
        
        return new ResponseEntity<>(
                this.magazineService.getMagazineNumberPublished(),
                HttpStatus.CREATED
        );
    }
    
    @GetMapping("/get-magazine-number-released")
    public ResponseEntity<List<MagazineNumber>> getAllMagazineNumberHasPublished(){
        
        return new ResponseEntity<>(
                this.magazineService.getMagazineNumberReleased(),
                HttpStatus.OK
        );
    }
    
    @GetMapping("/get-magazine-number-released-pagination")
    public Page<MagazineNumber> getAllMagazineNumberHasPublishedPagination(@RequestParam Optional<Integer> page){
        
        List<MagazineNumber> list = this.magazineService.getMagazineNumberReleased();
        
        //1. PageListHolder
        PagedListHolder<MagazineNumber> pageListHolder = new PagedListHolder<MagazineNumber>(list);
        pageListHolder.setPage(page.orElse(0));
        pageListHolder.setPageSize(1);
        //2. PropertyComparator
        List<MagazineNumber> pageSlice = pageListHolder.getPageList();
        boolean ascending = true;
        PropertyComparator.sort(pageSlice, new MutableSortDefinition());
        
        Pageable pageable = PageRequest.of(page.orElse(0), 1);
        
        Page<MagazineNumber> pageMagazine = new PageImpl<>(pageSlice, pageable, list.size());
        
        return pageMagazine;
    }
    
    @PostMapping("/publish-magazine-number")
    public ResponseEntity<MagazineNumber> publishMagazine(@RequestBody PublishMagazineRequest request) throws ParseException{
        MagazineNumber magazine = this.magazineService.getMagazineById(request.getMagazineId());
        Date date =new SimpleDateFormat("yyyy-MM-dd").parse(request.getDateString());  
        magazine.setReleaseDate(date);
        magazine = magazineService.save(magazine);
        
        return new ResponseEntity<>(
                magazine,
                HttpStatus.CREATED
        );
    }
}
