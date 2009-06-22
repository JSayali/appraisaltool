/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controllers;

import cz.strmik.cmmitool.entity.User;
import cz.strmik.cmmitool.enums.ApplicationRole;
import java.util.HashMap;
import java.util.Map;
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

    @RequestMapping(ADMIN)
    public String dashboard(Model model) {
        return ADMIN + "dashboard";
    }

    @RequestMapping(ADMIN+"addUser.do")
    public String addUser(Model model) {
        model.addAttribute("new", Boolean.TRUE);
        Map<String, String> roles = new HashMap<String, String>();
        for(ApplicationRole role : ApplicationRole.values()) {
            roles.put(role.toString(), "Role: "+role);
        }
        model.addAttribute("roleList", roles);
        model.addAttribute("user", new User());
        return ADMIN + "userForm";
    }

}
