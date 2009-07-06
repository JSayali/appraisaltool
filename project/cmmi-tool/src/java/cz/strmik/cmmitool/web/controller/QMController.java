/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/qmanager")
@SessionAttributes("organization")
public class QMController {

    private final GenericDao<Organization, Long> orgDao;
    private static final String PROJ_LIST = "/qmanager/projectList";

    @Autowired
    public QMController(GenericDao<Organization, Long> orgDao) {
        this.orgDao = orgDao;
    }

    @RequestMapping("/")
    public String displayProjects(ModelMap model) {
        model.addAttribute("orgs", orgDao.findAll());
        return PROJ_LIST;
    }


}
