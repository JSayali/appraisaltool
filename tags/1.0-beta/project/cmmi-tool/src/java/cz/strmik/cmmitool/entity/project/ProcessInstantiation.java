/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.entity.project;

import cz.strmik.cmmitool.entity.AbstractEntity;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Entity
public class ProcessInstantiation extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "processInstantiation")
    private Set<EvidenceMapping> evidenceMappings;
    @ManyToOne(optional=false)
    private Project project;
    @OneToMany(mappedBy = "processInstantiation")
    private Set<EvidenceRating> evidenceRating;

    private String name;
    private String context;
    private boolean defaultInstantiation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public boolean isDefaultInstantiation() {
        return defaultInstantiation;
    }

    public void setDefaultInstantiation(boolean defaultInstantiation) {
        this.defaultInstantiation = defaultInstantiation;
    }

    public Set<EvidenceMapping> getEvidenceMappings() {
        return evidenceMappings;
    }

    public void setEvidenceMappings(Set<EvidenceMapping> evidenceMappings) {
        this.evidenceMappings = evidenceMappings;
    }

    public Set<EvidenceRating> getEvidenceRating() {
        return evidenceRating;
    }

    public void setEvidenceRating(Set<EvidenceRating> evidenceRating) {
        this.evidenceRating = evidenceRating;
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
        if (!(object instanceof ProcessInstantiation)) {
            return false;
        }
        ProcessInstantiation other = (ProcessInstantiation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cz.strmik.cmmitool.entity.project.ProcessInstantiation[id=" + id + "]";
    }

}
