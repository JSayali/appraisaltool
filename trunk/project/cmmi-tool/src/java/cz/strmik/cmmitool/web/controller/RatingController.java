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
import cz.strmik.cmmitool.entity.model.Goal;
import cz.strmik.cmmitool.entity.model.Practice;
import cz.strmik.cmmitool.entity.model.ProcessArea;
import cz.strmik.cmmitool.entity.project.Project;
import cz.strmik.cmmitool.entity.project.rating.GoalSatisfactionRating;
import cz.strmik.cmmitool.entity.project.rating.PracticeImplementationRating;
import cz.strmik.cmmitool.enums.MaturityLevel;
import cz.strmik.cmmitool.service.RatingService;
import cz.strmik.cmmitool.util.tree.TreeGenerator;
import cz.strmik.cmmitool.web.controller.util.ProcessAreaRatingWrapper;
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
    private RatingService ratingService;

    @RequestMapping("/")
    public String dashboard(@ModelAttribute(Attribute.PROJECT) Project project, Model modelMap) {
        project = projectDao.read(project.getId());
        modelMap.addAttribute("ratingTree", TreeGenerator.modelToRatedTree(project.getModel() ,RATE_COMMAND, project, ratingService));
        return DASHBOARD;
    }

    @RequestMapping(method = RequestMethod.GET, value="/"+RATE_COMMAND+"-{element}-{id}.do")
    public String rate(@PathVariable("element") String element, @PathVariable("id") Long id,
            @ModelAttribute(Attribute.PROJECT) Project project, ModelMap modelMap) {
        project = projectDao.read(project.getId());
        Method method = methodDao.read(project.getMethod().getId());
        if(Model.class.getSimpleName().equalsIgnoreCase(element)) {
            modelMap.addAttribute("rateOrg", Boolean.TRUE);
            modelMap.addAttribute("rateOrgEnabled", method.isRateOrgMaturityLevel());
            modelMap.addAttribute("node", project);
        }
        if(ProcessArea.class.getSimpleName().equalsIgnoreCase(element)) {
            modelMap.addAttribute("ratePA", Boolean.TRUE);
            ProcessArea pa = processAreaDao.read(id);
            ProcessAreaRatingWrapper wrapper = new ProcessAreaRatingWrapper();
            wrapper.setId(pa.getId());
            wrapper.setName(pa.getName());
            if(method.isRateProcessAreaCapLevel()) {
                wrapper.setProcessAreaCapRating(ratingService.getRatingOfProcessAreaCap(pa, project));
                wrapper.setProcessAreaCapScales(method.getProcessAreaCapLevel());
            }
            if(method.isRateProcessAreaSatisfaction()) {
                wrapper.setProcessAreaSatisfactionRating(ratingService.getRatingOfProcessAreaSat(pa, project));
                wrapper.setProcessAreaSatisfactionScales(method.getProcessAreaSatisfaction());
            }
            modelMap.addAttribute("node", wrapper);
        }
        if(Goal.class.getSimpleName().equalsIgnoreCase(element)) {
            modelMap.addAttribute("rateGoal", Boolean.TRUE);
            modelMap.addAttribute("rateGoalEnabled", method.isRateGoalSatisfaction());
            modelMap.addAttribute("node", ratingService.getRatingOfGoal(goalDao.read(id), project));
            modelMap.addAttribute("scales", method.getGoalSatisfaction());
        }
        if(Practice.class.getSimpleName().equalsIgnoreCase(element)) {
            modelMap.addAttribute("ratePractice", Boolean.TRUE);
            modelMap.addAttribute("ratePracticeEnabled", method.isCharPracticeImplementation());
            modelMap.addAttribute("node", ratingService.getRatingOfPractice(practiceDao.read(id), project));
            modelMap.addAttribute("scales", method.getPracticeImplementation());
        }
        return DASHBOARD;
    }

    @RequestMapping(method = RequestMethod.POST, value="/save-Project-{id}.do")
    public String saveOrgUnitLevel(@PathVariable("id") String id, @ModelAttribute(Attribute.NODE) Project project,
            BindingResult result, ModelMap modelMap) {
        project = projectDao.read(id);
        Method method = methodDao.read(project.getMethod().getId());
        if(method.isRateOrgMaturityLevel()) {
            MaturityLevel ml = project.getMaturityRating();
            project.setMaturityRating(ml);
            project = projectDao.update(project);            
        }
        modelMap.addAttribute(Attribute.PROJECT, project);
        return DASHBOARD;
    }

    @RequestMapping(method = RequestMethod.POST, value="/save-ProcessAreaRatingWrapper-{id}.do")
    public String saveProcessAreaRating(@PathVariable("id") String id, @ModelAttribute(Attribute.NODE) ProcessAreaRatingWrapper wrapper,
            @ModelAttribute(Attribute.PROJECT) Project project, BindingResult result, ModelMap modelMap) {
        project = projectDao.read(project.getId());
        Method method = methodDao.read(project.getMethod().getId());
        if(method.isRateProcessAreaCapLevel()) {
            ratingService.setRatingOfProcessAreaCap(wrapper.getProcessAreaCapRating());
        }
        if(method.isRateProcessAreaSatisfaction()) {
            ratingService.setRatingOfProcessAreaSat(wrapper.getProcessAreaSatisfactionRating());
        }
        modelMap.addAttribute(Attribute.PROJECT, project);
        return "redirect:/appraisal/rate/";
    }

    @RequestMapping(method = RequestMethod.POST, value="/save-GoalSatisfactionRating-{id}.do")
    public String saveGoalRating(@PathVariable("id") String id, @ModelAttribute(Attribute.NODE) GoalSatisfactionRating gsr,
            @ModelAttribute(Attribute.PROJECT) Project project, BindingResult result, ModelMap modelMap) {
        project = projectDao.read(project.getId());
        Method method = methodDao.read(project.getMethod().getId());
        if(method.isRateGoalSatisfaction()) {
            project = ratingService.setRatingOfGoal(gsr);
        }
        modelMap.addAttribute(Attribute.PROJECT, project);
        return "redirect:/appraisal/rate/";
    }

    @RequestMapping(method = RequestMethod.POST, value="/save-PracticeImplementationRating-{id}.do")
    public String savePracticeRating(@PathVariable("id") String id, @ModelAttribute(Attribute.NODE)
            PracticeImplementationRating pir,
            @ModelAttribute(Attribute.PROJECT) Project project, BindingResult result, ModelMap modelMap) {
        project = projectDao.read(project.getId());
        Method method = methodDao.read(project.getMethod().getId());
        if(method.isCharPracticeImplementation()) {
            project = ratingService.setRatingOfPractice(pir);
        }
        modelMap.addAttribute(Attribute.PROJECT, project);
        return "redirect:/appraisal/rate/";
    }

    private static final Log log = LogFactory.getLog(RatingController.class);

}
