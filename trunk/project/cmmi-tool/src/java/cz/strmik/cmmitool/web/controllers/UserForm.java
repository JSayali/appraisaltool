/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controllers;

import cz.strmik.cmmitool.dao.UserDao;
import cz.strmik.cmmitool.entity.User;
import cz.strmik.cmmitool.enums.ApplicationRole;
import cz.strmik.cmmitool.web.lang.LangProvider;
import cz.strmik.cmmitool.validators.UserValidator;
import java.util.HashMap;
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
@RequestMapping("/admin/editUser.do")
@SessionAttributes("user")
public class UserForm {

    private final UserDao userDao;

    @Autowired
    public UserForm(UserDao userDao) {
        this.userDao = userDao;
    }

    @ModelAttribute("roles")
    public Map<String, String> populateUserRoles() {
        Map<String, String> roles = new HashMap<String, String>();
        for (ApplicationRole role : ApplicationRole.values()) {
            roles.put(role.toString(), LangProvider.getString(role.toString().toLowerCase()));
        }
        return roles;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String setupForm(ModelMap model) {
        if (!model.containsAttribute("userId")&&!model.containsAttribute("user")) {
            User user = new User();
            user.setNewUser(true);
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            model.addAttribute("user", user);
        } else {
            User user = userDao.findUser((String)model.get("user"));
            model.addAttribute("user", user);
        }
        return "/admin/userForm";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(@ModelAttribute("user") User user, BindingResult result, SessionStatus status) {
        new UserValidator(userDao).validate(user, result);
        if (result.hasErrors()) {
            return "/admin/userForm";
        }
        if (user.isNewUser()) {
            userDao.createUser(user);
        } else {
            userDao.updateUser(user);
        }
        status.setComplete();
        return "/admin/dashboard";
    }

}
