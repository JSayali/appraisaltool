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

    @OneToOne(optional=true)
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

}
