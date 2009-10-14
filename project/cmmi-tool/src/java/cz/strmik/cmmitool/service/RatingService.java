/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.entity.model.Goal;
import cz.strmik.cmmitool.entity.model.Practice;
import cz.strmik.cmmitool.entity.model.ProcessArea;
import cz.strmik.cmmitool.entity.project.Project;
import cz.strmik.cmmitool.entity.project.rating.GoalSatisfactionRating;
import cz.strmik.cmmitool.entity.project.rating.PracticeImplementationRating;
import cz.strmik.cmmitool.entity.project.rating.ProcessAreaCapRating;
import cz.strmik.cmmitool.entity.project.rating.ProcessAreaSatisfactionRating;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public interface RatingService {

    ProcessAreaCapRating getRatingOfProcessAreaCap(ProcessArea processArea, Project project);
    ProcessAreaSatisfactionRating getRatingOfProcessAreaSat(ProcessArea processArea, Project project);
    GoalSatisfactionRating getRatingOfGoal(Goal goal, Project project);
    PracticeImplementationRating getRatingOfPractice(Practice practice, Project project);

    void setRatingOfProcessAreaCap(ProcessAreaCapRating pacr);
    void setRatingOfProcessAreaSat(ProcessAreaSatisfactionRating pasr);
    void setRatingOfGoal(GoalSatisfactionRating gsr);
    void setRatingOfPractice(PracticeImplementationRating pir);

}
