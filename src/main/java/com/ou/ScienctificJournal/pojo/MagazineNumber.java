/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ScienctificJournal.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "magazine_number")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MagazineNumber.findAll", query = "SELECT m FROM MagazineNumber m"),
    @NamedQuery(name = "MagazineNumber.findById", query = "SELECT m FROM MagazineNumber m WHERE m.id = :id"),
    @NamedQuery(name = "MagazineNumber.findByMagazineNumberName", query = "SELECT m FROM MagazineNumber m WHERE m.magazineNumberName = :magazineNumberName"),
    @NamedQuery(name = "MagazineNumber.findByReleaseDate", query = "SELECT m FROM MagazineNumber m WHERE m.releaseDate = :releaseDate")})
public class MagazineNumber implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "magazine_number_name")
    private String magazineNumberName;
    @Column(name = "release_date")
    @Temporal(TemporalType.DATE)
    private Date releaseDate;
    @OneToMany(mappedBy = "magazineId")
    private Set<Journal> journalSet;

    public MagazineNumber() {
    }

    public MagazineNumber(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMagazineNumberName() {
        return magazineNumberName;
    }

    public void setMagazineNumberName(String magazineNumberName) {
        this.magazineNumberName = magazineNumberName;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @XmlTransient
    public Set<Journal> getJournalSet() {
        return journalSet;
    }

    public void setJournalSet(Set<Journal> journalSet) {
        this.journalSet = journalSet;
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
        if (!(object instanceof MagazineNumber)) {
            return false;
        }
        MagazineNumber other = (MagazineNumber) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ou.ScienctificJournal.pojo.MagazineNumber[ id=" + id + " ]";
    }
    
}
