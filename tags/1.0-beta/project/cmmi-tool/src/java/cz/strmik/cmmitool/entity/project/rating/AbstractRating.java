/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.entity.project.rating;

import cz.strmik.cmmitool.entity.AbstractEntity;
import cz.strmik.cmmitool.entity.method.RatingScale;
import cz.strmik.cmmitool.entity.project.Project;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@MappedSuperclass
public abstract class AbstractRating extends AbstractEntity {

    @OneToOne(optional=true, cascade=CascadeType.ALL)
    private Finding finding;

    @ManyToOne(optional=false)
    private Project project;

    @ManyToOne(optional=false)
    private RatingScale rating;

    public Finding getFinding() {
        return finding;
    }

    public void setFinding(Finding finding) {
        this.finding = finding;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public RatingScale getRating() {
        return rating;
    }

    public void setRating(RatingScale rating) {
        this.rating = rating;
    }

    public String getStrength() {
        return finding.getStrength();
    }

    public void setStrength(String strength) {
        finding.setStrength(strength);
    }

    public String getWeakness() {
        return finding.getWeakness();
    }

    public void setWeakness(String weakness) {
        finding.setWeakness(weakness);
    }
}
