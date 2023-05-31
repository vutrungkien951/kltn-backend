/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.controller;

import com.ou.ScienctificJournal.journal.JournalRepository;
import com.ou.ScienctificJournal.journal.JournalService;
import com.ou.ScienctificJournal.payload.CheckUsernameRequest;
import com.ou.ScienctificJournal.payload.CreateUserRequest;
import com.ou.ScienctificJournal.payload.UpdatePasswordRequest;
import com.ou.ScienctificJournal.pojo.Journal;
import com.ou.ScienctificJournal.pojo.User;
import com.ou.ScienctificJournal.user.UserService;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
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
public class ManagementController {

    @Autowired
    private UserService userService;
    @Autowired
    private JournalService journalService;
    @Autowired
    private JournalRepository journalRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser() {

        return new ResponseEntity<>(
                this.userService.getAllUsers(),
                HttpStatus.OK
        );
    }
    
   @GetMapping("/users-pagination")
   public ResponseEntity<Page<User>> getAllUserPagination(@RequestParam Optional<Integer> page){
        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.getUserByUsername(authen.getName());
       
        
        if(user == null ){
            return new ResponseEntity<>(
                    null,
                    HttpStatus.NO_CONTENT
            );
        }
        if(!"ROLE_ADMIN".equals(user.getUserRole())){
            return new ResponseEntity<>(
                    null,
                    HttpStatus.NO_CONTENT
            );
        }
        List<User> list = this.userService.getAllUsers();
        
         //1. PageListHolder
        PagedListHolder<User> pageListHolder = new PagedListHolder<User>(list);
        pageListHolder.setPage(page.orElse(0));
        pageListHolder.setPageSize(5);
        //2. PropertyComparator
        List<User> pageSlice = pageListHolder.getPageList();
        boolean ascending = true;
        PropertyComparator.sort(pageSlice, new MutableSortDefinition());
        
        Pageable pageable = PageRequest.of(page.orElse(0), 5);
        
        Page<User> listSlice = new PageImpl<>(pageSlice, pageable, list.size());

        return new ResponseEntity<>(
                    listSlice,
                    HttpStatus.OK
            );
   }
    
    @GetMapping("/journals")
    public Page<Journal> getAllJournals(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy
    ) {

        return this.journalRepository.findAll(PageRequest.of(page.orElse(0), 5, Sort.Direction.ASC, sortBy.orElse("id")));
    }

    @GetMapping("/users/peer-reviewer")
    public ResponseEntity<List<User>> getAllPeerReviewer() {

        return new ResponseEntity<>(
                this.userService.getAllPeerReviewer(),
                HttpStatus.OK
        );
    }

    @PostMapping("/create-user-role")
    public ResponseEntity<User> createUserRole(@RequestBody @Validated CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setDegree(request.getDegree());
        user.setScientificTitle(request.getScientificTitle());
        user.setWorkPlace(request.getWorkPlace());
        user.setPhoneNumber(request.getPhone());
        user.setUserRole(request.getUserRole());
        user.setActive(true);

        User u = this.userService.saveUser(user);

        return new ResponseEntity<>(
                u,
                HttpStatus.OK
        );
    }

    @GetMapping("/journal/{journalId}")
    public ResponseEntity<Journal> getJournalInfo(@PathVariable(value = "journalId") int journalId) {
        Journal j = this.journalService.getJournalById(journalId);

        return new ResponseEntity<>(
                j,
                HttpStatus.OK
        );
    }

    @GetMapping("/journal-waiting-handle")
    public ResponseEntity<List<Journal>> getAllJournalWatingHandle() {

        return new ResponseEntity<>(
                this.journalService.getJournalByRoleAuthor("ĐANG CHỜ XỬ LÝ"),
                HttpStatus.OK
        );
    }
    
    @GetMapping("/journal-waiting-handle-pagination")
    public Page<Journal> getAllJournalWatingHandlePagination(@RequestParam Optional<Integer> page) {
        
        List<Journal> list = this.journalService.getJournalByRoleAuthor("ĐANG CHỜ XỬ LÝ");
        
        //1. PageListHolder
        PagedListHolder<Journal> pageListHolder = new PagedListHolder<Journal>(list);
        pageListHolder.setPage(page.orElse(0));
        pageListHolder.setPageSize(5);
        //2. PropertyComparator
        List<Journal> pageSlice = pageListHolder.getPageList();
        boolean ascending = true;
        PropertyComparator.sort(pageSlice, new MutableSortDefinition());
        
        Pageable pageable = PageRequest.of(page.orElse(0), 5);
        
        Page<Journal> pageJournal = new PageImpl<>(pageSlice, pageable, list.size());

        return pageJournal;
    }

