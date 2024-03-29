/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.entity.model;

import cz.strmik.cmmitool.entity.*;
import cz.strmik.cmmitool.enums.MaturityLevel;
import java.util.Collection;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Entity
@NamedQueries(
    @NamedQuery(name="Model.findAll", query="SELECT m FROM Model m")
)
public class Model extends AcronymEntity {
    
    private static final long serialVersionUID = 1L;

    private String description;

    @Enumerated(EnumType.STRING)
    private MaturityLevel highestML;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "model")
    private Set<ProcessGroup> processGroups;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "model")
    private Set<ProcessArea> processAreas;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "model")
    private Set<Goal> genericGoals;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MaturityLevel getHighestML() {
        return highestML;
    }

    public void setHighestML(MaturityLevel highestML) {
        this.highestML = highestML;
    }

    public Set<ProcessGroup> getProcessGroups() {
        return processGroups;
    }

    public void setProcessGroups(Set<ProcessGroup> processGroups) {
        this.processGroups = processGroups;
    }

    public Collection<ProcessArea> getProcessAreas() {
        return processAreas;
    }

    public void setProcessAreas(Set<ProcessArea> processAreas) {
        this.processAreas = processAreas;
    }

    public Set<Goal> getGenericGoals() {
        return genericGoals;
    }

    public void setGenericGoals(Set<Goal> genericGoals) {
        this.genericGoals = genericGoals;
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
        if (!(object instanceof Model)) {
            return false;
        }
        Model other = (Model) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cz.strmik.cmmitool.entity.Model[id=" + id + "]";
    }

}
