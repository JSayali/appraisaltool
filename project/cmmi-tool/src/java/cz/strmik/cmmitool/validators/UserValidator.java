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
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class UserValidator implements Validator {

    private static Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");

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
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field-required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field-required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "field-required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "field-required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "role", "field-required");

        User user = (User) target;
        if(user.getPassword()!=null && !user.getPassword().equals(user.getPassword2())) {
            errors.rejectValue("password2", "passwords-must-match");
        }
        Matcher matcher = emailPattern.matcher(user.getEmail());
        if(!matcher.matches()) {
            errors.rejectValue("email", "invalid-email");
        }
        if(userDao.findUser(user.getId())!=null && user.isNewUser()) {
            errors.rejectValue("id", "duplicate-login");
        }
        if(userDao.findUser(user.getId())==null && !user.isNewUser()) {
            errors.rejectValue("id", "invalid-login");
        }
        if(StringUtils.containsWhitespace(user.getId())) {
            errors.rejectValue("id", "can-not-contain-whitespaces");
        }
        if(user.getRole()!=null && user.getRole().equals("-")) {
            errors.rejectValue("role", "field-required");
        }

    }

}
