/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.project.Evidence;
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
@RequestMapping("/appraisal/reports")
@SessionAttributes({Attribute.PROJECT})
public class ReportsController {

    private static final String PATH = "/appraisal/reports/";

    @Autowired
    private GenericDao<Project, String> projectDao;
    @Autowired
    private GenericDao<Evidence, Long> evidenceDao;

    @RequestMapping("/")
    public String dashboard(Model model) {
        return PATH + "dashboard";
    }

    @RequestMapping("evidence.do")
    public String evidence(@ModelAttribute(Attribute.PROJECT) Project project, Model modelMap) {
        project = projectDao.read(project.getId());
        modelMap.addAttribute("evidenceTree", TreeGenerator.evidenceReportTree(project, evidenceDao));
        return PATH + "evidence";
    }

    private static final Log log = LogFactory.getLog(ReportsController.class);

}
