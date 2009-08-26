/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.util.tree;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class TreeNode {

    public static final String FOLDER = "folder";
    public static final String FILE = "file";

    private String label;
    private String link;
    private String removeLink;

    private List<TreeNode> subNodes;

    public TreeNode(String label, String link, String removeLink) {
        this.label = label;
        this.link = link;
        this.removeLink = removeLink;
        subNodes = new ArrayList<TreeNode>();
    }

    public String getLabel() {
        return label;
    }

    public String getLink() {
        return link;
    }

    public String getRemoveLink() {
        return removeLink;
    }

    public void setRemoveLink(String removeLink) {
        this.removeLink = removeLink;
    }

    public List<TreeNode> getSubNodes() {
        return subNodes;
    }

    public String getType() {
        return subNodes.isEmpty() ? FILE : FOLDER;
    }

}
