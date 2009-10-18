/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.method.Method;
import cz.strmik.cmmitool.entity.method.RatingScale;
import cz.strmik.cmmitool.entity.model.Goal;
import cz.strmik.cmmitool.entity.model.Practice;
import cz.strmik.cmmitool.entity.model.ProcessArea;
import cz.strmik.cmmitool.entity.project.EvidenceMapping;
import cz.strmik.cmmitool.entity.project.EvidenceRating;
import cz.strmik.cmmitool.entity.project.ProcessInstantiation;
import cz.strmik.cmmitool.entity.project.Project;
import cz.strmik.cmmitool.entity.project.rating.GoalSatisfactionRating;
import cz.strmik.cmmitool.entity.project.rating.PracticeImplementationRating;
import cz.strmik.cmmitool.entity.project.rating.ProcessAreaSatisfactionRating;
import cz.strmik.cmmitool.enums.MaturityLevel;
import cz.strmik.cmmitool.service.RatingService;
import cz.strmik.cmmitool.util.tree.TreeGenerator;
import cz.strmik.cmmitool.web.controller.util.ProcessAreaRatingWrapper;
import cz.strmik.cmmitool.web.lang.LangProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/appraisal/rate")
@SessionAttributes({Attribute.PROJECT, "ratingTree", Attribute.NODE})
public class RatingController extends AbstractController {

    private static final Log log = LogFactory.getLog(RatingController.class);
    private static final String DASHBOARD = "/appraisal/rate/dashboard";
    private static final String RATE_COMMAND = "rate";
    @Autowired
    private GenericDao<Project, String> projectDao;
    @Autowired
    private GenericDao<Goal, Long> goalDao;
    @Autowired
    private GenericDao<Practice, Long> practiceDao;
    @Autowired
    private GenericDao<ProcessArea, Long> processAreaDao;
    @Autowired
    private GenericDao<Method, Long> methodDao;
    @Autowired
    private GenericDao<EvidenceMapping, Long> evidenceMappingDao;
    @Autowired
    private GenericDao<PracticeImplementationRating, Long> practiceImplementationRatingDao;
    @Autowired
    private RatingService ratingService;

