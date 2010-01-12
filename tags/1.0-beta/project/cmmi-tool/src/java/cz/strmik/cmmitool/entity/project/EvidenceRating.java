/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.entity.project;

import cz.strmik.cmmitool.entity.*;
import cz.strmik.cmmitool.entity.method.RatingScale;
import cz.strmik.cmmitool.entity.model.Practice;
import cz.strmik.cmmitool.enums.PracticeEvidenceAdequacy;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Entity
public class EvidenceRating implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional=false)
    private Project project;

    @ManyToOne(optional=false)
    private Practice practice;

    @ManyToOne(optional=false)
    private ProcessInstantiation processInstantiation;

    @Enumerated(EnumType.STRING)
    private PracticeEvidenceAdequacy evidenceAdequacy;

    @ManyToOne
    private RatingScale characterizePracticeImplementation;

    @ManyToOne
    private User modifiedBy;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date modifiedTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RatingScale getCharacterizePracticeImplementation() {
        return characterizePracticeImplementation;
    }

    public void setCharacterizePracticeImplementation(RatingScale characterizePracticeImplementation) {
        this.characterizePracticeImplementation = characterizePracticeImplementation;
    }

    public PracticeEvidenceAdequacy getEvidenceAdequacy() {
        return evidenceAdequacy;
    }

    public void setEvidenceAdequacy(PracticeEvidenceAdequacy evidenceAdequacy) {
        this.evidenceAdequacy = evidenceAdequacy;
    }

    public Practice getPractice() {
        return practice;
    }

    public void setPractice(Practice practice) {
        this.practice = practice;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public void setModifiedTimestamp(Date modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    public ProcessInstantiation getProcessInstantiation() {
        return processInstantiation;
    }

    public void setProcessInstantiation(ProcessInstantiation processInstantiation) {
        this.processInstantiation = processInstantiation;
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
        if (!(object instanceof EvidenceRating)) {
            return false;
        }
        EvidenceRating other = (EvidenceRating) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cz.strmik.cmmitool.entity.EvidenceRating[id=" + id + "]";
    }

    @PreUpdate
    @PrePersist
    protected void updateTimestamp() {
        this.modifiedTimestamp = new Date();
    }

}
