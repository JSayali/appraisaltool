/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.entity.project;

import cz.strmik.cmmitool.entity.*;
import cz.strmik.cmmitool.entity.method.Method;
import cz.strmik.cmmitool.entity.model.Model;
import cz.strmik.cmmitool.entity.project.rating.Finding;
import cz.strmik.cmmitool.entity.project.rating.GoalSatisfactionRating;
import cz.strmik.cmmitool.entity.project.rating.PracticeImplementationRating;
import cz.strmik.cmmitool.entity.project.rating.ProcessAreaCapRating;
import cz.strmik.cmmitool.entity.project.rating.ProcessAreaSatisfactionRating;
import cz.strmik.cmmitool.enums.MaturityLevel;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Entity
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String name;

    @ManyToOne
    private Method method;

    @ManyToOne
    private Model model;

    @ManyToOne
    private Organization organization;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="project")
    private Set<TeamMember> team;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "project")
    private Set<EvidenceMapping> evidenceMappings;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "project")
    private Set<EvidenceRating> evidenceRating;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "project")
    private Set<Evidence> evidence;

    @Enumerated(EnumType.STRING)
    private MaturityLevel targetML;

    // rating

    @Enumerated(EnumType.STRING)
    private MaturityLevel maturityRating;

    @OneToOne
    private Finding findingOnTheOrgLevel;

    @OneToMany(mappedBy = "project")
    private Set<ProcessAreaSatisfactionRating> processAreaSatisfaction;
    @OneToMany(mappedBy = "project")
    private Set<ProcessAreaCapRating> processAreaCap;
    @OneToMany(mappedBy = "project")
    private Set<GoalSatisfactionRating> goalSatisfaction;
    @OneToMany(mappedBy = "project")
    private Set<PracticeImplementationRating> practiceImplementation;

    // transient fields

    @Transient
    private boolean newProject;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Set<TeamMember> getTeam() {
        return team;
    }

    public void setTeam(Set<TeamMember> team) {
        this.team = team;
    }

    public boolean isNewProject() {
        return newProject;
    }

    public void setNewProject(boolean newProject) {
        this.newProject = newProject;
    }

    public MaturityLevel getTargetML() {
        return targetML;
    }

    public void setTargetML(MaturityLevel targetML) {
        this.targetML = targetML;
    }

    public Set<EvidenceMapping> getEvidenceMappings() {
        return evidenceMappings;
    }

    public void setEvidenceMappings(Set<EvidenceMapping> evidenceMappings) {
        this.evidenceMappings = evidenceMappings;
    }

    public Set<Evidence> getEvidence() {
        return evidence;
    }

    public void setEvidence(Set<Evidence> evidence) {
        this.evidence = evidence;
    }

    public Set<EvidenceRating> getEvidenceRating() {
        return evidenceRating;
    }

    public void setEvidenceRating(Set<EvidenceRating> evidenceRating) {
        this.evidenceRating = evidenceRating;
    }

    public Finding getFindingOnTheOrgLevel() {
        return findingOnTheOrgLevel;
    }

    public void setFindingOnTheOrgLevel(Finding findingOnTheOrgLevel) {
        this.findingOnTheOrgLevel = findingOnTheOrgLevel;
    }

    public Set<GoalSatisfactionRating> getGoalSatisfaction() {
        return goalSatisfaction;
    }

    public void setGoalSatisfaction(Set<GoalSatisfactionRating> goalSatisfaction) {
        this.goalSatisfaction = goalSatisfaction;
    }

    public MaturityLevel getMaturityRating() {
        return maturityRating;
    }

    public void setMaturityRating(MaturityLevel maturityRating) {
        this.maturityRating = maturityRating;
    }

    public Set<PracticeImplementationRating> getPracticeImplementation() {
        return practiceImplementation;
    }

    public void setPracticeImplementation(Set<PracticeImplementationRating> practiceImplementation) {
        this.practiceImplementation = practiceImplementation;
    }

    public Set<ProcessAreaCapRating> getProcessAreaCap() {
        return processAreaCap;
    }

    public void setProcessAreaCap(Set<ProcessAreaCapRating> processAreaCap) {
        this.processAreaCap = processAreaCap;
    }

    public Set<ProcessAreaSatisfactionRating> getProcessAreaSatisfaction() {
        return processAreaSatisfaction;
    }

    public void setProcessAreaSatisfaction(Set<ProcessAreaSatisfactionRating> processAreaSatisfaction) {
        this.processAreaSatisfaction = processAreaSatisfaction;
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
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Project[id=" + id + ", Team="+team+"]";
    }

}
