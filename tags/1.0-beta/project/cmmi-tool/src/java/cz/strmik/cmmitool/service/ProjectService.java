/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.entity.project.ProcessInstantiation;
import cz.strmik.cmmitool.entity.project.Project;
import cz.strmik.cmmitool.entity.project.TeamMember;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public interface ProjectService {
    
    Project createProject(Project project);

    void removeProject(String projectId);

    Project addMember(TeamMember teamMember);

    Project removeTeamMember(long id);

    Project addPI(ProcessInstantiation pi);
    Project removePI(long id);

}
