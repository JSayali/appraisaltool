/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.entity.Artifact;
import cz.strmik.cmmitool.entity.Goal;
import cz.strmik.cmmitool.entity.Model;
import cz.strmik.cmmitool.entity.Practice;
import cz.strmik.cmmitool.entity.ProcessArea;
import cz.strmik.cmmitool.entity.ProcessGroup;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public interface ModelService {

    Model addGroup(ProcessGroup processGroup);

    Model addProcess(ProcessArea process);

    Model addGoal(Goal goal);

    Model addPractice(Practice practice);

    Model addArtifact(Artifact artifact);
    

    Model saveProcess(ProcessArea process);

    Model saveGoal(Goal goal);

    Model savePractice(Practice practice);

    Model saveArtifact(Artifact artifact);


    Model removeGroup(long id);

    Model removeProcess(long id);

    Model removeGoal(long id);

    Model removePractice(long id);

    Model removeArtifact(long id);

}
