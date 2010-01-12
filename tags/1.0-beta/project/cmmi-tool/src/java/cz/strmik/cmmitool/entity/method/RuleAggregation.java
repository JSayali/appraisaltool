/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.entity.method;

import cz.strmik.cmmitool.entity.*;
import java.util.Set;
import javax.persistence.CascadeType;
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
public class RuleAggregation extends AbstractEntity implements Comparable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Method methodPractice;
    @ManyToOne
    private Method methodGoal;

    private int ruleNo;
    
    @OneToMany(mappedBy = "ruleAggregationSource", cascade=CascadeType.ALL)
    private Set<ScaleRule> sources;

    @OneToMany(mappedBy = "ruleAggregationTarget", cascade=CascadeType.ALL)
    private Set<ScaleRule> targets;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRuleNo() {
        return ruleNo;
    }

    public void setRuleNo(int ruleNo) {
        this.ruleNo = ruleNo;
    }

    public Set<ScaleRule> getSources() {
        return sources;
    }

    public void setSources(Set<ScaleRule> sources) {
        this.sources = sources;
    }

    public Set<ScaleRule> getTargets() {
        return targets;
    }

    public void setTargets(Set<ScaleRule> targets) {
        this.targets = targets;
    }

    public Method getMethodGoal() {
        return methodGoal;
    }

    public void setMethodGoal(Method methodGoal) {
        this.methodGoal = methodGoal;
    }

    public Method getMethodPractice() {
        return methodPractice;
    }

    public void setMethodPractice(Method methodPractice) {
        this.methodPractice = methodPractice;
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
        if (!(object instanceof RuleAggregation)) {
            return false;
        }
        RuleAggregation other = (RuleAggregation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cz.strmik.cmmitool.entity.PracticeRuleAggregation[id=" + id + "]";
    }

    @Override
    public int compareTo(Object o) {
        return ruleNo - ((RuleAggregation)o).ruleNo;
    }

}
