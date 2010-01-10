/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller;

import cz.strmik.cmmitool.security.SecurityContextFacade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/")
public class WelcomeController extends AbstractController {

    private static final String WELCOME_PAGE = "index";

    @Autowired
    private SecurityContextFacade securityContextFacade;

    @RequestMapping(method = RequestMethod.GET, value = "dashboard.do")
    public String getWelcomePage(ModelMap model) {
        Authentication auth = securityContextFacade.getContext().getAuthentication();
        if(auth!=null) {
            model.addAttribute("authenticated", getLoggedUser()!=null);
        }
        return WELCOME_PAGE;
    }

    private static final Log log = LogFactory.getLog(WelcomeController.class);

}
