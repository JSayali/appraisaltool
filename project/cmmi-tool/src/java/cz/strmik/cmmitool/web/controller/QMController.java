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
import cz.strmik.cmmitool.enums.TeamRole;
import cz.strmik.cmmitool.util.validator.ProjectValidator;
import cz.strmik.cmmitool.util.validator.TeamMemberValidator;
import cz.strmik.cmmitool.web.lang.LangProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/qmanager")
@SessionAttributes({"project","organization"})
public class QMController {

    private final UserDao userDao;
    @Autowired
    private GenericDao<Organization, Long> organizationDao;
    @Autowired
    private GenericDao<Method, String> methodDao;
    @Autowired
    private GenericDao<Model, String> modelDao;
    @Autowired
    private GenericDao<Project, String> projectDao;
    @Autowired
    private GenericDao<TeamMember, Long> teamMemberDao;
    
    private static final String PROJ_LIST = "/qmanager/projectList";
    private static final String PROJ_FORM = "/qmanager/projectForm1";
    private static final String PROJ_FORM_USERS = "/qmanager/projectForm2";

    // Acces to DAOs

    @Autowired
    public QMController(UserDao userDao) {
        this.userDao = userDao;
    }

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
        teamMember.setProject((Project) session.getAttribute("project"));
        return teamMember;
    }

    @ModelAttribute("users")
    public List<User> getUsers(HttpSession session) {
        List<User> availableUsers = new ArrayList<User>();
        Project project = (Project) session.getAttribute("project");
        System.err.println("project = "+project);
        if (project != null) {
            List<User> users = userDao.findActive();
            boolean found = false;
            for (User user : users) {
                if(project.getTeam()!=null) {
                    for (TeamMember member : project.getTeam()) {
                        if (member.getUser().equals(user)) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    availableUsers.add(user);
                }
                found = false;
            }
        }
        return availableUsers;
    }

    @ModelAttribute("methods")
    public List<Method> getMethods() {
        return methodDao.findAll();
    }

    @ModelAttribute("models")
    public List<Model> getModels() {
        return  modelDao.findAll();
    }

    @ModelAttribute("teamRoles")
    public Map<String, String> populateTeamRoles() {
        Map<String, String> roles = new HashMap<String, String>();
        for (TeamRole role : TeamRole.values()) {
            roles.put(role.toString(), LangProvider.getString(role.toString().toLowerCase()));
        }
        return roles;
    }

    // Project list

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String displayProjects(ModelMap model) {
        Organization org = new Organization();
        model.addAttribute("organization", org);
        return PROJ_LIST;
    }

    @RequestMapping(method = RequestMethod.POST, value ="/")
    public String processSubmitAdd(@ModelAttribute("organization") Organization org, BindingResult result, ModelMap model, SessionStatus status) {
        Long id = org.getId();
        model.addAttribute("organization", new Organization(organizationDao.read(id)));
        return PROJ_LIST;
    }

    // Project edit - page 1

    @RequestMapping(method = RequestMethod.GET, value = "/add.do")
    public String setupFormAdd(@ModelAttribute("organization") Organization org, ModelMap model) {
        Project project = new Project();
        project.setNewProject(true);
        project.setOrganization(organizationDao.read(org.getId()));
        model.addAttribute("project", project);
        return PROJ_FORM;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/edit-{projectId}.do")
    public String setupFormEdit(@PathVariable("projectId") String projectId, ModelMap model) {
        Project project = projectDao.read(projectId);
        model.addAttribute("project", project);
        return PROJ_FORM;
    }

    @RequestMapping(method = RequestMethod.POST, value="/save-project.do")
    public String saveProject(@ModelAttribute("project") Project project,  BindingResult result, ModelMap model, SessionStatus status) {
        new ProjectValidator().validate(project, result);
        if(result.hasErrors()) {
            return PROJ_FORM;
        }
        if(project.isNewProject()) {
            projectDao.create(project);
        } else {
            projectDao.update(project);
        }
        return PROJ_FORM_USERS;
    }

    // Project edit - page 2

    @RequestMapping(method = RequestMethod.POST, value="/add-member.do")
    public String addMember(@ModelAttribute("teamMember") TeamMember teamMember,BindingResult result, @ModelAttribute("project") Project project,
            BindingResult noresult, ModelMap model, SessionStatus status) {
        new TeamMemberValidator().validate(teamMember, result);
        if (result.hasErrors()) {
            return PROJ_FORM_USERS;
        }
        project.getTeam().add(teamMember);
        teamMemberDao.create(teamMember);
        projectDao.update(project);
        return PROJ_FORM_USERS;
    }

    @RequestMapping(method = RequestMethod.GET, value="/remove-member-{userId}.do")
    public String removeMember(@PathVariable("userId") String userId, @ModelAttribute("project") Project project, ModelMap model) {
        Iterator<TeamMember> it = project.getTeam().iterator();
        TeamMember member = null;
        while(it.hasNext()) {
            member = it.next();
            if(member.getUser().getId().equals(userId)) {
                it.remove();
                break;
            }
        }
        projectDao.update(project);
        teamMemberDao.delete(member.getId());
        return PROJ_FORM;
    }

}
