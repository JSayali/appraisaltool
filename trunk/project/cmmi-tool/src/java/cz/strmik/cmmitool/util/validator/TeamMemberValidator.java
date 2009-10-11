/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.util.validator;

import cz.strmik.cmmitool.entity.project.Project;
import cz.strmik.cmmitool.entity.project.TeamMember;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class TeamMemberValidator extends AbstractValidator {

    private Project project;

    public TeamMemberValidator(Project project) {
        this.project = project;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return TeamMember.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "user", "field-required");
        ValidationUtils.rejectIfEmpty(errors, "project", "field-required");
        ValidationUtils.rejectIfEmpty(errors, "teamRole", "field-required");

        // when new, chcek if user is not already assigned on project
        TeamMember member = (TeamMember) target;
        if(member.getId()==null && project.getTeam()!=null) {
            for(TeamMember m : project.getTeam()) {
                if(m.getUser().equals(member.getUser())) {
                    errors.rejectValue("user", "user-already-assigned");
                }
            }
        }

    }

}
