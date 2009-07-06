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
import cz.strmik.cmmitool.web.wrapper.OrganizationWrapper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@SessionAttributes("wrapper")
public class QMController {

    private final UserDao userDao;
    private GenericDao<Organization, Long> organizationDao;
    private GenericDao<Method, String> methodDao;
    private GenericDao<Model, String> modelDao;
    
    private static final String PROJ_LIST = "/qmanager/projectList";
    private static final String PROJ_FORM = "/qmanager/projectForm";

    @Autowired
    public void setMethodDao(GenericDao<Method, String> methodDao) {
        this.methodDao = methodDao;
    }

    @Autowired
    public void setModelDao(GenericDao<Model, String> modelDao) {
        this.modelDao = modelDao;
    }

    @Autowired
    public void setOrganizationDao(GenericDao<Organization, Long> organizationDao) {
        this.organizationDao = organizationDao;
    }

    @Autowired
    public QMController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String displayProjects(ModelMap model) {
        if (!model.containsAttribute("wrapper")) {
            OrganizationWrapper ow = new OrganizationWrapper();
            List<Organization> orgs = organizationDao.findByNamedQuery("findActive");
            Map<Long, String> aviableOrgs = new HashMap<Long, String>();
            for (Organization org : orgs) {
                aviableOrgs.put(org.getId(),org.getName());
            }
            ow.setAviableOrganizations(aviableOrgs);
            model.addAttribute("wrapper", ow);
        }
        return PROJ_LIST;
    }

    @RequestMapping(method = RequestMethod.POST, value ="/")
    public String processSubmitAdd(@ModelAttribute("wrapper") OrganizationWrapper wrapper, BindingResult result, ModelMap model, SessionStatus status) {
        if(wrapper.getSelectedOrganizationId()==null) {
            wrapper.setProjects(null);
        } else {
            Organization org = organizationDao.read(wrapper.getSelectedOrganizationId());
            List<Project> projects = org.getProjects();
            wrapper.setProjects(projects);
            wrapper.setSelectedOrganizationName(org.getName());
        }
        return PROJ_LIST;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/add.do")
    public String setupFormAdd(@ModelAttribute("wrapper") OrganizationWrapper wrapper, ModelMap model) {
        wrapper.setCreate(true);
        wrapper.setProject(new Project());
        fillOptions(wrapper);
        return PROJ_FORM;
    }

    private void fillOptions(OrganizationWrapper wrapper) {
        wrapper.setUsers(userDao.findActive());
        Map<String, Method> methodMap = new HashMap<String, Method>();
        List<Method> methods = methodDao.findAll();
        for(Method m : methods) {
            methodMap.put(m.getId(), m);
        }
        wrapper.setMethods(methodMap);
        Map<String, Model> modelMap = new HashMap<String, Model>();
        List<Model> models = modelDao.findAll();
        for(Model m : models) {
            modelMap.put(m.getId(), m);
        }
        wrapper.setModels(modelMap);
    }


}
