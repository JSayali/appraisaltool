/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.dao.UserDao;
import cz.strmik.cmmitool.entity.Method;
import cz.strmik.cmmitool.entity.Model;
import cz.strmik.cmmitool.entity.Organization;
import cz.strmik.cmmitool.entity.Project;
import cz.strmik.cmmitool.entity.TeamMember;
import cz.strmik.cmmitool.entity.User;
import cz.strmik.cmmitool.enums.MaturityLevel;
import cz.strmik.cmmitool.enums.TeamRole;
import cz.strmik.cmmitool.service.ProjectService;
import cz.strmik.cmmitool.util.validator.ProjectValidator;
import cz.strmik.cmmitool.util.validator.TeamMemberValidator;
import cz.strmik.cmmitool.web.lang.LangProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/qmanager")
@SessionAttributes({Attribute.PROJECT,Attribute.ORG})
public class QMController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GenericDao<Organization, Long> organizationDao;
    @Autowired
    private GenericDao<Method, String> methodDao;
    @Autowired
    private GenericDao<Model, String> modelDao;
    @Autowired
    private GenericDao<Project, String> projectDao;
    
    private static final String PROJ_LIST = "/qmanager/projectList";
    private static final String PROJ_FORM = "/qmanager/projectForm1";
    private static final String PROJ_FORM_USERS = "/qmanager/projectForm2";

    // Model attributes

    @ModelAttribute("organizations")
    public Map<Long, String> getOrganizations() {
        List<Organization> orgs = organizationDao.findByNamedQuery("findActive");
        Map<Long, String> aviableOrgs = new HashMap<Long, String>();
            for (Organization org : orgs) {
                aviableOrgs.put(org.getId(),org.getName());
            }
        return aviableOrgs;
    }

    @ModelAttribute("teamMember")
    public TeamMember getTeamMember(HttpSession session) {
        TeamMember teamMember = new TeamMember();
        teamMember.setProject((Project) session.getAttribute(Attribute.PROJECT));
        return teamMember;
    }

    @ModelAttribute("methods")
    public List<Method> getMethods() {
        return methodDao.findAll();
    }

    @ModelAttribute("models")
    public List<Model> getModels() {
        return  modelDao.findAll();
    }

    @ModelAttribute("levels")
    public Collection<MaturityLevel> getLevels() {
        List<MaturityLevel> levels = new ArrayList<MaturityLevel>(MaturityLevel.values().length);
        for(MaturityLevel level : MaturityLevel.values()) {
            levels.add(level);
        }
        return levels;
    }

    @ModelAttribute("teamRoles")
    public Map<String, String> populateTeamRoles() {
        Map<String, String> roles = new HashMap<String, String>();
        for (TeamRole role : TeamRole.values()) {
            roles.put(role.toString(), LangProvider.getString("team-role-"+role.toString().toLowerCase()));
        }
        return roles;
    }

    // Project list

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String displayProjects(ModelMap model, HttpSession session) {
        if(session.getAttribute(Attribute.ORG)!=null) {
            model.addAttribute(Attribute.ORG, organizationDao.read(((Organization)session.getAttribute(Attribute.ORG)).getId()));
        }
        return PROJ_LIST;
    }

    @RequestMapping(method = RequestMethod.POST, value ="/")
    public String processSubmitAdd(@RequestParam("orgId") Long id, ModelMap model, SessionStatus status) {
        if(id != null) {
            Organization org = organizationDao.read(id);
            model.addAttribute(Attribute.ORG, org);
        }
        return PROJ_LIST;
    }

    // Project edit - page 1

    @RequestMapping(method = RequestMethod.GET, value = "/add.do")
    public String setupFormAdd(@ModelAttribute(Attribute.ORG) Organization org, ModelMap model) {
        Project project = new Project();
        project.setNewProject(true);
        project.setTeam(new HashSet<TeamMember>());
        project.setOrganization(org);
        model.addAttribute(Attribute.PROJECT, project);
        return PROJ_FORM;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/edit-{projectId}.do")
    public String setupFormEdit(@PathVariable("projectId") String projectId,
            ModelMap model) {
        Project project = projectDao.read(projectId);
        project.setNewProject(false);
        model.addAttribute(Attribute.PROJECT, project);
        return PROJ_FORM;
    }

    @RequestMapping(method = RequestMethod.POST, value="/save-project.do")
    public String saveProject(@ModelAttribute(Attribute.PROJECT) Project project, BindingResult result, ModelMap model, SessionStatus status) {
        new ProjectValidator(projectDao).validate(project, result);
        if(result.hasErrors()) {
            return PROJ_FORM;
        }
        if(project.isNewProject()) {
            project = projectService.createProject(project);
        } else {
            project = projectDao.update(project);
        }
        model.addAttribute(Attribute.PROJECT, project);
        model.addAttribute(Attribute.USERS, getAvailAbleUsers(project));
        return PROJ_FORM_USERS;
    }

    // Project edit - page 2

    @RequestMapping(method = RequestMethod.POST, value="/add-member.do")
    public String addMember(@ModelAttribute("teamMember") TeamMember teamMember,BindingResult result, 
            @ModelAttribute(Attribute.PROJECT) Project project, BindingResult noresult, ModelMap model, SessionStatus status) {
        new TeamMemberValidator(project).validate(teamMember, result);
        if (result.hasErrors()) {
            return PROJ_FORM_USERS;
        }

        project = projectService.addMember(teamMember);

        model.addAttribute(Attribute.PROJECT, project);
        model.addAttribute(Attribute.USERS, getAvailAbleUsers(project));
        model.addAttribute("saved", Boolean.TRUE);
        return PROJ_FORM_USERS;
    }

    @RequestMapping(method = RequestMethod.GET, value="/remove-member-{memberId}.do")
    public String removeMember(@PathVariable("memberId") Long memberId, @ModelAttribute(Attribute.PROJECT) Project project,
            ModelMap model) {

        project = projectService.removeTeamMember(memberId);

        model.addAttribute(Attribute.PROJECT, project);
        model.addAttribute(Attribute.USERS, getAvailAbleUsers(project));
        model.addAttribute("saved", Boolean.TRUE);
        return PROJ_FORM_USERS;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/finish-team.do")
    public String finishTeam(ModelMap model) {
        model.remove(Attribute.PROJECT);
        return "redirect:/qmanager/";
    }

    // Other project actions

    @RequestMapping(method = RequestMethod.GET, value = "/delete-{projectId}.do")
    public String deleteProject(@PathVariable("projectId") String projectId, ModelMap model) {
        projectService.removeProject(projectId);
        return "redirect:/qmanager/";
    }


    private List<User> getAvailAbleUsers(Project project) {
        List<User> availableUsers = userDao.findActive();
        //project = projectDao.read(project.getId());
        Iterator<User> it = availableUsers.iterator();
        while (it.hasNext()) {
            User user = it.next();
            for (TeamMember member : project.getTeam()) {
                if (member.getUser().equals(user)) {
                    it.remove();
                    break;
                }
            }
        }
        return availableUsers;
    }

    private static final Log log = LogFactory.getLog(QMController.class);

}
