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
import cz.strmik.cmmitool.util.validator.OrganizationValidator;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/organizations")
@SessionAttributes({"org","create"})
public class OrganizationController {

    private GenericDao<Organization, Long> organizationDao;

    private static final String ORG_LIST = "/admin/organizations/list";
    private static final String ORG_FORM = "/admin/organizations/form";

    @Autowired
    public void setOrganizationDao(GenericDao<Organization, Long> organizationDao) {
        this.organizationDao = organizationDao;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/edit-{orgId}.do")
    public String setupFormEdit(@PathVariable("orgId") Long orgId, ModelMap model) {
        if (!model.containsAttribute("org")) {
            Organization org = organizationDao.read(orgId);
            model.addAttribute("org", org);
            model.addAttribute("create", Boolean.FALSE);
        }
        return ORG_FORM;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/add.do")
    public String setupFormAdd(ModelMap model) {
        if (!model.containsAttribute("org")) {
            Organization org = new Organization();
            org.setActive(true);
            model.addAttribute("org", org);
            model.addAttribute("create", Boolean.TRUE);
        }
        return ORG_FORM;
    }

    @RequestMapping("/")
    public String manageOrgs(ModelMap model) {
        model.addAttribute("orgs", organizationDao.findAll());
        return ORG_LIST;
    }

    @RequestMapping("/delete-{orgId}.do")
    public String deleteOrgnaization(@PathVariable("orgId") Long orgId, ModelMap model) {
        organizationDao.delete(orgId);
        model.addAttribute("orgs", organizationDao.findAll());
        return ORG_LIST;
    }

    @RequestMapping(method = RequestMethod.POST, value ="/add.do")
    public String processSubmitAdd(@ModelAttribute("org") Organization org, BindingResult result, ModelMap model, SessionStatus status) {
        new OrganizationValidator().validate(org, result);
        if (result.hasErrors()) {
            return ORG_FORM;
        }
        organizationDao.create(org);
        status.setComplete();
        model.addAttribute("orgs", organizationDao.findAll());
        return ORG_LIST;
    }

    @RequestMapping(method = RequestMethod.POST, value ="/edit-{orgId}.do")
    public String processSubmitEdit(@ModelAttribute("org") Organization org, BindingResult result, ModelMap model, SessionStatus status) {
        new OrganizationValidator().validate(org, result);
        if (result.hasErrors()) {
            return ORG_FORM;
        }
        organizationDao.update(org);
        status.setComplete();
        model.addAttribute("orgs", organizationDao.findAll());
        return ORG_LIST;
    }

    private static final Log log = LogFactory.getLog(OrganizationController.class);

}
