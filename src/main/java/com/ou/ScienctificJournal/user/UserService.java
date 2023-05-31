/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.user;

import com.ou.ScienctificJournal.SearchCriteria;
import com.ou.ScienctificJournal.payload.SignUpRequest;
import com.ou.ScienctificJournal.pojo.PeerReview;
import com.ou.ScienctificJournal.pojo.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author kien
 */
@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PersistenceContext
    private EntityManager entityManager;
    Logger logger = LoggerFactory.getLogger(UserService.class);


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomerUserDetails(user);
    }
    
    @Transactional
    public UserDetails loadUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return new CustomerUserDetails(user);
    }
    
    @Transactional
    public User createUser(SignUpRequest signUpRequest){
        
        if (this.checkUserByUsername(signUpRequest.getUsername()).size() > 0){
            
            return null;
        }
        
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setUserRole("ROLE_AUTHOR");
        user.setDegree(signUpRequest.getDegree());
        user.setScientificTitle(signUpRequest.getScientificTitle());
        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setPhoneNumber(signUpRequest.getPhone());
        user.setWorkPlace(signUpRequest.getWorkPlace());
        user.setActive(true);
        
        try{
            
            user = userRepository.save(user);
        }catch(Exception ex) {
            
            System.out.println(ex.getMessage());
            return null;
        }

        return user;
    }
    
    @Transactional
    public User saveUser(User user){
        User u = this.userRepository.save(user);
        
        return u;
    }
    
    public User getUserByUsername(String username){
        try{
            
            User user = userRepository.findByUsername(username);
            
            return user;
        } catch(Exception ex){
            
            return null;
        }
       
    }
    
    public List<User> checkUserByUsername(String username){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("username"), username));
        query = query.where(predicates.toArray(Predicate[]::new));
        
        return entityManager.createQuery(query).getResultList();
    }
    
    public List<User> getAllUsers(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.notEqual(root.get("userRole"), "ROLE_ADMIN"));
        query = query.where(predicates.toArray(Predicate[]::new));
        
        
        return entityManager.createQuery(query).getResultList();
    }
    
    public List<User> getAllPeerReviewer(){
        UserSpecification spec = new UserSpecification(new SearchCriteria("userRole", ":", "ROLE_REVIEWER"));
        
        List<User> users = this.userRepository.findAll(spec);
        
        return users;
    }
    
    public User getUserById(int id){
        return userRepository.findById(id).get();
    }
    
}
