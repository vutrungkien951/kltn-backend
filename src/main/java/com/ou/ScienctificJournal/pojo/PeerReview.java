/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kien
 */
@Entity
@Table(name = "peer_review")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PeerReview.findAll", query = "SELECT p FROM PeerReview p"),
    @NamedQuery(name = "PeerReview.findById", query = "SELECT p FROM PeerReview p WHERE p.id = :id"),
    @NamedQuery(name = "PeerReview.findByStartedDate", query = "SELECT p FROM PeerReview p WHERE p.startedDate = :startedDate"),
    @NamedQuery(name = "PeerReview.findByReport", query = "SELECT p FROM PeerReview p WHERE p.report = :report"),
    @NamedQuery(name = "PeerReview.findByRecommendation", query = "SELECT p FROM PeerReview p WHERE p.recommendation = :recommendation")})
public class PeerReview implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "started_date")
    @Temporal(TemporalType.DATE)
    private Date startedDate;
    @Size(max = 255)
    @Column(name = "report")
    private String report;
    @Size(max = 100)
    @Column(name = "recommendation")
    private String recommendation;
    @JoinColumn(name = "journal_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Journal journalId;
    @JoinColumn(name = "reviewer_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private User reviewerId;

    public PeerReview() {
    }

    public PeerReview(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recomendation) {
        this.recommendation = recomendation;
    }

    public Journal getJournalId() {
        return journalId;
    }

    public void setJournalId(Journal journalId) {
        this.journalId = journalId;
    }

    public User getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(User reviewerId) {
        this.reviewerId = reviewerId;
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
        if (!(object instanceof PeerReview)) {
            return false;
        }
        PeerReview other = (PeerReview) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ou.ScienctificJournal.pojo.PeerReview[ id=" + id + " ]";
    }
    
}
