/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.util.validator;

import cz.strmik.cmmitool.entity.AcronymEntity;
import java.util.regex.Matcher;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class AcronymValidator extends AbstractValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return AcronymEntity.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "acronym", "field-required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field-required");

        AcronymEntity acronym = (AcronymEntity) target;
        Matcher idMatcher = PATTERN_ID.matcher(acronym.getAcronym());
        if(!idMatcher.matches()) {
            errors.rejectValue("acronym", "invalid-acronym");
        }
    }

}
