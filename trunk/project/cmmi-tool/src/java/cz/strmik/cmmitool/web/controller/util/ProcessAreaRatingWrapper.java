/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller.util;

import cz.strmik.cmmitool.entity.method.RatingScale;
import cz.strmik.cmmitool.entity.project.rating.ProcessAreaCapRating;
import cz.strmik.cmmitool.entity.project.rating.ProcessAreaSatisfactionRating;
import java.util.Set;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class ProcessAreaRatingWrapper {

    private Long id;
    private String name;

    private ProcessAreaCapRating processAreaCapRating;
    private ProcessAreaSatisfactionRating processAreaSatisfactionRating;

    private Set<RatingScale> processAreaCapScales;
    private Set<RatingScale> processAreaSatisfactionScales;

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

    public ProcessAreaCapRating getProcessAreaCapRating() {
        return processAreaCapRating;
    }

    public RatingScale getProcessAreaCapRatingScale() {
        return processAreaCapRating.getRating();
    }

    public void setProcessAreaCapRatingScale(RatingScale rs) {
        processAreaCapRating.setRating(rs);
    }

    public void setProcessAreaCapRating(ProcessAreaCapRating processAreaCapRating) {
        this.processAreaCapRating = processAreaCapRating;
    }

    public ProcessAreaSatisfactionRating getProcessAreaSatisfactionRating() {
        return processAreaSatisfactionRating;
    }

    public void setProcessAreaSatisfactionRating(ProcessAreaSatisfactionRating processAreaSatisfactionRating) {
        this.processAreaSatisfactionRating = processAreaSatisfactionRating;
    }

    public RatingScale getProcessAreaSatRatingScale() {
        return processAreaSatisfactionRating.getRating();
    }

    public void setProcessAreaSatRatingScale(RatingScale rs) {
        processAreaSatisfactionRating.setRating(rs);
    }

    public Set<RatingScale> getProcessAreaCapScales() {
        return processAreaCapScales;
    }

    public void setProcessAreaCapScales(Set<RatingScale> processAreaCapScales) {
        this.processAreaCapScales = processAreaCapScales;
    }

    public Set<RatingScale> getProcessAreaSatisfactionScales() {
        return processAreaSatisfactionScales;
    }

    public void setProcessAreaSatisfactionScales(Set<RatingScale> processAreaSatisfactionScales) {
        this.processAreaSatisfactionScales = processAreaSatisfactionScales;
    }

    public String getWeakness() {
        return processAreaSatisfactionRating.getFinding().getWeakness();
    }

    public void setWeakness(String weakness) {
        processAreaSatisfactionRating.getFinding().setWeakness(weakness);
    }

    public String getStrength() {
        return processAreaSatisfactionRating.getFinding().getStrength();
    }

    public void setStrength(String strength) {
        System.err.println("strength = "+strength);
        processAreaSatisfactionRating.getFinding().setStrength(strength);
    }

}