    @RequestMapping("/")
    public String dashboard(@ModelAttribute(Attribute.PROJECT) Project project, Model modelMap) {
        project = projectDao.read(project.getId());
        modelMap.addAttribute("ratingTree", TreeGenerator.modelToRatedTree(project.getModel(), RATE_COMMAND, project, ratingService));
        return DASHBOARD;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/" + RATE_COMMAND + "-{element}-{id}.do")
    public String rate(@PathVariable("element") String element, @PathVariable("id") Long id,
            @ModelAttribute(Attribute.PROJECT) Project project, ModelMap modelMap) {
        project = projectDao.read(project.getId());
        Method method = methodDao.read(project.getMethod().getId());
        if (Model.class.getSimpleName().equalsIgnoreCase(element)) {
            rateModel(project, method, modelMap);
        }
        if (ProcessArea.class.getSimpleName().equalsIgnoreCase(element)) {
            ratePA(project, method, modelMap, id);
        }
        if (Goal.class.getSimpleName().equalsIgnoreCase(element)) {
            rateGoal(project, method, modelMap, id);
        }
        if (Practice.class.getSimpleName().equalsIgnoreCase(element)) {
            ratePractice(project, method, modelMap, id);
        }
        return DASHBOARD;
    }

    private void rateModel(Project project, Method method, ModelMap modelMap) {
        modelMap.addAttribute("rateOrg", Boolean.TRUE);
        modelMap.addAttribute("rateOrgEnabled", method.isRateOrgMaturityLevel());
        modelMap.addAttribute("node", project);
        addPAs(project, method, modelMap);
    }

    private void addPAs(Project project, Method method, ModelMap modelMap) {
        List<ProcessAreaSatisfactionRating> pasrs = new ArrayList<ProcessAreaSatisfactionRating>();
        Set<ProcessArea> pas = new HashSet<ProcessArea>(project.getModel().getProcessAreas());
        // add rated PAs
        for(ProcessAreaSatisfactionRating pasr : project.getProcessAreaSatisfaction()) {
            pasrs.add(pasr);
            pas.remove(pasr.getProcessArea());
        }
        // add unrated practices
        RatingScale defaultRating = ratingService.getDefaultRating(method.getProcessAreaSatisfaction());
        for(ProcessArea pa : pas) {
            ProcessAreaSatisfactionRating pir = new ProcessAreaSatisfactionRating();
            pir.setProcessArea(pa);
            pir.setRating(defaultRating);
        }
        modelMap.addAttribute("pas", pasrs);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save-Project-{id}.do")
    public String saveOrgUnitLevel(@PathVariable("id") String id, @ModelAttribute(Attribute.NODE) Project project,
            BindingResult result, ModelMap modelMap) {
        project = projectDao.read(id);
        Method method = methodDao.read(project.getMethod().getId());
        if (method.isRateOrgMaturityLevel()) {
            MaturityLevel ml = project.getMaturityRating();
            project.setMaturityRating(ml);
            project = projectDao.update(project);
        }
        modelMap.addAttribute(Attribute.PROJECT, project);
        return DASHBOARD;
    }

    private void ratePA(Project project, Method method, ModelMap modelMap, long processAreaId) {
        modelMap.addAttribute("ratePA", Boolean.TRUE);
        ProcessArea pa = processAreaDao.read(processAreaId);
        ProcessAreaRatingWrapper wrapper = new ProcessAreaRatingWrapper();
        wrapper.setId(pa.getId());
        wrapper.setName(pa.getName());
        if (method.isRateProcessAreaCapLevel()) {
            wrapper.setProcessAreaCapRating(ratingService.getRatingOfProcessAreaCap(pa, project));
            wrapper.setProcessAreaCapScales(method.getProcessAreaCapLevel());
        }
        if (method.isRateProcessAreaSatisfaction()) {
            wrapper.setProcessAreaSatisfactionRating(ratingService.getRatingOfProcessAreaSat(pa, project));
            wrapper.setProcessAreaSatisfactionScales(method.getProcessAreaSatisfaction());
        }
        modelMap.addAttribute("node", wrapper);
        addGoalsOfPA(project, method, modelMap, pa);
    }

    private void addGoalsOfPA(Project project, Method method, ModelMap modelMap, ProcessArea pa) {
        List<GoalSatisfactionRating> gsrs = new ArrayList<GoalSatisfactionRating>();
        Set<Goal> goals = new HashSet<Goal>(pa.getGoals());
        // add rated goals
        for(GoalSatisfactionRating gsr : project.getGoalSatisfaction()) {
            if(goals.contains(gsr.getGoal())) {
                gsrs.add(gsr);
                goals.remove(gsr.getGoal());
            }
        }
        // add unrated practices
        RatingScale defaultRating = ratingService.getDefaultRating(method.getGoalSatisfaction());
        for(Goal g : goals) {
            GoalSatisfactionRating pir = new GoalSatisfactionRating();
            pir.setGoal(g);
            pir.setRating(defaultRating);
        }
        modelMap.addAttribute("goals", gsrs);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save-ProcessAreaRatingWrapper-{id}.do")
    public String saveProcessAreaRating(@PathVariable("id") String id, @ModelAttribute(Attribute.NODE) ProcessAreaRatingWrapper wrapper,
            @ModelAttribute(Attribute.PROJECT) Project project, BindingResult result, ModelMap modelMap) {
        project = projectDao.read(project.getId());
        Method method = methodDao.read(project.getMethod().getId());
        if (method.isRateProcessAreaCapLevel()) {
            ratingService.setRatingOfProcessAreaCap(wrapper.getProcessAreaCapRating());
        }
        if (method.isRateProcessAreaSatisfaction()) {
            ratingService.setRatingOfProcessAreaSat(wrapper.getProcessAreaSatisfactionRating());
        }
        modelMap.addAttribute(Attribute.PROJECT, project);
        return "redirect:/appraisal/rate/";
    }


    private void rateGoal(Project project, Method method, ModelMap modelMap, long goalId) {
        Goal goal = goalDao.read(goalId);
        modelMap.addAttribute("rateGoal", Boolean.TRUE);
        modelMap.addAttribute("rateGoalEnabled", method.isRateGoalSatisfaction());
        modelMap.addAttribute("node", ratingService.getRatingOfGoal(goal, project));
        modelMap.addAttribute("scales", method.getGoalSatisfaction());
        addPracticesOfGoal(project, goal, modelMap);
        Set<RatingScale> aggregated = ratingService.computeGoalAggregation(project, goal);
        addAggregatedMessage(aggregated, modelMap);
    }
    
    private void addPracticesOfGoal(Project project, Goal goal, ModelMap modelMap) {
        Set<PracticeImplementationRating> pirs = ratingService.getRatingsOfPracticesOfGoal(project, goal);
        modelMap.addAttribute("practices", pirs);
    }

    private void addAggregatedMessage(Set<RatingScale> aggregated, ModelMap modelMap) {
        String result;
        if(aggregated.isEmpty()) {
            result = LangProvider.getString("team-judgement");
        } else {
            StringBuilder sb = new StringBuilder();
            for(RatingScale rs : aggregated) {
                sb.append(rs.getName());
                sb.append(", ");
            }
            if(sb.length()>2) {
                sb.delete(sb.length()-2, sb.length()-1);
            }
            result = sb.toString();
        }
        modelMap.addAttribute("aggregatedMessage", result);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save-GoalSatisfactionRating-{id}.do")
    public String saveGoalRating(@PathVariable("id") String id, @ModelAttribute(Attribute.NODE) GoalSatisfactionRating gsr,
            @ModelAttribute(Attribute.PROJECT) Project project, BindingResult result, ModelMap modelMap) {
        project = projectDao.read(project.getId());
        Method method = methodDao.read(project.getMethod().getId());
        if (method.isRateGoalSatisfaction()) {
            project = ratingService.setRatingOfGoal(gsr);
        }
        modelMap.addAttribute(Attribute.PROJECT, project);
        return "redirect:/appraisal/rate/";
    }

    private void ratePractice(Project project, Method method, ModelMap modelMap, long practiceId) {
        Practice practice = practiceDao.read(practiceId);
        modelMap.addAttribute("ratePractice", Boolean.TRUE);
        modelMap.addAttribute("ratePracticeEnabled", method.isCharPracticeImplementation());
        modelMap.addAttribute("node", ratingService.getRatingOfPractice(practice, project));
        modelMap.addAttribute("scales", method.getPracticeImplementation());
        addEvidencesOfPractice(project, practice, modelMap);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save-PracticeImplementationRating-{id}.do")
    public String savePracticeRating(@PathVariable("id") String id, @ModelAttribute(Attribute.NODE) PracticeImplementationRating pir,
            @ModelAttribute(Attribute.PROJECT) Project project, BindingResult result, ModelMap modelMap) {
        project = projectDao.read(project.getId());
        Method method = methodDao.read(project.getMethod().getId());
        if (method.isCharPracticeImplementation()) {
            project = ratingService.setRatingOfPractice(pir);
        }
        modelMap.addAttribute(Attribute.PROJECT, project);
        return "redirect:/appraisal/rate/";
    }

    private void addEvidencesOfPractice(Project project, Practice practice, ModelMap modelMap) {
        Map<ProcessInstantiation, List<EvidenceMapping>> evidenceMappings = new
                HashMap<ProcessInstantiation, List<EvidenceMapping>>();
        Map<ProcessInstantiation, EvidenceRating> evidenceRatings = new
                HashMap<ProcessInstantiation, EvidenceRating>();

        for (ProcessInstantiation pi : project.getInstantions()) {
            List<EvidenceMapping> mappings = evidenceMappingDao.findByNamedQuery("findByProjectPracticeInstantiation", "project", project,
                    "practice", practice, "processInstantiation", pi);
            evidenceMappings.put(pi, mappings);
            for (EvidenceRating er : project.getEvidenceRating()) {
                if (er.getPractice().equals(practice) && er.getProcessInstantiation().equals(pi)) {
                    evidenceRatings.put(pi, er);
                }
            }
        }

        modelMap.addAttribute("evidenceMapping", evidenceMappings);
        modelMap.addAttribute("evidenceRatings", evidenceRatings);
    }

}

//                    modelMap.addAttribute("characterization", er.getCharacterizePracticeImplementation().getName());
//                    modelMap.addAttribute("adequacy", LangProvider.getString("PracticeEvidenceAdequacy." + er.getEvidenceAdequacy().name()));
