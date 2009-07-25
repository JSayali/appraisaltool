/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.entity;

import java.util.Collection;
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
public class Goal extends AcronymEntity {

    private static final long serialVersionUID = 1L;
    
    private String summary;

    @ManyToOne
    private ProcessArea processArea;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "goal")
    private Collection<Practice> practices;

    public ProcessArea getProcessArea() {
        return processArea;
    }

    public void setProcessArea(ProcessArea processArea) {
        this.processArea = processArea;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Collection<Practice> getPractices() {
        return practices;
    }

    public void setPractices(Collection<Practice> practices) {
        this.practices = practices;
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
        if (!(object instanceof Goal)) {
            return false;
        }
        Goal other = (Goal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cz.strmik.cmmitool.entity.Goal[id=" + id + "]";
    }

}
