/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.entity;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Entity
@NamedQueries(
    @NamedQuery(name="Method.findAll", query="SELECT m FROM Method m")
)
public class Method extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;

    private boolean findingOnTheOrgLevel;
    private boolean findingOnProcessArea;
    private boolean findingOnGoalLevel;
    private boolean findingOnPracticeLevel;

    @OneToMany
    private Set<Rating> processAreaCapLevel;
    @OneToMany
    private Set<Rating> processAreaSatisfaction;
    @OneToMany
    private Set<Rating> goalSatisfaction;
    @OneToMany
    private Set<Rating> orgMaturityLevel;

    @Transient
    private boolean rateProcessAreaCapLevel;
    @Transient
    private boolean rateProcessAreaSatisfaction;
    @Transient
    private boolean rateGoalSatisfaction;
    @Transient
    private boolean rateOrgMaturityLevel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFindingOnGoalLevel() {
        return findingOnGoalLevel;
    }

    public void setFindingOnGoalLevel(boolean findingOnGoalLevel) {
        this.findingOnGoalLevel = findingOnGoalLevel;
    }

    public boolean isFindingOnPracticeLevel() {
        return findingOnPracticeLevel;
    }

    public void setFindingOnPracticeLevel(boolean findingOnPracticeLevel) {
        this.findingOnPracticeLevel = findingOnPracticeLevel;
    }

    public boolean isFindingOnProcessArea() {
        return findingOnProcessArea;
    }

    public void setFindingOnProcessArea(boolean findingOnProcessArea) {
        this.findingOnProcessArea = findingOnProcessArea;
    }

    public boolean isFindingOnTheOrgLevel() {
        return findingOnTheOrgLevel;
    }

    public void setFindingOnTheOrgLevel(boolean findingOnTheOrgLevel) {
        this.findingOnTheOrgLevel = findingOnTheOrgLevel;
    }

    public Set<Rating> getGoalSatisfaction() {
        return goalSatisfaction;
    }

    public void setGoalSatisfaction(Set<Rating> goalSatisfaction) {
        this.goalSatisfaction = goalSatisfaction;
    }

    public Set<Rating> getOrgMaturityLevel() {
        return orgMaturityLevel;
    }

    public void setOrgMaturityLevel(Set<Rating> orgMaturityLevel) {
        this.orgMaturityLevel = orgMaturityLevel;
    }

    public Set<Rating> getProcessAreaCapLevel() {
        return processAreaCapLevel;
    }

    public void setProcessAreaCapLevel(Set<Rating> processAreaCapLevel) {
        this.processAreaCapLevel = processAreaCapLevel;
    }

    public Set<Rating> getProcessAreaSatisfaction() {
        return processAreaSatisfaction;
    }

    public void setProcessAreaSatisfaction(Set<Rating> processAreaSatisfaction) {
        this.processAreaSatisfaction = processAreaSatisfaction;
    }

    public boolean isRateGoalSatisfaction() {
        return rateGoalSatisfaction;
    }

    public void setRateGoalSatisfaction(boolean rateGoalSatisfaction) {
        this.rateGoalSatisfaction = rateGoalSatisfaction;
    }

    public boolean isRateOrgMaturityLevel() {
        return rateOrgMaturityLevel;
    }

    public void setRateOrgMaturityLevel(boolean rateOrgMaturityLevel) {
        this.rateOrgMaturityLevel = rateOrgMaturityLevel;
    }

    public boolean isRateProcessAreaCapLevel() {
        return rateProcessAreaCapLevel;
    }

    public void setRateProcessAreaCapLevel(boolean rateProcessAreaCapLevel) {
        this.rateProcessAreaCapLevel = rateProcessAreaCapLevel;
    }

    public boolean isRateProcessAreaSatisfaction() {
        return rateProcessAreaSatisfaction;
    }

    public void setRateProcessAreaSatisfaction(boolean rateProcessAreaSatisfaction) {
        this.rateProcessAreaSatisfaction = rateProcessAreaSatisfaction;
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
        if (!(object instanceof Method)) {
            return false;
        }
        Method other = (Method) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cz.strmik.cmmitool.entity.Method[id=" + id + "]";
    }
    
}
