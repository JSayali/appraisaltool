/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.entity;

import cz.strmik.cmmitool.enums.RuleCompletion;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Entity
public class ScaleRule extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private RuleAggregation ruleAggregationSource;
    @ManyToOne
    private RuleAggregation ruleAggregationTarget;

    @ManyToOne(optional=false)
    private RatingScale scale;

    @Enumerated(EnumType.STRING)
    private RuleCompletion ruleCompletion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RatingScale getScale() {
        return scale;
    }

    public void setScale(RatingScale scale) {
        this.scale = scale;
    }

    public RuleCompletion getRuleCompletion() {
        return ruleCompletion;
    }

    public void setRuleCompletion(RuleCompletion ruleCompletion) {
        this.ruleCompletion = ruleCompletion;
    }

    public RuleAggregation getRuleAggregationSource() {
        return ruleAggregationSource;
    }

    public void setRuleAggregationSource(RuleAggregation ruleAggregationSource) {
        this.ruleAggregationSource = ruleAggregationSource;
    }

    public RuleAggregation getRuleAggregationTarget() {
        return ruleAggregationTarget;
    }

    public void setRuleAggregationTarget(RuleAggregation ruleAggregationTarget) {
        this.ruleAggregationTarget = ruleAggregationTarget;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : scale.hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ScaleRule)) {
            return false;
        }
        ScaleRule other = (ScaleRule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        if ((this.scale == null && other.scale != null) || (this.scale != null && !this.scale.equals(other.scale))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cz.strmik.cmmitool.entity.AggregationRule[id=" + id + "]";
    }

}
