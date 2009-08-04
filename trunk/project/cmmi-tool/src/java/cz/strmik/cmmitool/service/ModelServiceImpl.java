/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.Artifact;
import cz.strmik.cmmitool.entity.Goal;
import cz.strmik.cmmitool.entity.Model;
import cz.strmik.cmmitool.entity.Practice;
import cz.strmik.cmmitool.entity.ProcessArea;
import cz.strmik.cmmitool.entity.ProcessGroup;
import java.util.HashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class ModelServiceImpl implements ModelService {

    @Autowired
    private GenericDao<ProcessGroup, Long> processGroupDao;
    @Autowired
    private GenericDao<ProcessArea, String> processAreaDao;
    @Autowired
    private GenericDao<Model, String> modelDao;
    @Autowired
    private GenericDao<Goal, String> goalDao;
    @Autowired
    private GenericDao<Practice, String> practiceDao;
    @Autowired
    private GenericDao<Artifact, String> artifactDao;

    @Override
    public Model addGroup(ProcessGroup processGroup) {
        if(processGroup.getModel()==null) {
            throw new IllegalArgumentException("processGroup does not have specified model");
        }
        processGroup = processGroupDao.create(processGroup);
        Model model = processGroup.getModel();
        if(model.getProcessGroups()==null) {
            model.setProcessGroups(new HashSet<ProcessGroup>());
        }
        if(!model.getProcessGroups().contains(processGroup)) {
            model.getProcessGroups().add(processGroup);
            modelDao.update(model);
        }
        return model;
    }

    @Override
    public Model addProcess(ProcessArea process) {
        if(process.getModel()==null) {
            throw new IllegalArgumentException("process does not have specified model");
        }
        Model model = process.getModel();
        process = processAreaDao.create(process);
        if(model.getProcessAreas()==null) {
            model.setProcessAreas(new HashSet<ProcessArea>());
        }
        if(!model.getProcessAreas().contains(process)) {
            model.getProcessAreas().add(process);
            modelDao.update(model);
        }
        return model;
    }

    @Override
    public Model addGoal(Goal goal) {
        if(goal.getProcessArea()==null) {
            throw new IllegalArgumentException("goal does not have specified model");
        }
        ProcessArea process = goal.getProcessArea();
        goal = goalDao.create(goal);
        if(process.getGoals()==null) {
            process.setGoals(new HashSet<Goal>());
        }
        if(!process.getGoals().contains(goal)) {
            process.getGoals().add(goal);
            process = processAreaDao.update(process);
        }
        return process.getModel();
    }

    @Override
    public Model addPractice(Practice practice) {
        if(practice.getGoal()==null) {
            throw new IllegalArgumentException("practice does not have specified goal");
        }
        Goal goal = practice.getGoal();
        practice = practiceDao.create(practice);
        if(goal.getPractices()==null) {
            goal.setPractices(new HashSet<Practice>());
        }
        if(!goal.getPractices().contains(practice)) {
            goal.getPractices().add(practice);
            goal = goalDao.update(goal);
        }
        return goal.getProcessArea().getModel();
    }

    @Override
    public Model addArtifact(Artifact artifact) {
        if(artifact.getPractice()==null) {
            throw new IllegalArgumentException("artifact does not have specified practice");
        }
        Practice practice = artifact.getPractice();
        artifact = artifactDao.create(artifact);
        if(practice.getArtifacts()==null) {
            practice.setArtifacts(new HashSet<Artifact>());
        }
        if(!practice.getArtifacts().contains(artifact)) {
            practice.getArtifacts().add(artifact);
            practice = practiceDao.update(practice);
        }
        return practice.getGoal().getProcessArea().getModel();
    }

    @Override
    public Model removeGroup(long id) {
        ProcessGroup group = processGroupDao.read(id);
        Model model = group.getModel();
        model.getProcessGroups().remove(group);
        group.setModel(null);        
        processGroupDao.delete(id);
        return modelDao.update(model);
    }

    @Override
    public Model removeProcess(String id) {
        ProcessArea process = processAreaDao.read(id);
        Model model = process.getModel();
        model.getProcessAreas().remove(process);
        process.setModel(null);
        processAreaDao.delete(id);
        return modelDao.update(model);
    }

    @Override
    public Model removeGoal(String id) {
        Goal goal = goalDao.read(id);
        ProcessArea process = goal.getProcessArea();
        process.getGoals().remove(goal);
        goal.setProcessArea(null);
        goalDao.delete(id);
        return processAreaDao.update(process).getModel();
    }

    @Override
    public Model removePractice(String id) {
        Practice practice = practiceDao.read(id);
        Goal goal = practice.getGoal();
        goal.getPractices().remove(practice);
        practice.setGoal(null);
        practiceDao.delete(id);
        return goalDao.update(goal).getProcessArea().getModel();
    }

    @Override
    public Model removeArtifact(String id) {
        Artifact artifact = artifactDao.read(id);
        Practice practice = artifact.getPractice();
        practice.getArtifacts().remove(artifact);
        artifact.setPractice(null);
        artifactDao.delete(id);
        return practiceDao.update(practice).getGoal().getProcessArea().getModel();
    }

    private static final Log _log = LogFactory.getLog(ModelServiceImpl.class);

}
