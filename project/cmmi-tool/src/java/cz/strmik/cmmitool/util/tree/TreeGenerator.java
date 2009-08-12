/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.util.tree;

import cz.strmik.cmmitool.entity.AcronymEntity;
import cz.strmik.cmmitool.entity.Artifact;
import cz.strmik.cmmitool.entity.Goal;
import cz.strmik.cmmitool.entity.Method;
import cz.strmik.cmmitool.entity.Model;
import cz.strmik.cmmitool.entity.Practice;
import cz.strmik.cmmitool.entity.ProcessArea;
import cz.strmik.cmmitool.entity.RatingScale;
import cz.strmik.cmmitool.web.lang.LangProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class TreeGenerator {

    private final static String SEPARATOR = "-";

    /**
     * Creates tree of model.
     *
     * @param entity
     * @param editCommand
     * @param removeCommand
     * @return
     */
    public static TreeNode modelToTree(AcronymEntity entity, String editCommand, String removeCommand) {
        String link = null;
        String removeLink = null;
        if(!(entity instanceof Model)) {
            String element = SEPARATOR + entity.getClass().getSimpleName().toLowerCase() + SEPARATOR + entity.getId() + ".do";
            link = editCommand + element;
            removeLink = removeCommand  + element;
        }
        TreeNode node = new TreeNode("(" + entity.getAcronym() + ") " +entity.getName(), link, removeLink);
        if (entity instanceof Model) {
            Model model = (Model) entity;
            if (model.getProcessAreas() != null) {
                for (ProcessArea area : model.getProcessAreas()) {
                    node.getSubNodes().add(modelToTree(area, editCommand, removeCommand));
                }
            }
        } else if (entity instanceof ProcessArea) {
            ProcessArea area = (ProcessArea) entity;
            if (area.getGoals() != null) {
                for (Goal goal : area.getGoals()) {
                    node.getSubNodes().add(modelToTree(goal, editCommand, removeCommand));
                }
            }
        } else if (entity instanceof Goal) {
            Goal goal = (Goal) entity;
            if (goal.getPractices() != null) {
                for (Practice practice : goal.getPractices()) {
                    node.getSubNodes().add(modelToTree(practice, editCommand, removeCommand));
                }
            }
        } else if (entity instanceof Practice) {
            Practice practice = (Practice) entity;
            if(practice.getArtifacts() != null) {
                for(Artifact artifact : practice.getArtifacts()) {
                    node.getSubNodes().add(modelToTree(artifact, editCommand, removeCommand));
                }
            }
        }
        return node;
    }

    /**
     * Coverts method rating to tree.
     *
     * @param method
     * @param editCommand
     * @param removeCommand
     * @return
     */
    public static TreeNode methodToTree(Method method, String editCommand, String editCommand2, String removeCommand) {
       TreeNode root = new TreeNode(method.getName(), null, null);
       if(method.getOrgMaturityLevel()!=null && !method.getOrgMaturityLevel().isEmpty()) {
           addScales(root, "org-maturity-level", method.getOrgMaturityLevel(), editCommand, editCommand2, removeCommand);
       }
       if(method.getProcessAreaCapLevel()!=null && !method.getProcessAreaCapLevel().isEmpty()) {
           addScales(root, "process-area-cap-level", method.getProcessAreaCapLevel(), editCommand, editCommand2, removeCommand);
       }
       if(method.getProcessAreaSatisfaction()!=null && !method.getProcessAreaSatisfaction().isEmpty()) {
           addScales(root, "process-area-satisfaction", method.getProcessAreaSatisfaction(), editCommand, editCommand2, removeCommand);
       }
       if(method.getGoalSatisfaction()!=null && !method.getGoalSatisfaction().isEmpty()) {
           addScales(root, "goal-satisfaction", method.getGoalSatisfaction(), editCommand, editCommand2, removeCommand);
       }
       if(method.getPracticeImplementation()!=null && !method.getPracticeImplementation().isEmpty()) {
           addScales(root, "char-practice-impl", method.getPracticeImplementation(), editCommand, editCommand2, removeCommand);
       }
       return root;
    }

    private static void addScales(TreeNode root, String ratingName, Set<RatingScale> scales, String editCommand,
             String editCommand2, String removeCommand) {
        TreeNode rating = new TreeNode(LangProvider.getString(ratingName),editCommand2 + SEPARATOR + ratingName + ".do",null);
        root.getSubNodes().add(rating);
        List<RatingScale> sortedScales = new ArrayList<RatingScale>(scales);
        Collections.sort(sortedScales);
        for(RatingScale rs : sortedScales) {
            TreeNode node = new TreeNode(rs.getName() +" ["+rs.getScore()+"]", editCommand + SEPARATOR + rs.getId() + ".do", removeCommand +
                    SEPARATOR + rs.getId() + ".do");
            rating.getSubNodes().add(node);
        }
    }
    
}
