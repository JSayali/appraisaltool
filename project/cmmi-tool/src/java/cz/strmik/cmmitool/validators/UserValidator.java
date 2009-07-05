/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.validators;

import cz.strmik.cmmitool.dao.UserDao;
import cz.strmik.cmmitool.entity.User;
import java.util.regex.Matcher;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class UserValidator extends AbstractValidator {


    private final UserDao userDao;

    public UserValidator(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field-required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "role", "field-required");

        User user = (User) target;
        if(user.isNewUser()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field-required");
        }
        if(user.getPassword()!=null && !user.getPassword().equals(user.getPassword2())) {
            errors.rejectValue("password2", "passwords-must-match");
        }
        Matcher emailMatcher = PATTERN_EMAIL.matcher(user.getEmail());
        if(!emailMatcher.matches()) {
            errors.rejectValue("email", "invalid-email");
        }
        Matcher idMatcher = PATTERN_ID.matcher(user.getId());
        if(!idMatcher.matches()) {
            errors.rejectValue("id", "invalid-login");
        }
        if(userDao.findUser(user.getId())!=null && user.isNewUser()) {
            errors.rejectValue("id", "duplicate-login");
        }
        if(userDao.findUser(user.getId())==null && !user.isNewUser()) {
            errors.rejectValue("id", "login-can-not-be-changed");
        }
        if(StringUtils.containsWhitespace(user.getId())) {
            errors.rejectValue("id", "can-not-contain-whitespaces");
        }
        if(user.getRole()!=null && user.getRole().equals("-")) {
            errors.rejectValue("role", "field-required");
        }
    }

}
