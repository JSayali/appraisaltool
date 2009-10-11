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
import cz.strmik.cmmitool.util.tree.TreeGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/appraisal/rate")
@SessionAttributes({Attribute.PROJECT})
public class RatingController extends AbstractController {
    
    private static final String DASHBOARD = "/appraisal/rate/dashboard";

    @Autowired
    private GenericDao<Project, String> projectDao;

    @RequestMapping("/")
    public String dashboard(@ModelAttribute(Attribute.PROJECT) Project project, Model modelMap) {
        project = projectDao.read(project.getId());
        modelMap.addAttribute("ratingTree", TreeGenerator.modelToRatedTree(project.getModel() ,"edit", project));
        return DASHBOARD;
    }

    private static final Log log = LogFactory.getLog(RatingController.class);

}