    @GetMapping("/journal-waiting-spec")
    public ResponseEntity<List<Journal>> getAllJournalWatingSpec() {

        return new ResponseEntity<>(
                this.journalService.getJournalByRoleAuthor("ĐANG CHỜ CHỈ ĐỊNH NGƯỜI PHẢN BIỆN"),
                HttpStatus.OK
        );
    }
    
    @GetMapping("/journal-waiting-spec-pagination")
    public Page<Journal> getAllJournalWatingSpecPagination(@RequestParam Optional<Integer> page) {
        
        List<Journal> list = this.journalService.getJournalByRoleAuthor("ĐANG CHỜ CHỈ ĐỊNH NGƯỜI PHẢN BIỆN");
        
        //1. PageListHolder
        PagedListHolder<Journal> pageListHolder = new PagedListHolder<Journal>(list);
        pageListHolder.setPage(page.orElse(0));
        pageListHolder.setPageSize(5);
        //2. PropertyComparator
        List<Journal> pageSlice = pageListHolder.getPageList();
        boolean ascending = true;
        PropertyComparator.sort(pageSlice, new MutableSortDefinition());
        
        Pageable pageable = PageRequest.of(page.orElse(0), 5);
        
        Page<Journal> pageJournal = new PageImpl<>(pageSlice, pageable, list.size());

        return pageJournal;
    }

    @GetMapping("/journal-waiting-decide")
    public ResponseEntity<List<Journal>> getAllJournalWatingDecide() {

        return new ResponseEntity<>(
                this.journalService.getJournalByRoleAuthor("ĐANG CHỜ QUYẾT ĐỊNH"),
                HttpStatus.OK
        );
    }
    
     @GetMapping("/journal-waiting-decide-pagination")
    public Page<Journal> getAllJournalWatingDecidePagination(@RequestParam Optional<Integer> page) {
        
        List<Journal> list = this.journalService.getJournalByRoleAuthor("ĐANG CHỜ QUYẾT ĐỊNH");
        
        //1. PageListHolder
        PagedListHolder<Journal> pageListHolder = new PagedListHolder<Journal>(list);
        pageListHolder.setPage(page.orElse(0));
        pageListHolder.setPageSize(5);
        //2. PropertyComparator
        List<Journal> pageSlice = pageListHolder.getPageList();
        boolean ascending = true;
        PropertyComparator.sort(pageSlice, new MutableSortDefinition());
        
        Pageable pageable = PageRequest.of(page.orElse(0), 5);
        
        Page<Journal> pageJournal = new PageImpl<>(pageSlice, pageable, list.size());

        return pageJournal;
    }

    @GetMapping("/journal-waiting-decide-after-edit")
    public ResponseEntity<List<Journal>> getAllJournalWatingDecideAfterEdit() {

        return new ResponseEntity<>(
                this.journalService.getJournalByRoleAuthor("CHỜ QUYẾT ĐỊNH SAU CHỈNH SỬA"),
                HttpStatus.OK
        );
    }
    
    @GetMapping("/journal-waiting-decide-after-edit-pagination")
    public Page<Journal> getAllJournalWatingDecideAfterEditPagination(@RequestParam Optional<Integer> page) {
        
        List<Journal> list = this.journalService.getJournalByRoleAuthor("CHỜ QUYẾT ĐỊNH SAU CHỈNH SỬA");
        
        //1. PageListHolder
        PagedListHolder<Journal> pageListHolder = new PagedListHolder<Journal>(list);
        pageListHolder.setPage(page.orElse(0));
        pageListHolder.setPageSize(5);
        //2. PropertyComparator
        List<Journal> pageSlice = pageListHolder.getPageList();
        boolean ascending = true;
        PropertyComparator.sort(pageSlice, new MutableSortDefinition());
        
        Pageable pageable = PageRequest.of(page.orElse(0), 5);
        
        Page<Journal> pageJournal = new PageImpl<>(pageSlice, pageable, list.size());

        return pageJournal;
    }

    @GetMapping("/journal-waiting-format")
    public ResponseEntity<List<Journal>> getAllJournalWatingFormat() {

        return new ResponseEntity<>(
                this.journalService.getJournalByRoleAuthor("CHẤP NHẬN XUẤT BẢN"),
                HttpStatus.OK
        );
    }
    
