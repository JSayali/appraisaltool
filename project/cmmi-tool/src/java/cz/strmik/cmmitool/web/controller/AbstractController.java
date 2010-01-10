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
import cz.strmik.cmmitool.entity.project.Project;
import cz.strmik.cmmitool.entity.project.TeamMember;
import cz.strmik.cmmitool.enums.MaturityLevel;
import cz.strmik.cmmitool.enums.TeamRole;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public abstract class AbstractController {
    
    @Autowired
    private UserDao userDao;

    public User getLoggedUser() {
        User loggedUser = null;
        Object principal = null;
        try {
            principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String loggedUserId = userDetails.getUsername();
                loggedUser = userDao.findUser(loggedUserId);
            }
            
        }catch (Exception ex) {
            throw new RuntimeException("Unable to get logged user! Principal="+principal, ex);
        }
        return loggedUser;
    }

    protected Collection<MaturityLevel> getMLLevels() {
        List<MaturityLevel> levels = new ArrayList<MaturityLevel>(MaturityLevel.values().length);
        for(MaturityLevel level : MaturityLevel.values()) {
            levels.add(level);
        }
        return levels;
    }
    
    protected void checkIsAudior(Project project) {
        User user = getLoggedUser();
        for(TeamMember tm : project.getTeam()) {
            if(tm.getUser().equals(user)) {
                if(tm.getTeamRole().equals(TeamRole.AUDITOR)) {
                    return;
                } else {
                    break;
                }
            }
        }
        throw new SecurityException("User "+user+" is not authorized to audit project "+project);
    }

}
