/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.util.validator;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.Project;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class ProjectValidator extends AbstractValidator {

    private GenericDao<Project, String> projectDao;

    public ProjectValidator(GenericDao<Project, String> projectDao) {
        this.projectDao = projectDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Project.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "id", "field-required");
        ValidationUtils.rejectIfEmpty(errors, "name", "field-required");
        ValidationUtils.rejectIfEmpty(errors, "method", "field-required");
        ValidationUtils.rejectIfEmpty(errors, "model", "field-required");
        ValidationUtils.rejectIfEmpty(errors, "organization", "field-required");
        ValidationUtils.rejectIfEmpty(errors, "targetML", "field-required");

        Project project = (Project) target;
        if(project.isNewProject() && projectDao.read(project.getId())!=null) {
            errors.rejectValue("id", "duplicate-id");
        }

        if(project.getTargetML()!=null && project.getModel()!=null &&
                project.getTargetML().getLevel() > project.getModel().getHighestML().getLevel()) {
            errors.rejectValue("targetML", "ml-is-higher-than-model-allows");
        }
        
    }

}
