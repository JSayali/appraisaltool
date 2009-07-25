/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.Model;
import cz.strmik.cmmitool.entity.ProcessArea;
import cz.strmik.cmmitool.entity.ProcessGroup;
import java.util.ArrayList;
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


    @Override
    public Model addGroup(ProcessGroup processGroup) {
        if(processGroup.getModel()==null) {
            throw new IllegalArgumentException("processGroup does not have specified model");
        }
        processGroup = processGroupDao.create(processGroup);
        Model model = processGroup.getModel();
        if(model.getProcessGroups()==null) {
            model.setProcessGroups(new ArrayList<ProcessGroup>());
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
            model.setProcessAreas(new ArrayList<ProcessArea>());
        }
        if(!model.getProcessAreas().contains(process)) {
            model.getProcessAreas().add(process);
            modelDao.update(model);
        }
        return model;
    }

    @Override
    public void removeGroup(long id) {
        ProcessGroup group = processGroupDao.read(id);
        Model model = group.getModel();
        model.getProcessGroups().remove(group);
        group.setModel(null);
        modelDao.update(model);
        processGroupDao.delete(id);
    }

}
