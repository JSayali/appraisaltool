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
import cz.strmik.cmmitool.entity.method.RuleAggregation;
import cz.strmik.cmmitool.entity.method.ScaleRule;
import cz.strmik.cmmitool.entity.model.Goal;
import cz.strmik.cmmitool.entity.model.Practice;
import cz.strmik.cmmitool.entity.model.ProcessArea;
import cz.strmik.cmmitool.entity.project.EvidenceMapping;
import cz.strmik.cmmitool.entity.project.EvidenceRating;
import cz.strmik.cmmitool.entity.project.ProcessInstantiation;
import cz.strmik.cmmitool.entity.project.Project;
import cz.strmik.cmmitool.entity.project.rating.AbstractRating;
import cz.strmik.cmmitool.entity.project.rating.Finding;
import cz.strmik.cmmitool.entity.project.rating.GoalSatisfactionRating;
import cz.strmik.cmmitool.entity.project.rating.PracticeImplementationRating;
import cz.strmik.cmmitool.entity.project.rating.ProcessAreaCapRating;
import cz.strmik.cmmitool.entity.project.rating.ProcessAreaSatisfactionRating;
import cz.strmik.cmmitool.enums.RuleCompletion;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private GenericDao<GoalSatisfactionRating, Long> goalSatisfactionRatingDao;
    @Autowired
    private GenericDao<PracticeImplementationRating, Long> practiceImplementationRatingDao;
    @Autowired
    private GenericDao<ProcessAreaCapRating, Long> processAreaCapRatingDao;
    @Autowired
    private GenericDao<ProcessAreaSatisfactionRating, Long> processAreaSatisfactionRatingDao;
    @Autowired
    private GenericDao<Finding, Long> findingDao;

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
    public Project setRatingOfProcessAreaCap(ProcessAreaCapRating pacr) {
        Project project = projectDao.read(pacr.getProject().getId());
        if (pacr.isNew()) {
            project.getProcessAreaCap().add(pacr);
            pacr.setProject(project);
            findingDao.create(pacr.getFinding());
            processAreaCapRatingDao.create(pacr);
        } else {
            for (ProcessAreaCapRating par : project.getProcessAreaCap()) {
                if (par.getProcessArea().equals(pacr.getProcessArea())) {
                    par.setRating(pacr.getRating());
                    break;
                }
            }
        }
        return projectDao.update(project);
    }

    private Finding attachFinding(Finding detachedFinding) {
        Finding finding = findingDao.read(detachedFinding.getId());
        finding.setStrength(detachedFinding.getStrength());
        finding.setWeakness(detachedFinding.getWeakness());
        return finding;
    }

    @Override
    public Project setRatingOfProcessAreaSat(ProcessAreaSatisfactionRating pasr) {
        Project project = projectDao.read(pasr.getProject().getId());
        if (pasr.isNew()) {
            project.getProcessAreaSatisfaction().add(pasr);
            pasr.setProject(project);
            findingDao.create(pasr.getFinding());
            processAreaSatisfactionRatingDao.create(pasr);
        } else {
            for (ProcessAreaSatisfactionRating pas : project.getProcessAreaSatisfaction()) {
                if (pas.getProcessArea().equals(pasr.getProcessArea())) {
                    pas.setRating(pasr.getRating());
                    pas.setFinding(attachFinding(pasr.getFinding()));
                    break;
                }
            }
        }
        return projectDao.update(project);
    }

    @Override
    public Project setRatingOfGoal(GoalSatisfactionRating gsr) {
        Project project = projectDao.read(gsr.getProject().getId());
        if (gsr.isNew()) {
            project.getGoalSatisfaction().add(gsr);
            gsr.setProject(project);
            findingDao.create(gsr.getFinding());
            goalSatisfactionRatingDao.create(gsr);
        } else {
            for (GoalSatisfactionRating gs : project.getGoalSatisfaction()) {
                if (gs.getGoal().equals(gsr.getGoal())) {
                    gs.setRating(gsr.getRating());
                    gs.setFinding(attachFinding(gsr.getFinding()));
                    break;
                }
            }
        }
        return projectDao.update(project);
    }

    @Override
    public Project setRatingOfPractice(PracticeImplementationRating pir) {
        Project project = projectDao.read(pir.getProject().getId());
        if (pir.isNew()) {
            project.getPracticeImplementation().add(pir);
            pir.setProject(project);
            findingDao.create(pir.getFinding());
            practiceImplementationRatingDao.create(pir);
        } else {
            for (PracticeImplementationRating pi : project.getPracticeImplementation()) {
                if (pi.getPractice().equals(pir.getPractice())) {
                    pi.setRating(pir.getRating());
                    pi.setFinding(attachFinding(pir.getFinding()));
                    break;
                }
            }
        }
        return projectDao.update(project);
    }

    private ProcessAreaSatisfactionRating defaultPAS(ProcessArea processArea, Project project) {
        ProcessAreaSatisfactionRating pasr = new ProcessAreaSatisfactionRating();
        setNewAbstractRating(pasr, project);
        pasr.setProcessArea(processArea);
        pasr.setRating(getDefaultRating(project.getMethod().getProcessAreaSatisfaction()));
        return pasr;
    }

    private ProcessAreaCapRating defaultPAC(ProcessArea processArea, Project project) {
        ProcessAreaCapRating pacr = new ProcessAreaCapRating();
        setNewAbstractRating(pacr, project);
        pacr.setFinding(null);
        pacr.setProcessArea(processArea);
        pacr.setRating(getDefaultRating(project.getMethod().getProcessAreaCapLevel()));
        return pacr;
    }

    private GoalSatisfactionRating defaultGS(Goal goal, Project project) {
        GoalSatisfactionRating gsr = new GoalSatisfactionRating();
        setNewAbstractRating(gsr, project);
        gsr.setGoal(goal);
        gsr.setRating(getDefaultRating(project.getMethod().getGoalSatisfaction()));
        return gsr;
    }

    private PracticeImplementationRating defaultPI(Practice practice, Project project) {
        PracticeImplementationRating pir = new PracticeImplementationRating();
        setNewAbstractRating(pir, project);
        pir.setPractice(practice);
        pir.setRating(getDefaultRating(project.getMethod().getPracticeImplementation()));
        return pir;
    }

    private void setNewAbstractRating(AbstractRating ar, Project project) {
        ar.setNew(true);
        ar.setFinding(getDefaultFinding());
        ar.setProject(project);
    }

    @Override
    public RatingScale getDefaultRating(Set<RatingScale> rss) {
        if (rss.isEmpty()) {
            throw new IllegalArgumentException("Unable to get default rating of empty set of ratings");
        }
        for (RatingScale rs : rss) {
            if (rs.isDefaultRating()) {
                return rs;
            }
        }
        return rss.iterator().next();
    }

    private Finding getDefaultFinding() {
        Finding finding = new Finding();
        finding.setNew(true);
        return finding;
    }

    @Override
    public Set<RatingScale> computeGoalAggregation(Project project, Goal goal) {
        Map<RatingScale, Integer> counts = new HashMap<RatingScale, Integer>();

        Set<PracticeImplementationRating> pirs = getRatingsOfPracticesOfGoal(project, goal);
        for (PracticeImplementationRating pir : pirs) {
            RatingScale key = pir.getRating();
            addRatingCount(counts, key);
        }

        Set<RuleAggregation> rules = project.getMethod().getSortedGoalRuleAggregation();
        return computeAggregation(counts, rules);
    }

    @Override
    public Set<RatingScale> computePracticeAggregation(Project project, Practice practice) {
        Map<RatingScale, Integer> counts = new HashMap<RatingScale, Integer>();

        Set<EvidenceRating> ers = getRatingsOfProcessInstancesOfPractice(project, practice);
        for(EvidenceRating er : ers) {
            RatingScale key = er.getCharacterizePracticeImplementation();
            addRatingCount(counts, key);
        }

        Set<RuleAggregation> rules = project.getMethod().getSortedPracticeRuleAggregation();
        return computeAggregation(counts, rules);
    }

    private void addRatingCount(Map<RatingScale, Integer> counts, RatingScale key) {
        if (!counts.containsKey(key)) {
            counts.put(key, 0);
        }
        counts.put(key, counts.get(key) + 1);
    }

    @Override
    public Set<PracticeImplementationRating> getRatingsOfPracticesOfGoal(Project project, Goal goal) {
        Set<PracticeImplementationRating> pirs = new HashSet<PracticeImplementationRating>();
        Set<Practice> practices = new HashSet<Practice>(goal.getPractices());
        // add rated practices
        for (PracticeImplementationRating pir : project.getPracticeImplementation()) {
            if (practices.contains(pir.getPractice())) {
                pirs.add(pir);
                practices.remove(pir.getPractice());
            }
        }
        // add unrated practices
        RatingScale defaultRating = getDefaultRating(project.getMethod().getPracticeImplementation());
        for (Practice p : practices) {
            PracticeImplementationRating pir = new PracticeImplementationRating();
            pir.setPractice(p);
            pir.setRating(defaultRating);
        }
        return pirs;
    }

    @Override
    public Set<EvidenceRating> getRatingsOfProcessInstancesOfPractice(Project project, Practice practice) {
        Set<EvidenceRating> ers = new HashSet<EvidenceRating>();
        for (ProcessInstantiation pi : project.getInstantions()) {
            for (EvidenceRating er : project.getEvidenceRating()) {
                if (er.getPractice().equals(practice) && er.getProcessInstantiation().equals(pi)) {
                    ers.add(er);
                }
            }
        }
        return ers;
    }

    private Set<RatingScale> computeAggregation(Map<RatingScale, Integer> counts, Set<RuleAggregation> rules) {
        Set<RatingScale> targetScales = new HashSet<RatingScale>();
        for (RuleAggregation rule : rules) {
            boolean matches = false;
            for (ScaleRule sr : rule.getSources()) {
                int count = counts.get(sr.getScale()) == null ? 0 : counts.get(sr.getScale());
                RuleCompletion rc = sr.getRuleCompletion();
                matches = rc.equals(RuleCompletion.YES_NO) ||
                        (count == 0 && rc.equals(RuleCompletion.NO)) ||
                        (count > 0 && rc.equals(RuleCompletion.YES));
                if(!matches) {
                    break;
                }
            }
            if(matches) {
                for(ScaleRule sr : rule.getTargets()) {
                    if(sr.getRuleCompletion().equals(RuleCompletion.YES)) {
                        targetScales.add(sr.getScale());
                    }
                }
                return targetScales;
            }
        }
        return targetScales;
    }

    private static final Log _log = LogFactory.getLog(RatingServiceImpl.class);

}
