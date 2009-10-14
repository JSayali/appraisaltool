/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.method.RatingScale;
import cz.strmik.cmmitool.entity.model.Goal;
import cz.strmik.cmmitool.entity.model.Practice;
import cz.strmik.cmmitool.entity.model.ProcessArea;
import cz.strmik.cmmitool.entity.project.Project;
import cz.strmik.cmmitool.entity.project.rating.GoalSatisfactionRating;
import cz.strmik.cmmitool.entity.project.rating.PracticeImplementationRating;
import cz.strmik.cmmitool.entity.project.rating.ProcessAreaCapRating;
import cz.strmik.cmmitool.entity.project.rating.ProcessAreaSatisfactionRating;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class RatingServiceImpl implements RatingService {

    @Autowired
    private GenericDao<Project, String> projectDao;

    @Override
    public ProcessAreaCapRating getRatingOfProcessAreaCap(ProcessArea processArea, Project project) {
        project = projectDao.read(project.getId());
        if (project.getProcessAreaCap() != null) {
            for (ProcessAreaCapRating pac : project.getProcessAreaCap()) {
                if (pac.getProcessArea().equals(processArea)) {
                    return pac;
                }
            }
        }
        return defaultPAC(processArea, project);
    }

    @Override
    public ProcessAreaSatisfactionRating getRatingOfProcessAreaSat(ProcessArea processArea, Project project) {
        project = projectDao.read(project.getId());
        if (project.getProcessAreaSatisfaction() != null) {
            for (ProcessAreaSatisfactionRating pas : project.getProcessAreaSatisfaction()) {
                if (pas.getProcessArea().equals(processArea)) {
                    return pas;
                }
            }
        }
        return defaultPAS(processArea, project);
    }

    @Override
    public GoalSatisfactionRating getRatingOfGoal(Goal goal, Project project) {
        project = projectDao.read(project.getId());
        if (project.getGoalSatisfaction() != null) {
            for (GoalSatisfactionRating gas : project.getGoalSatisfaction()) {
                if (gas.getGoal().equals(goal)) {
                    return gas;
                }
            }
        }
        return defaultGS(goal, project);
    }

    @Override
    public PracticeImplementationRating getRatingOfPractice(Practice practice, Project project) {
        project = projectDao.read(project.getId());
        if (project.getPracticeImplementation() != null) {
            for (PracticeImplementationRating par : project.getPracticeImplementation()) {
                if (par.getPractice().equals(practice)) {
                    return par;
                }
            }
        }
        return defaultPI(practice, project);
    }

    @Override
    public void setRatingOfProcessAreaCap(ProcessAreaCapRating pacr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRatingOfProcessAreaSat(ProcessAreaSatisfactionRating pasr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRatingOfGoal(GoalSatisfactionRating gsr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRatingOfPractice(PracticeImplementationRating pir) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private ProcessAreaSatisfactionRating defaultPAS(ProcessArea processArea, Project project) {
        ProcessAreaSatisfactionRating pasr = new ProcessAreaSatisfactionRating();
        pasr.setNew(true);
        pasr.setProcessArea(processArea);
        pasr.setProject(project);
        pasr.setRating(getDefaultRating(project.getMethod().getProcessAreaSatisfaction()));
        return pasr;
    }

    private ProcessAreaCapRating defaultPAC(ProcessArea processArea, Project project) {
        ProcessAreaCapRating pacr = new ProcessAreaCapRating();
        pacr.setNew(true);
        pacr.setProcessArea(processArea);
        pacr.setProject(project);
        pacr.setRating(getDefaultRating(project.getMethod().getProcessAreaCapLevel()));
        return pacr;
    }

    private GoalSatisfactionRating defaultGS(Goal goal, Project project) {
        GoalSatisfactionRating gsr = new GoalSatisfactionRating();
        gsr.setNew(true);
        gsr.setGoal(goal);
        gsr.setProject(project);
        gsr.setRating(getDefaultRating(project.getMethod().getGoalSatisfaction()));
        return gsr;
    }

    private PracticeImplementationRating defaultPI(Practice practice, Project project) {
        PracticeImplementationRating pir = new PracticeImplementationRating();
        pir.setNew(true);
        pir.setPractice(practice);
        pir.setProject(project);
        pir.setRating(getDefaultRating(project.getMethod().getPracticeImplementation()));
        return pir;
    }

    private RatingScale getDefaultRating(Set<RatingScale> rss) {
        if(rss.isEmpty()) {
            throw new IllegalArgumentException("Unable to get default rating of empty set of ratings");
        }
        for(RatingScale rs : rss) {
            if(rs.isDefaultRating()) {
                return rs;
            }
        }
        return rss.iterator().next();
    }

    private static final Log _log = LogFactory.getLog(RatingServiceImpl.class);

}
