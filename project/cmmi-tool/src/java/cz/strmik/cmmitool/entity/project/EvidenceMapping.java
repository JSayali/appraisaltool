/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.entity.project;

import cz.strmik.cmmitool.entity.model.Practice;
import cz.strmik.cmmitool.enums.EvidenceCharacteristic;
import cz.strmik.cmmitool.enums.IndicatorType;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Entity
@NamedQueries({
    @NamedQuery(name="EvidenceMapping.findByProjectPracticeInstantiation", query="SELECT m FROM EvidenceMapping m WHERE m.project = :project AND m.practice = :practice AND m.processInstantiation = :processInstantiation ORDER BY m.evidence.name")
})
public class EvidenceMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private ProcessInstantiation processInstantiation;
    @ManyToOne
    private Practice practice;
    @ManyToOne(optional=false)
    private Project project;

    @ManyToOne
    private Evidence evidence;


    @Enumerated(EnumType.STRING)
    private IndicatorType indicatorType;
    @Enumerated(EnumType.STRING)
    private EvidenceCharacteristic characteristic;

    private String comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Evidence getEvidence() {
        return evidence;
    }

    public void setEvidence(Evidence evidence) {
        this.evidence = evidence;
    }

    public IndicatorType getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(IndicatorType indicatorType) {
        this.indicatorType = indicatorType;
    }

    public Practice getPractice() {
        return practice;
    }

    public void setPractice(Practice practice) {
        this.practice = practice;
    }

    public EvidenceCharacteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(EvidenceCharacteristic characteristic) {
        this.characteristic = characteristic;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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
        if (!(object instanceof EvidenceMapping)) {
            return false;
        }
        EvidenceMapping other = (EvidenceMapping) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cz.strmik.cmmitool.entity.EvidenceMapping[id=" + id + "]";
    }

}
