/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.util.tree;

import cz.strmik.cmmitool.entity.AcronymEntity;
import cz.strmik.cmmitool.entity.Goal;
import cz.strmik.cmmitool.entity.Model;
import cz.strmik.cmmitool.entity.Practice;
import cz.strmik.cmmitool.entity.ProcessArea;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class TreeGenerator {

    public static TreeNode modelToTree(AcronymEntity entity) {
        TreeNode node = new TreeNode(entity.getName(), "edit-" + entity.getClass().getSimpleName().toLowerCase() + "-" +
                entity.getId() + ".do");
        if (entity instanceof Model) {
            Model model = (Model) entity;
            if (model.getProcessAreas() != null) {
                for (ProcessArea area : model.getProcessAreas()) {
                    node.getSubNodes().add(modelToTree(area));
                }
            }
        } else if (entity instanceof ProcessArea) {
            ProcessArea area = (ProcessArea) entity;
            if (area.getGoals() != null) {
                for (Goal goal : area.getGoals()) {
                    node.getSubNodes().add(modelToTree(goal));
                }
            }
        } else if (entity instanceof Goal) {
            Goal goal = (Goal) entity;
            if (goal.getPractices() != null) {
                for (Practice practice : goal.getPractices()) {
                    node.getSubNodes().add(modelToTree(practice));
                }
            }
        }
        return node;
    }
}
