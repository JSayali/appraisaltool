/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.model.Artifact;
import cz.strmik.cmmitool.entity.model.Goal;
import cz.strmik.cmmitool.entity.model.Model;
import cz.strmik.cmmitool.entity.model.Practice;
import cz.strmik.cmmitool.entity.model.ProcessArea;
import cz.strmik.cmmitool.entity.model.ProcessGroup;
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
    private GenericDao<ProcessArea, Long> processAreaDao;
    @Autowired
    private GenericDao<Model, Long> modelDao;
    @Autowired
    private GenericDao<Goal, Long> goalDao;
    @Autowired
    private GenericDao<Practice, Long> practiceDao;
    @Autowired
    private GenericDao<Artifact, Long> artifactDao;

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
        if(goal.getProcessArea()==null && goal.getModel()==null) {
            throw new IllegalArgumentException("goal does not have specified model or process area");
        }
        if(goal.getProcessArea()!=null && goal.getModel()!=null) {
            throw new IllegalArgumentException("goal does have specified both model and process area");
        }

        goal = goalDao.create(goal);
        if(goal.isGeneric()) {
            Model model = goal.getModel();
            if(model.getGenericGoals()==null) {
                model.setGenericGoals(new HashSet<Goal>());
            }
            if(!model.getGenericGoals().contains(goal)) {
                model.getGenericGoals().add(goal);
            }
            return modelDao.update(model);
        } else {
            ProcessArea process = goal.getProcessArea();
            if(process.getGoals()==null) {
                process.setGoals(new HashSet<Goal>());
            }
            if(!process.getGoals().contains(goal)) {
                process.getGoals().add(goal);
                process = processAreaDao.update(process);
            }
            return process.getModel();
        }
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
        return (goal.getProcessArea()!=null ? goal.getProcessArea().getModel() : goal.getModel());
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
        return (practice.getGoal().getProcessArea()!=null ? practice.getGoal().getProcessArea().getModel() :
            practice.getGoal().getModel());
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
    public Model removeProcess(long id) {
        ProcessArea process = processAreaDao.read(id);
        Model model = process.getModel();
        model.getProcessAreas().remove(process);
        process.setModel(null);
        processAreaDao.delete(id);
        return modelDao.update(model);
    }

    @Override
    public Model removeGoal(long id) {
        Goal goal = goalDao.read(id);
        goalDao.delete(id);
        if(goal.isGeneric()) {
            goal.getModel().getGenericGoals().remove(goal);
            return goal.getModel();
        }else{
            ProcessArea process = goal.getProcessArea();
            process.getGoals().remove(goal);
            goal.setProcessArea(null);
            return processAreaDao.update(process).getModel();
        }
    }

    @Override
    public Model removePractice(long id) {
        Practice practice = practiceDao.read(id);
        Goal goal = practice.getGoal();
        goal.getPractices().remove(practice);
        practice.setGoal(null);
        practiceDao.delete(id);
        goal = goalDao.update(goal);
        return goal.getProcessArea()!=null ? goal.getProcessArea().getModel() : goal.getModel();
    }

    @Override
    public Model removeArtifact(long id) {
        Artifact artifact = artifactDao.read(id);
        Practice practice = artifact.getPractice();
        practice.getArtifacts().remove(artifact);
        artifact.setPractice(null);
        artifactDao.delete(id);
        practice = practiceDao.update(practice);
        return practice.getGoal().getProcessArea()!=null ? practice.getGoal().getProcessArea().getModel() :
            practice.getGoal().getModel();
    }

    @Override
    public Model saveProcess(ProcessArea process) {
        ProcessArea pa = processAreaDao.read(process.getId());
        pa.setAcronym(process.getAcronym());
        pa.setMaturityLevel(process.getMaturityLevel());
        pa.setName(process.getName());
        pa.setPurpose(process.getPurpose());
        pa.setSummary(process.getSummary());
        pa.setProcessGroup(process.getProcessGroup());
        return processAreaDao.update(pa).getModel();
    }

    @Override
    public Model saveGoal(Goal goal) {
        Goal g = goalDao.read(goal.getId());
        g.setAcronym(goal.getAcronym());
        g.setName(goal.getName());
        g.setSummary(goal.getSummary());
        g = goalDao.update(g);
        return g.getProcessArea()!=null ? g.getProcessArea().getModel() : g.getModel();
    }

    @Override
    public Model savePractice(Practice practice) {
        Practice p = practiceDao.read(practice.getId());
        p.setAcronym(practice.getAcronym());
        p.setName(practice.getName());
        p.setPracticeCapability(practice.getPracticeCapability());
        p.setPurpose(practice.getPurpose());
        p.setSummary(practice.getSummary());
        p = practiceDao.update(p);
        return p.getGoal().getProcessArea()!=null ? p.getGoal().getProcessArea().getModel() :
            p.getGoal().getModel();
    }

    @Override
    public Model saveArtifact(Artifact artifact) {
        Artifact a = artifactDao.read(artifact.getId());
        a.setAcronym(artifact.getAcronym());
        a.setDirect(artifact.isDirect());
        a.setName(artifact.getName());
        a = artifactDao.update(a);
        return a.getPractice().getGoal().getProcessArea()!=null ? a.getPractice().getGoal().getProcessArea().getModel()
                : a.getPractice().getGoal().getModel();
    }

    private static final Log _log = LogFactory.getLog(ModelServiceImpl.class);

}
