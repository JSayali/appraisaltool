/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.dao.UserDao;
import cz.strmik.cmmitool.entity.Organization;
import cz.strmik.cmmitool.entity.project.Project;
import cz.strmik.cmmitool.entity.project.TeamMember;
import cz.strmik.cmmitool.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private GenericDao<Organization, Long> organizationDao;
    @Autowired
    private GenericDao<Project, String> projectDao;
    @Autowired
    private GenericDao<TeamMember, Long> teamMemberDao;

    @Override
    public Project addMember(TeamMember teamMember) {
        
        User user = userDao.findUser(teamMember.getUser().getId());
        Project project = teamMember.getProject();

        teamMember.setProject(project);
        teamMember.setUser(user);

        if(!project.getTeam().contains(teamMember)) {
            project.getTeam().add(teamMember);
        }

        if(!user.getMemberOfTeams().contains(teamMember)) {
            user.getMemberOfTeams().add(teamMember);
        }

        teamMemberDao.create(teamMember);
        userDao.updateUser(user);
        return projectDao.update(project);
    }

    @Override
    public Project removeTeamMember(long id) {

        TeamMember teamMember = teamMemberDao.read(id);
        User user = teamMember.getUser();
        Project project = teamMember.getProject();

        user.getMemberOfTeams().remove(teamMember);
        project.getTeam().remove(teamMember);

        teamMemberDao.delete(teamMember.getId());
        userDao.updateUser(user);
        return projectDao.update(project);
    }

    @Override
    public void removeProject(String projectId) {

        Project project = projectDao.read(projectId);

        Organization org = project.getOrganization();
        org.getProjects().remove(project);
        
        projectDao.delete(projectId);
        organizationDao.update(org);
    }

    @Override
    public Project createProject(Project project) {

        Organization org = project.getOrganization();

        if(!org.getProjects().contains(project)) {
            org.getProjects().add(project);
        }
        project.setOrganization(org);

        return projectDao.create(project);
    }

}
