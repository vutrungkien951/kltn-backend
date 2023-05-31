/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kien
 */
@Entity
@Table(name = "journal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Journal.findAll", query = "SELECT j FROM Journal j"),
    @NamedQuery(name = "Journal.findById", query = "SELECT j FROM Journal j WHERE j.id = :id"),
    @NamedQuery(name = "Journal.findByTitle", query = "SELECT j FROM Journal j WHERE j.title = :title"),
    @NamedQuery(name = "Journal.findByDescription", query = "SELECT j FROM Journal j WHERE j.description = :description"),
    @NamedQuery(name = "Journal.findByFileName", query = "SELECT j FROM Journal j WHERE j.fileName = :fileName"),
    @NamedQuery(name = "Journal.findByFileVersion", query = "SELECT j FROM Journal j WHERE j.fileVersion = :fileVersion"),
    @NamedQuery(name = "Journal.findByFileDownloadUrl", query = "SELECT j FROM Journal j WHERE j.fileDownloadUrl = :fileDownloadUrl"),
    @NamedQuery(name = "Journal.findByState", query = "SELECT j FROM Journal j WHERE j.state = :state"),
    @NamedQuery(name = "Journal.findByType", query = "SELECT j FROM Journal j WHERE j.type = :type"),
    @NamedQuery(name = "Journal.findByCreatedDate", query = "SELECT j FROM Journal j WHERE j.createdDate = :createdDate"),
    @NamedQuery(name = "Journal.findByLastModified", query = "SELECT j FROM Journal j WHERE j.lastModified = :lastModified"),
    @NamedQuery(name = "Journal.findByListAuthor", query = "SELECT j FROM Journal j WHERE j.listAuthor = :listAuthor"),
    @NamedQuery(name = "Journal.findByListKeyword", query = "SELECT j FROM Journal j WHERE j.listKeyword = :listKeyword"),
    @NamedQuery(name = "Journal.findByEmail", query = "SELECT j FROM Journal j WHERE j.email = :email"),
    @NamedQuery(name = "Journal.findByOrganization", query = "SELECT j FROM Journal j WHERE j.organization = :organization"),
    @NamedQuery(name = "Journal.findByContentHtml", query = "SELECT j FROM Journal j WHERE j.contentHtml = :contentHtml")})
public class Journal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 125)
    @Column(name = "title")
    private String title;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Size(max = 125)
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_version")
    private Integer fileVersion;
    @Size(max = 255)
    @Column(name = "file_download_url")
    private String fileDownloadUrl;
    @Size(max = 45)
    @Column(name = "state")
    private String state;
    @Size(max = 125)
    @Column(name = "type")
    private String type;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @Size(max = 255)
    @Column(name = "list_author")
    private String listAuthor;
    @Size(max = 255)
    @Column(name = "list_keyword")
    private String listKeyword;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 125)
    @Column(name = "email")
    private String email;
    @Size(max = 125)
    @Column(name = "organization")
    private String organization;
    @Size(max = 15000)
    @Column(name = "content_html")
    private String contentHtml;
    @JoinColumn(name = "magazine_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private MagazineNumber magazineId;
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private User authorId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "journalId")
    private Set<Rating> ratingSet;
    @OneToMany(mappedBy = "journalId")
    private Set<PeerReview> peerReviewSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "journalId")
    private Set<Comment> commentSet;

    public Journal() {
    }

    public Journal(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(Integer fileVersion) {
        this.fileVersion = fileVersion;
    }

    public String getFileDownloadUrl() {
        return fileDownloadUrl;
    }

    public void setFileDownloadUrl(String fileDownloadUrl) {
        this.fileDownloadUrl = fileDownloadUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getListAuthor() {
        return listAuthor;
    }

    public void setListAuthor(String listAuthor) {
        this.listAuthor = listAuthor;
    }

    public String getListKeyword() {
        return listKeyword;
    }

    public void setListKeyword(String listKeyword) {
        this.listKeyword = listKeyword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public MagazineNumber getMagazineId() {
        return magazineId;
    }

    public void setMagazineId(MagazineNumber magazineId) {
        this.magazineId = magazineId;
    }

    public User getAuthorId() {
        return authorId;
    }

    public void setAuthorId(User authorId) {
        this.authorId = authorId;
    }

    @XmlTransient
    public Set<Rating> getRatingSet() {
        return ratingSet;
    }

    public void setRatingSet(Set<Rating> ratingSet) {
        this.ratingSet = ratingSet;
    }

    @XmlTransient
    public Set<PeerReview> getPeerReviewSet() {
        return peerReviewSet;
    }

    public void setPeerReviewSet(Set<PeerReview> peerReviewSet) {
        this.peerReviewSet = peerReviewSet;
    }

    @XmlTransient
    public Set<Comment> getCommentSet() {
        return commentSet;
    }

    public void setCommentSet(Set<Comment> commentSet) {
        this.commentSet = commentSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Journal)) {
            return false;
        }
        Journal other = (Journal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ou.ScienctificJournal.pojo.Journal[ id=" + id + " ]";
    }
    
}
