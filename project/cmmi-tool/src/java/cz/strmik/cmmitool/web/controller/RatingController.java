/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.project.Project;
import cz.strmik.cmmitool.enums.MaturityLevel;
import cz.strmik.cmmitool.service.RatingService;
import cz.strmik.cmmitool.util.tree.TreeGenerator;
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
@SessionAttributes({Attribute.PROJECT, "ratingTree"})
public class RatingController extends AbstractController {
    
    private static final String DASHBOARD = "/appraisal/rate/dashboard";
    private static final String RATE_COMMAND = "rate";

    @Autowired
    private GenericDao<Project, String> projectDao;

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
        if(Model.class.getSimpleName().equalsIgnoreCase(element)) {
            modelMap.addAttribute("rateOrg", Boolean.TRUE);
            modelMap.addAttribute("node", project);
        }

        return DASHBOARD;
    }

    @RequestMapping(method = RequestMethod.POST, value="/save-Project-{id}.do")
    public String saveElementProcessArea(@PathVariable("id") String id, @ModelAttribute(Attribute.NODE) Project project,
            BindingResult result, ModelMap modelMap) {
        MaturityLevel ml = project.getMaturityRating();
        project = projectDao.read(id);
        project.setMaturityRating(ml);
        project = projectDao.update(project);
        modelMap.addAttribute(Attribute.PROJECT, project);
        return DASHBOARD;
    }

    private static final Log log = LogFactory.getLog(RatingController.class);

}
