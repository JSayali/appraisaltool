/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.util.validator;

import cz.strmik.cmmitool.entity.model.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class ModelValidator extends AbstractValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Model.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "acronym", "field-required");
        ValidationUtils.rejectIfEmpty(errors, "name", "field-required");
        ValidationUtils.rejectIfEmpty(errors, "description", "field-required");
        ValidationUtils.rejectIfEmpty(errors, "highestML", "field-required");
    }

}
