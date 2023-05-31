/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.controller;

import com.ou.ScienctificJournal.email.EmailDetails;
import com.ou.ScienctificJournal.email.EmailService;
import com.ou.ScienctificJournal.journal.JournalService;
import com.ou.ScienctificJournal.magazine.MagazineService;
import com.ou.ScienctificJournal.payload.AddJournalToMagazineRequest;
import com.ou.ScienctificJournal.payload.RejectPublishRequest;
import com.ou.ScienctificJournal.payload.UpdateSubmitFormatRequest;
import com.ou.ScienctificJournal.pojo.Journal;
import com.ou.ScienctificJournal.pojo.MagazineNumber;
import com.ou.ScienctificJournal.pojo.User;
import com.ou.ScienctificJournal.services.FileStorageService;
import com.ou.ScienctificJournal.user.UserService;
import java.util.Date;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author kien
 */
@RestController
@RequestMapping("/api")
public class HandleJournalController {

    @Autowired
    private UserService userService;
    @Autowired
    private JournalService journalService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private MagazineService magazineService;

    @GetMapping("/journal/accept-in-table/{journalId}")
    public ResponseEntity<Journal> acceptJournalInTable(@PathVariable(value = "journalId") int journalId) {
        Journal journal = this.journalService.getJournalById(journalId);
        journal.setState("ĐANG CHỜ CHỈ ĐỊNH NGƯỜI PHẢN BIỆN");
        this.journalService.saveJournal(journal);

        return new ResponseEntity<>(
                journal,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/journal/accept-publish/{journalId}")
    public ResponseEntity<Journal> acceptJournalPublish(@PathVariable(value = "journalId") int journalId) {
        Journal journal = this.journalService.getJournalById(journalId);
        journal.setState("CHẤP NHẬN XUẤT BẢN");
        this.journalService.saveJournal(journal);

        return new ResponseEntity<>(
                journal,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/journal/reject-publish/{journalId}")
    public ResponseEntity<Journal> rejectJournalPublish(@PathVariable(value = "journalId") int journalId,
            @RequestBody RejectPublishRequest request) {
        Journal journal = this.journalService.getJournalById(journalId);
        journal.setState("TỪ CHỐI XUẤT BẢN");
        this.journalService.saveJournal(journal);
        try {
            EmailDetails emailDetails = new EmailDetails();
            User u = this.userService.getUserById(journal.getAuthorId().getId());
            emailDetails.setRecipient(u.getEmail());
            emailDetails.setSubject("REJECT Journal from OU!");
            String msgBody = "Bài viết " + journal.getTitle()+ " đã bị từ chối xuất bản!\n";
            msgBody = msgBody.concat("Lý do: " + request.getReason());
            emailDetails.setMsgBody(msgBody);
            emailService.sendSimpleMail(emailDetails);
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }

        return new ResponseEntity<>(
                journal,
                HttpStatus.NO_CONTENT
        );
    }

    @PostMapping("/journal/edit-before-publish/{journalId}")
    public ResponseEntity<Journal> editBeforeJournalPublish(@PathVariable(value = "journalId") int journalId,
            @RequestParam("fileRequest") MultipartFile fileRequest) {

        Journal journal = this.journalService.getJournalById(journalId);
        journal.setState("CHỈNH SỬA TRƯỚC XUẤT BẢN");

        this.journalService.saveJournal(journal);
        MimeMessage mimeMessage
                = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        User user = this.journalService.getJournalById(journalId).getAuthorId();

        try {
            // Setting multipart as true for attachments to
            // be send
            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(user.getEmail());
            String msgBody = "Journal OU sau khi xem xét bài viết bạn đã gửi.\n";
            msgBody = msgBody.concat("Bài viết "+journal.getFileName()+" cần chỉnh sửa trước khi xuất bản!\n");
            msgBody = msgBody.concat("Nội dung cần chỉnh sửa được đính kèm cùng email này.");
            mimeMessageHelper.setText(msgBody);
            mimeMessageHelper.setSubject(
                    "CHỈNH SỬA BÀI VIẾT TRƯỚC KHI XUẤT BẢN");

            mimeMessageHelper.addAttachment(fileRequest.getResource().getFilename(), fileRequest);
            // Sending the mail
            javaMailSender.send(mimeMessage);

        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }

        return new ResponseEntity<>(
                journal,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/journal/reject-in-table/{journalId}")
    public ResponseEntity<Journal> rejectJournalInTable(@PathVariable(value = "journalId") int journalId) {
        Journal journal = this.journalService.getJournalById(journalId);
        journal.setState("TỪ CHỐI TẠI BÀN");
        this.journalService.saveJournal(journal);
        EmailDetails emailDetails = new EmailDetails();
        User u = this.userService.getUserById(journal.getAuthorId().getId());
        emailDetails.setRecipient(u.getEmail());
        emailDetails.setSubject("REJECT Journal from OU!");
        emailDetails.setMsgBody("Bài viết " + journal.getFileName() + " đã bị từ chối tại bàn!");
        emailService.sendSimpleMail(emailDetails);

        return new ResponseEntity<>(
                journal,
                HttpStatus.NO_CONTENT
        );
    }

    
    @PostMapping("/journal/submit-edit-journal-publish/{journalId}")
    public ResponseEntity<Journal> editJournalPublish(@PathVariable(value = "journalId") int journalId,
            @RequestParam("fileRequest") MultipartFile fileRequest) {
        
        //store file
        String fileName = fileStorageService.storeFile(fileRequest);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/downloadFile/")
                .path(fileName)
                .toUriString();

        Journal journal = this.journalService.getJournalById(journalId);
        journal.setFileName(fileName);
        journal.setFileDownloadUrl(fileDownloadUri);
        journal.setLastModified(new Date());
        journal.setState("CHỜ QUYẾT ĐỊNH SAU CHỈNH SỬA");

        this.journalService.saveJournal(journal);

        return new ResponseEntity<>(
                journal,
                HttpStatus.CREATED
        );
    }
    //Save thôi chưa có submit nên không co change status
    @PostMapping("/journal/update-format-journal-publish/{journalId}")
    public ResponseEntity<Journal> updateJournalPublish(@PathVariable(value = "journalId") int journalId,
            @RequestBody UpdateSubmitFormatRequest request) {

        Journal journal = this.journalService.getJournalById(journalId);
        journal.setContentHtml(request.getContentHtml());
        journal.setLastModified(new Date());

        this.journalService.saveJournal(journal);

        return new ResponseEntity<>(
                journal,
                HttpStatus.OK
        );
    }
    //CHỜ TÁC GIẢ XÁC NHẬN CHẤP NHẬN XUẤT BẢN
    @PostMapping("/journal/submit-format-journal-publish/{journalId}")
    public ResponseEntity<Journal> formatJournalPublish(@PathVariable(value = "journalId") int journalId,
            @RequestBody UpdateSubmitFormatRequest request) {

        Journal journal = this.journalService.getJournalById(journalId);
        journal.setContentHtml(request.getContentHtml());
        journal.setLastModified(new Date());
        journal.setState("CHỜ TÁC GIẢ PHẢN HỒI XUẤT BẢN SAU CHỈNH SỬA");

        this.journalService.saveJournal(journal);
        
        try {
            EmailDetails emailDetails = new EmailDetails();
            User u = this.userService.getUserById(journal.getAuthorId().getId());
            emailDetails.setRecipient(u.getEmail());
            emailDetails.setSubject("Formatted your journal, please read and confirm to publish your journal!");
            String msgBody = "Bài viết " + journal.getTitle()+ " được chỉnh sửa!\n";
            msgBody += "Bạn cần đọc và xác nhận bài viết để xuất bản.";
            emailDetails.setMsgBody(msgBody);
            emailService.sendSimpleMail(emailDetails);
        } catch (Exception ex) {
            
            System.out.println(ex.getStackTrace());
        }

        return new ResponseEntity<>(
                journal,
                HttpStatus.OK
        );
    }
    
    //edit thanh lai state  
    @PostMapping("/journal/submit-formatted-journal-publish/{journalId}")
    public ResponseEntity<Journal> formattedJournalPublish(@PathVariable(value = "journalId") int journalId,
            @RequestBody UpdateSubmitFormatRequest request) {

        Journal journal = this.journalService.getJournalById(journalId);
        journal.setContentHtml(request.getContentHtml());
        journal.setLastModified(new Date());
        journal.setState("CHỜ ĐƯA VÀO SỐ TẠP CHÍ");

        this.journalService.saveJournal(journal);

        return new ResponseEntity<>(
                journal,
                HttpStatus.OK
        );
    }
    
//    @PostMapping("/journal/submit-format-journal-publish/{journalId}")
//    public ResponseEntity<Journal> formatJournalPublish(@PathVariable(value = "journalId") int journalId,
//            @RequestParam("fileRequest") MultipartFile fileRequest) {
//        
//        //store file
//        String fileName = fileStorageService.storeFile(fileRequest);
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/api/downloadFile/")
//                .path(fileName)
//                .toUriString();
//
//        Journal journal = this.journalService.getJournalById(journalId);
//        journal.setFileName(fileName);
//        journal.setFileDownloadUrl(fileDownloadUri);
//        journal.setLastModified(new Date());
//        journal.setState("CHỜ ĐƯA VÀO SỐ TẠP CHÍ");
//
//        this.journalService.saveJournal(journal);
//
//        return new ResponseEntity<>(
//                journal,
//                HttpStatus.CREATED
//        );
//    }
    
    //fix -- add journal magazine unpublish
    @PostMapping("/journal/add-journal-magazine-unpublish/{magazineId}")
    public ResponseEntity<Journal> addJournalPublish(@RequestBody AddJournalToMagazineRequest request,
            @PathVariable(value = "magazineId") int magazineId
            ) {
        MagazineNumber magazine = this.magazineService.getMagazineById(magazineId);
        
        Journal journal = this.journalService.getJournalById(request.getJournalId());
        journal.setState("ĐÃ XUẤT BẢN");
        journal.setMagazineId(magazine);
        
        this.journalService.saveJournal(journal);

        return new ResponseEntity<>(
                journal,
                HttpStatus.CREATED
        );
    }
    
    @PostMapping("/journal/remove-journal-magazine-unpublish/{magazineId}")
    public ResponseEntity<Journal> removeJournalPublish(@RequestBody AddJournalToMagazineRequest request,
            @PathVariable(value = "magazineId") int magazineId
            ) {
        
        MagazineNumber magazine = this.magazineService.getMagazineById(magazineId);
        
        Journal journal = this.journalService.getJournalById(request.getJournalId());
        journal.setState("CHỜ ĐƯA VÀO SỐ TẠP CHÍ");
        journal.setMagazineId(null);
        
        this.journalService.saveJournal(journal);

        return new ResponseEntity<>(
                journal,
                HttpStatus.CREATED
        );
    }
}
