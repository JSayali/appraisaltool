/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.Project;
import cz.strmik.cmmitool.entity.TeamMember;
import cz.strmik.cmmitool.entity.User;
import cz.strmik.cmmitool.enums.TeamRole;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/appraisal")
@SessionAttributes({Attribute.PROJECT, Attribute.PROJECTS, Attribute.LEADER, Attribute.AUDITOR})
public class AppraisalController extends AbstractController {

    @Autowired
    private GenericDao<Project, String> projectDao;

    private static final String DASHBOARD = "/appraisal/dashboard";

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String showDashboard(ModelMap model) {
        model.addAttribute(Attribute.PROJECTS, getUserProjects());
        return DASHBOARD;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public String selectProject(ModelMap model, @RequestParam("projectId") String projectId, HttpSession session) {
        if(projectId != null) {
            Project project = projectDao.read(projectId);
            if(project!=null) {
                model.addAttribute(Attribute.PROJECT, project);
                session.setAttribute(Attribute.PROJECT, project);
                addUserRoles(model, project);
            } else {
                log.warn("Project with id ="+projectId+" not found!");
            }
        }
        return DASHBOARD;
    }

    private void addUserRoles(ModelMap model, Project project) {
        User user = getLoggedUser();
        for(TeamMember tm : user.getMemberOfTeams()) {
            if(tm.getProject().equals(project)) {
                if(tm.getTeamRole().equals(TeamRole.AUDITOR)) {
                    model.addAttribute("auditor", true);
                }
                if(tm.getTeamRole().equals(TeamRole.LEADER)) {
                    model.addAttribute("leader", true);
                }
                break;
            }
        }
    }

    private List<Project> getUserProjects() {
        User user = getLoggedUser();
        List<Project> projects = new ArrayList<Project>();
        for(TeamMember tm : user.getMemberOfTeams()) {
            projects.add(tm.getProject());
        }
        return projects;
    }

    private static final Log log = LogFactory.getLog(AppraisalController.class);

}
