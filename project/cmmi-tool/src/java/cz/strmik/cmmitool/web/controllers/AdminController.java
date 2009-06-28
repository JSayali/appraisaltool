/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controllers;

import cz.strmik.cmmitool.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Controller
public class AdminController {

    private static final String ADMIN = "/admin/";

    @Autowired
    private UserDao userDao;

    @RequestMapping(ADMIN+"dashboard.do")
    public String dashboard(Model model) {
        return ADMIN + "dashboard";
    }

}
