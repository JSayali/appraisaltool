/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.util.validator;

import cz.strmik.cmmitool.entity.Organization;
import java.util.regex.Matcher;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class OrganizationValidator extends AbstractValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Organization.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "field-required");
        Organization org = (Organization) target;
        if(!StringUtils.isEmpty(org.getEmail())) {
            Matcher emailMatcher = PATTERN_EMAIL.matcher(org.getEmail());
            if(!emailMatcher.matches()) {
                errors.rejectValue("email", "invalid-email");
            }
        }
    }

}
