/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.entity;

import cz.strmik.cmmitool.enums.MaturityLevel;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
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
public class Model extends AbstractEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private MaturityLevel highestML;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "model")
    private Collection<ProcessGroup> processGroups;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "model")
    private Collection<ProcessArea> processAreas;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public Collection<ProcessGroup> getProcessGroups() {
        return processGroups;
    }

    public void setProcessGroups(Collection<ProcessGroup> processGroups) {
        this.processGroups = processGroups;
    }

    public Collection<ProcessArea> getProcessAreas() {
        return processAreas;
    }

    public void setProcessAreas(Collection<ProcessArea> processAreas) {
        this.processAreas = processAreas;
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
