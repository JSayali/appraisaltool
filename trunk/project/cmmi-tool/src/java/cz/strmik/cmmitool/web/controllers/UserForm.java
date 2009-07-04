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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/admin/users/")
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

    @RequestMapping(method = RequestMethod.GET, value = "edit-{userId}.do")
    public String setupFormEdit(@PathVariable("userId") String userId, ModelMap model) {
        if (!model.containsAttribute("user")) {
            User user = userDao.findUser(userId);
            model.addAttribute("user", user);
        }
        return "/admin/users/form";
    }

    @RequestMapping(method = RequestMethod.GET, value = "add.do")
    public String setupFormAdd(ModelMap model) {
        if (!model.containsAttribute("user")) {
            User user = new User();
            user.setNewUser(true);
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            model.addAttribute("user", user);
        }
        return "/admin/users/form";
    }

    @RequestMapping(method = RequestMethod.POST, value ="add.do")
    public String processSubmitAdd(@ModelAttribute("user") User user, BindingResult result, SessionStatus status) {
        new UserValidator(userDao).validate(user, result);
        if (result.hasErrors()) {
            return "/admin/users/form";
        }
        userDao.createUser(user);
        status.setComplete();
        return "/admin/dashboard";
    }

    @RequestMapping(method = RequestMethod.POST, value ="edit-{userId}.do")
    public String processSubmitEdit(@ModelAttribute("user") User user, BindingResult result, ModelMap model, SessionStatus status) {
        new UserValidator(userDao).validate(user, result);
        if (result.hasErrors()) {
            return "/admin/users/form";
        }
        userDao.updateUser(user);
        status.setComplete();
        model.addAttribute("users", userDao.findAll());
        return "/admin/users/list";
    }

    @RequestMapping("delete-{userId}.do")
    public String deleteUser(@PathVariable("userId") String userId, ModelMap model) {
        String loggedUserId = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        if(loggedUserId.equals(userId)) {
            throw new IllegalArgumentException("Can not remove yourself!");
        }
        userDao.removeUser(userId);
        model.addAttribute("users", userDao.findAll());
        return "/admin/users/list";
    }

}
