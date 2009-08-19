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
import javax.persistence.OneToOne;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Entity
public class AggregationRule extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private RatingScale scale;

    @Enumerated(EnumType.STRING)
    private RuleCompletion source;
    @Enumerated(EnumType.STRING)
    private RuleCompletion target;

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

    public RuleCompletion getSource() {
        return source;
    }

    public void setSource(RuleCompletion source) {
        this.source = source;
    }

    public RuleCompletion getTarget() {
        return target;
    }

    public void setTarget(RuleCompletion target) {
        this.target = target;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : scale.hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AggregationRule)) {
            return false;
        }
        AggregationRule other = (AggregationRule) object;
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
