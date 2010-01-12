/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller;

import cz.strmik.cmmitool.dao.UserDao;
import cz.strmik.cmmitool.entity.User;
import cz.strmik.cmmitool.enums.ApplicationRole;
import cz.strmik.cmmitool.web.lang.LangProvider;
import cz.strmik.cmmitool.util.validator.UserValidator;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
@RequestMapping("/admin/users")
@SessionAttributes("user")
public class UsersController {

    private final UserDao userDao;

    private static final String USER_LIST = "/admin/users/list";
    private static final String USER_FORM = "/admin/users/form";

    @Autowired
    public UsersController(UserDao userDao) {
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

    @RequestMapping(method = RequestMethod.GET, value = "/edit-{userId}.do")
    public String setupFormEdit(@PathVariable("userId") String userId, ModelMap model) {
        if (!model.containsAttribute("user")) {
            User user = userDao.findUser(userId);
            model.addAttribute("user", user);
        }
        return USER_FORM;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/add.do")
    public String setupFormAdd(ModelMap model) {
        if (!model.containsAttribute("user")) {
            User user = new User();
            user.setNewUser(true);
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            model.addAttribute("user", user);
        }
        return USER_FORM;
    }

    @RequestMapping(method = RequestMethod.POST, value ="/add.do")
    public String processSubmitAdd(@ModelAttribute("user") User user, BindingResult result, ModelMap model, SessionStatus status) {
        new UserValidator(userDao).validate(user, result);
        if (result.hasErrors()) {
            return USER_FORM;
        }
        userDao.createUser(user);
        status.setComplete();
        model.addAttribute("users", userDao.findAll());
        return USER_LIST;
    }

    @RequestMapping(method = RequestMethod.POST, value ="/edit-{userId}.do")
    public String processSubmitEdit(@ModelAttribute("user") User user, BindingResult result, ModelMap model, SessionStatus status) {
        new UserValidator(userDao).validate(user, result);
        if(getLoggeduserId().equals(user.getUsername()) && (!user.isEnabled()||
                !user.isAccountNonExpired()||!user.isCredentialsNonExpired())) {
            result.rejectValue("enabled", "can-not-disable-yourself");
        }
        if (result.hasErrors()) {
            return USER_FORM;
        }
        userDao.updateUser(user);
        status.setComplete();
        model.addAttribute("users", userDao.findAll());
        return USER_LIST;
    }

    @RequestMapping("/delete-{userId}.do")
    public String deleteUser(@PathVariable("userId") String userId, ModelMap model) {
        if(getLoggeduserId().equals(userId)) {
            throw new IllegalArgumentException("Can not remove yourself!");
        }
        userDao.removeUser(userId);
        model.addAttribute("users", userDao.findAll());
        return USER_LIST;
    }

    private String getLoggeduserId() {
        String loggedUserId = ((UserDetails)
                SecurityContextHolder.getContext().getAuthentication().
                getPrincipal()).getUsername();

        return loggedUserId;
    }

    @RequestMapping("/")
    public String manageUsers(ModelMap model) {
        model.addAttribute("users", userDao.findAll());
        return USER_LIST;
    }

    private static final Log log = LogFactory.getLog(UsersController.class);

}
