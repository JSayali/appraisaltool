/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.entity;

import cz.strmik.cmmitool.enums.MaturityLevel;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Entity
public class ProcessArea extends AcronymEntity {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    private ProcessGroup processGroup;

    @ManyToOne
    private Model model;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "processArea")
    private Set<Goal> goals;

    private MaturityLevel maturityLevel;
    private String summary;
    private String purpose;

    public MaturityLevel getMaturityLevel() {
        return maturityLevel;
    }

    public void setMaturityLevel(MaturityLevel maturityLevel) {
        this.maturityLevel = maturityLevel;
    }

    public ProcessGroup getProcessGroup() {
        return processGroup;
    }

    public void setProcessGroup(ProcessGroup processGroup) {
        this.processGroup = processGroup;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Set<Goal> getGoals() {
        return goals;
    }

    public void setGoals(Set<Goal> goals) {
        this.goals = goals;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
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
        if (!(object instanceof ProcessArea)) {
            return false;
        }
        ProcessArea other = (ProcessArea) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cz.strmik.cmmitool.entity.ProcessArea[id=" + id + "]";
    }

}
