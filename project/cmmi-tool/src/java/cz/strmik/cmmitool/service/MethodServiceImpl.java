/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.Method;
import cz.strmik.cmmitool.entity.RatingScale;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class MethodServiceImpl implements MethodService {

    @Autowired
    private GenericDao<RatingScale, Long> ratingScaleDao;

    @Autowired
    private GenericDao<Method, Long> methodDao;

    @Override
    public Method removeScale(Method method, RatingScale scale) {
        method.getGoalSatisfaction().remove(scale);
        method.getOrgMaturityLevel().remove(scale);
        method.getPracticeImplementation().remove(scale);
        method.getProcessAreaCapLevel().remove(scale);
        method.getProcessAreaSatisfaction().remove(scale);
        ratingScaleDao.delete(scale.getId());
        return methodDao.update(method);
    }

    @Override
    public Method removeUnusedScales(Method method) {
        if(!method.isRateGoalSatisfaction()) {
            if(method.getGoalSatisfaction()!=null) {
                deleteScales(method.getGoalSatisfaction());
                method.setGoalSatisfaction(null);
            }
        }
        if(!method.isRateOrgMaturityLevel()) {
            if(method.getOrgMaturityLevel()!=null) {
                deleteScales(method.getOrgMaturityLevel());
                method.setOrgMaturityLevel(null);
            }
        }
        if(!method.isCharPracticeImplementation()) {
            if(method.getPracticeImplementation()!=null) {
                deleteScales(method.getPracticeImplementation());
                method.setPracticeImplementation(null);
            }
        }
        if(!method.isRateProcessAreaCapLevel()) {
            if(method.getProcessAreaCapLevel()!=null) {
                deleteScales(method.getProcessAreaCapLevel());
                method.setProcessAreaCapLevel(null);
            }
        }
        if(!method.isRateProcessAreaSatisfaction()) {
            if(method.getProcessAreaSatisfaction()!=null) {
                deleteScales(method.getProcessAreaSatisfaction());
                method.setProcessAreaSatisfaction(null);
            }
        }
        return methodDao.update(method);
    }

    private void deleteScales(Set<RatingScale> scales) {
        for(RatingScale scale :  scales) {
            ratingScaleDao.delete(scale.getId());
        }
    }

}
