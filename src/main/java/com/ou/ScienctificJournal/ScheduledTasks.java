/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal;

import com.ou.ScienctificJournal.email.EmailDetails;
import com.ou.ScienctificJournal.email.EmailService;
import com.ou.ScienctificJournal.peerreview.PeerReviewService;
import com.ou.ScienctificJournal.pojo.PeerReview;
import com.ou.ScienctificJournal.pojo.User;
import com.ou.ScienctificJournal.user.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author kien
 */
@Component
public class ScheduledTasks {

    @Autowired
    private PeerReviewService peerReviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @Scheduled(fixedRate = 84600)
    public void scheduleTaskWithFixedRate() {
        // call send email method here

    }

    public void scheduleTaskWithFixedDelay() {
    }

    public void scheduleTaskWithInitialDelay() {
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleTaskWithCronExpression() {
        List<PeerReview> listPeerReviewUnreview = this.peerReviewService.getAllPeerReviewUnreview();

        for (PeerReview peerReview : listPeerReviewUnreview) {
            User user = this.userService.getUserById(peerReview.getReviewerId().getId());

            String emailReviewer = user.getEmail();
            try {
                EmailDetails emailDetails = new EmailDetails();
                emailDetails.setRecipient(user.getEmail());
                emailDetails.setSubject("Remind peer review!");
                String msgBody = "Bài viết " + peerReview.getJournalId().getTitle() + " đang đợi review!\n";
                emailDetails.setMsgBody(msgBody);
                emailService.sendSimpleMail(emailDetails);
            } catch (Exception ex) {
                System.out.println(ex.getStackTrace());
            }
        }
    }
}
