/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.util.tree;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.AcronymEntity;
import cz.strmik.cmmitool.entity.Artifact;
import cz.strmik.cmmitool.entity.Evidence;
import cz.strmik.cmmitool.entity.EvidenceMapping;
import cz.strmik.cmmitool.entity.EvidenceRating;
import cz.strmik.cmmitool.entity.Goal;
import cz.strmik.cmmitool.entity.Method;
import cz.strmik.cmmitool.entity.Model;
import cz.strmik.cmmitool.entity.Practice;
import cz.strmik.cmmitool.entity.ProcessArea;
import cz.strmik.cmmitool.entity.Project;
import cz.strmik.cmmitool.entity.RatingScale;
import cz.strmik.cmmitool.enums.EvidenceCharacteristic;
import cz.strmik.cmmitool.enums.IndicatorType;
import cz.strmik.cmmitool.enums.PracticeEvidenceAdequacy;
import cz.strmik.cmmitool.util.Constants;
import cz.strmik.cmmitool.web.lang.LangProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO: Needs total refactoring. It was really small static helper class which have deloped into huge class with
 * awful methods and there is no time now to refator tree building subsystem. TreeNode objects are used in
 * strmik:tree tag.
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
    public static TreeNode modelToTree(AcronymEntity entity, boolean generic, String editCommand, String removeCommand) {
        String link = null;
        String removeLink = null;
        if (!(entity instanceof Model)) {
            String element = SEPARATOR + entity.getClass().getSimpleName().toLowerCase() + SEPARATOR + entity.getId() + ".do";
            link = editCommand + element;
            removeLink = removeCommand + element;
        }
        TreeNode node = new TreeNode("(" + entity.getAcronym() + ") " + entity.getName(), link, removeLink);
        if (entity instanceof Model) {
            Model model = (Model) entity;
            if (generic) {
                if (model.getGenericGoals() != null) {
                    for (Goal goal : model.getGenericGoals()) {
                        node.getSubNodes().add(modelToTree(goal, generic, editCommand, removeCommand));
                    }
                }
            } else {
                if (model.getProcessAreas() != null) {
                    for (ProcessArea area : model.getProcessAreas()) {
                        node.getSubNodes().add(modelToTree(area, generic, editCommand, removeCommand));
                    }
                }
            }
        } else if (entity instanceof ProcessArea) {
            ProcessArea area = (ProcessArea) entity;
            if (area.getGoals() != null) {
                for (Goal goal : area.getGoals()) {
                    node.getSubNodes().add(modelToTree(goal, generic, editCommand, removeCommand));
                }
            }
        } else if (entity instanceof Goal) {
            Goal goal = (Goal) entity;
            if (goal.getPractices() != null) {
                for (Practice practice : goal.getPractices()) {
                    node.getSubNodes().add(modelToTree(practice, generic, editCommand, removeCommand));
                }
            }
        } else if (entity instanceof Practice) {
            Practice practice = (Practice) entity;
            if (practice.getArtifacts() != null) {
                for (Artifact artifact : practice.getArtifacts()) {
                    node.getSubNodes().add(modelToTree(artifact, generic, editCommand, removeCommand));
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
        if (method.getOrgMaturityLevel() != null && !method.getOrgMaturityLevel().isEmpty()) {
            addScales(root, Constants.Ratings.ORG_MATURITY_LEVEL.toString(), method.getOrgMaturityLevel(), editCommand, editCommand2, removeCommand);
        }
        if (method.getProcessAreaCapLevel() != null && !method.getProcessAreaCapLevel().isEmpty()) {
            addScales(root, Constants.Ratings.PROCESS_AREA_CAP_LEVEL.toString(), method.getProcessAreaCapLevel(), editCommand, editCommand2, removeCommand);
        }
        if (method.getProcessAreaSatisfaction() != null && !method.getProcessAreaSatisfaction().isEmpty()) {
            addScales(root, Constants.Ratings.PROCESS_AREA_SATISFACTION.toString(), method.getProcessAreaSatisfaction(), editCommand, editCommand2, removeCommand);
        }
        if (method.getGoalSatisfaction() != null && !method.getGoalSatisfaction().isEmpty()) {
            addScales(root, Constants.Ratings.GOAL_SATISFACTION.toString(), method.getGoalSatisfaction(), editCommand, editCommand2, removeCommand);
        }
        if (method.getPracticeImplementation() != null && !method.getPracticeImplementation().isEmpty()) {
            addScales(root, Constants.Ratings.CHAR_PRACTICE_IMPL.toString(), method.getPracticeImplementation(), editCommand, editCommand2, removeCommand);
        }
        return root;
    }

    private static void addScales(TreeNode root, String ratingName, Set<RatingScale> scales, String editCommand,
            String editCommand2, String removeCommand) {
        TreeNode rating = new TreeNode(LangProvider.getString(ratingName), editCommand2 + SEPARATOR + ratingName + ".do", null);
        root.getSubNodes().add(rating);
        List<RatingScale> sortedScales = new ArrayList<RatingScale>(scales);
        Collections.sort(sortedScales);
        for (RatingScale rs : sortedScales) {
            TreeNode node = new TreeNode(rs.getName() + " [" + rs.getScore() + "]", editCommand + SEPARATOR + rs.getId() + ".do", removeCommand +
                    SEPARATOR + rs.getId() + ".do");
            rating.getSubNodes().add(node);
        }
    }

    /**
     * Build tree with project model and puts checkboxes to practices, when evidence is not null. Checkboxes are
     * selected, when evidence is attached to practice.
     *
     * When evidence is null, then no checkboxes are rendered, only paractices with all atached evidences and
     * comboboxes for rating evidence and practices.
     *
     * @param project Project tree to be rendered
     * @param evidence When attaching evidence to prectices, must be not null. See description.
     * @param evidenceDao DAO used for looking-up evidences attached to practices. Read only.
     * @return Root node of tree.
     */
    public static TreeNode evidenceTree(Project project, Evidence evidence, GenericDao<Evidence, Long> evidenceDao) {
        boolean linking = evidence != null;
        Map<String, String> evAdequacy = new HashMap<String, String>();
        Map<String, String> evChar = new HashMap<String, String>();
        Map<String, String> indType = new HashMap<String, String>();
        Map<String, String> evInstanceChar = new HashMap<String, String>();

        if (!linking) {
            for (PracticeEvidenceAdequacy pea : PracticeEvidenceAdequacy.values()) {
                evAdequacy.put(pea.toString(), LangProvider.getString("PracticeEvidenceAdequacy."+pea));
            }
            for (EvidenceCharacteristic ec : EvidenceCharacteristic.values()) {
                evInstanceChar.put(ec.toString(), LangProvider.getString("EvidenceCharacteristic."+ec));
            }
            for (IndicatorType it : IndicatorType.values()) {
                indType.put(it.toString(), LangProvider.getString("IndicatorType."+it));
            }
            project.getMethod().setupBools();
            if (project.getMethod().isCharPracticeImplementation()) {
                for (RatingScale rs : project.getMethod().getPracticeImplementation()) {
                    evChar.put(rs.getId().toString(), rs.getName());
                }
            }

        }

        TreeNode root = new TreeNode(project.getModel().getName(), null, null);
        for (ProcessArea area : project.getModel().getProcessAreas()) {
            TreeNode areaNode = new TreeNode(area.getAcronym() + " " + area.getName(), null, null);
            root.getSubNodes().add(areaNode);
            for (Goal goal : area.getGoals()) {
                TreeNode goalNode = new TreeNode(goal.getAcronym() + " " + goal.getName(), null, null);
                areaNode.getSubNodes().add(goalNode);
                for (Practice practice : goal.getPractices()) {
                    List<Evidence> attachedEvidence = evidenceDao.findByNamedQuery("findByProjectPractice", "project", project,
                            "practice", practice);

                    TreeNode practiceNode = null;
                    if(linking) {
                        practiceNode = new TreeNode(area.getAcronym() + " " + goal.getAcronym() + " " + practice.getAcronym() + " " +
                            practice.getName(), "practice-" + practice.getId(), attachedEvidence.contains(evidence));
                    } else {
                        practiceNode = new TreeNode(area.getAcronym() + " " + goal.getAcronym() + " " + practice.getAcronym() + " ",
                            null, null);
                    }
                    goalNode.getSubNodes().add(practiceNode);

                    if (!linking) {
                        String pea = null;
                        for (EvidenceRating er : project.getEvidenceRating()) {
                            if (er.getPractice().equals(practice)) {
                                pea = er.getEvidenceAdequacy().toString();
                                break;
                            }
                        }
                        practiceNode.addList("practice-adequacy-" + practice.getId(), LangProvider.getString("adequacy"), evAdequacy,
                                pea);

                        if (project.getMethod().isCharPracticeImplementation()) {
                            String rs = null;
                            for (EvidenceRating er : project.getEvidenceRating()) {
                                if (er.getPractice().equals(practice)) {
                                    rs = er.getCharacterizePracticeImplementation().getId().toString();
                                    break;
                                }
                            }
                            practiceNode.addList("practice-char-" + practice.getId(), LangProvider.getString("characterization"),
                                    evChar, rs);
                        }
                        for (Evidence ev : attachedEvidence) {
                            TreeNode evidenceNode = new TreeNode(ev.getName(), null, null);
                            practiceNode.getSubNodes().add(evidenceNode);

                            if (!linking) {
                                String evIndTypeVal = "";
                                String evInstanceCharVal = "";
                                if (ev.getMappings() != null) {
                                    for (EvidenceMapping em : ev.getMappings()) {
                                        if (em.getPractice().equals(practice)) {
                                            evIndTypeVal = em.getIndicatorType()!=null ? em.getIndicatorType().toString() : "";
                                            evInstanceCharVal = em.getCharacteristic()!=null ? em.getCharacteristic().toString() : "";
                                        }
                                    }
                                }
                                evidenceNode.addList("evidence-char-" + ev.getId(), LangProvider.getString("characterization"), evInstanceChar, evInstanceCharVal);
                                evidenceNode.addList("evidence-ind-" + ev.getId(), LangProvider.getString("indicator-type"), indType, evIndTypeVal);
                            }

                        }
                    }
                }
            }
        }
        return root;
    }
}