    @GetMapping("/journal-waiting-format-pagination")
    public Page<Journal> getAllJournalWatingFormatPagination(@RequestParam Optional<Integer> page) {
        
        List<Journal> list = this.journalService.getJournalByRoleAuthor("CHẤP NHẬN XUẤT BẢN");
        
        //1. PageListHolder
        PagedListHolder<Journal> pageListHolder = new PagedListHolder<Journal>(list);
        pageListHolder.setPage(page.orElse(0));
        pageListHolder.setPageSize(5);
        //2. PropertyComparator
        List<Journal> pageSlice = pageListHolder.getPageList();
        boolean ascending = true;
        PropertyComparator.sort(pageSlice, new MutableSortDefinition());
        
        Pageable pageable = PageRequest.of(page.orElse(0), 5);
        
        Page<Journal> pageJournal = new PageImpl<>(pageSlice, pageable, list.size());

        return pageJournal;
    }

    @GetMapping("/journal-waiting-add-magazine")
    public ResponseEntity<List<Journal>> getAllJournalWatingAddMagazine() {

        return new ResponseEntity<>(
                this.journalService.getJournalByRoleAuthor("CHỜ ĐƯA VÀO SỐ TẠP CHÍ"),
                HttpStatus.OK
        );
    }
    
    @GetMapping("/journal-waiting-add-magazine-pagination")
    public Page<Journal> getAllJournalWatingAddMagazinePagination(@RequestParam Optional<Integer> page) {
        
        List<Journal> list = this.journalService.getJournalByRoleAuthor("CHỜ ĐƯA VÀO SỐ TẠP CHÍ");
        
        //1. PageListHolder
        PagedListHolder<Journal> pageListHolder = new PagedListHolder<Journal>(list);
        pageListHolder.setPage(page.orElse(0));
        pageListHolder.setPageSize(5);
        //2. PropertyComparator
        List<Journal> pageSlice = pageListHolder.getPageList();
        boolean ascending = true;
        PropertyComparator.sort(pageSlice, new MutableSortDefinition());
        
        Pageable pageable = PageRequest.of(page.orElse(0), 5);
        
        Page<Journal> pageJournal = new PageImpl<>(pageSlice, pageable, list.size());

        return pageJournal;
    }

    @GetMapping("/author-journal")
    public ResponseEntity<List<Journal>> getAllAuthorJournal() {
        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.getUserByUsername(authen.getName());

        return new ResponseEntity<>(
                this.journalService.getJournalByAuthor(user),
                HttpStatus.OK
        );
    }
    
    @GetMapping("/author-journal-pagination")
    public Page<Journal> getAllAuthorJournalPagination(@RequestParam Optional<Integer> page) {
        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.getUserByUsername(authen.getName());
        
        List<Journal> list = this.journalService.getJournalByAuthor(user);
        
        //1. PageListHolder
        PagedListHolder<Journal> pageListHolder = new PagedListHolder<Journal>(list);
        pageListHolder.setPage(page.orElse(0));
        pageListHolder.setPageSize(5);
        //2. PropertyComparator
        List<Journal> pageSlice = pageListHolder.getPageList();
        boolean ascending = true;
        PropertyComparator.sort(pageSlice, new MutableSortDefinition());
        
        Pageable pageable = PageRequest.of(page.orElse(0), 5);
        
        Page<Journal> pageJournal = new PageImpl<>(pageSlice, pageable, list.size());

        return pageJournal;
    }

    @GetMapping("/get-user-by-id/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "userId") int userId) {

        User u = this.userService.getUserById(userId);

        return new ResponseEntity<>(
                u,
                HttpStatus.OK
        );
    }

    @PostMapping("/checkUsernameExist")
    public ResponseEntity<List<User>> checkUsernameExist(@RequestBody CheckUsernameRequest request) {
        List<User> users = userService.checkUserByUsername(request.getUsername());
        if (users.isEmpty()) {
            return new ResponseEntity<>(
                    users,
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    users,
                    HttpStatus.ACCEPTED
            );
        }

    }

    @PostMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest request) {
        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.getUserByUsername(authen.getName());

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        this.userService.saveUser(user);

        return new ResponseEntity<>(
                    "Change password successfully!",
                    HttpStatus.OK
        );

    }
}
