/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.entity.method;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
public class RatingScale implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="order_")
    private int order;
    private String name;
    private int score;

    @ManyToOne
    private Method methodProcessCap;
    @ManyToOne
    private Method methodProcessSat;
    @ManyToOne
    private Method methodGoalSat;
    @ManyToOne
    private Method methodMatLevel;
    @ManyToOne
    private Method methodPracImpl;

    @OneToMany(mappedBy = "scale", cascade=CascadeType.ALL)
    private Set<ScaleRule> scaleRules;

    public RatingScale() {
    }

    public RatingScale(String name, int order, int score) {
        this.order = order;
        this.name = name;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Set<ScaleRule> getScaleRules() {
        return scaleRules;
    }

    public void setScaleRules(Set<ScaleRule> scaleRules) {
        this.scaleRules = scaleRules;
    }

    public Method getMethodGoalSat() {
        return methodGoalSat;
    }

    public void setMethodGoalSat(Method methodGoalSat) {
        this.methodGoalSat = methodGoalSat;
    }

    public Method getMethodMatLevel() {
        return methodMatLevel;
    }

    public void setMethodMatLevel(Method methodMatLevel) {
        this.methodMatLevel = methodMatLevel;
    }

    public Method getMethodPracImpl() {
        return methodPracImpl;
    }

    public void setMethodPracImpl(Method methodPracImpl) {
        this.methodPracImpl = methodPracImpl;
    }

    public Method getMethodProcessCap() {
        return methodProcessCap;
    }

    public void setMethodProcessCap(Method methodProcessCap) {
        this.methodProcessCap = methodProcessCap;
    }

    public Method getMethodProcessSat() {
        return methodProcessSat;
    }

    public void setMethodProcessSat(Method methodProcessSat) {
        this.methodProcessSat = methodProcessSat;
    }

    public String getScaleString() {
        if(getName()==null) {
            return "";
        }
        String[] scaleNames = getName().split(" ");
        StringBuilder result = new StringBuilder();
        for(int i=0;i<scaleNames.length;i++) {
            result.append(scaleNames[i].charAt(0));
        }
        return result.toString().toUpperCase();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : (name != null ? name.hashCode() : 0));
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RatingScale)) {
            return false;
        }
        RatingScale other = (RatingScale) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RatingScale[id=" + id + ", name="+name+", score="+score+"]";
    }

    @Override
    public int compareTo(Object o) {
        return order - ((RatingScale)o).order;
    }

}
