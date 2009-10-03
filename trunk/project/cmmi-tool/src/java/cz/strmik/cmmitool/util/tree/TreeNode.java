/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.util.tree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private String checkbox;
    private boolean checked;
    private Map<String, Map<String, String>> lists;
    private Map<String, String> listLabels;
    private Map<String, String> listValues;

    private List<TreeNode> subNodes;

    public TreeNode(String label) {
        subNodes = new LinkedList<TreeNode>();
        this.label = label;
    }

    /**
     * creates standard text node
     *
     * @param label
     * @param link If link specified, label of node contain this links.
     * @param removeLink if not null, then remove link "(X)" is added after label.
     */
    public TreeNode(String label, String link, String removeLink) {
        this(label);
        this.link = link;
        this.removeLink = removeLink;
    }

    /**
     * Creates node with chcekbox.
     *
     * @param label
     * @param checkbox
     * @param checked
     */
    public TreeNode(String label, String checkbox, boolean checked) {
        this(label);
        this.checkbox = checkbox;
        this.checked = checked;
    }

    /**
     * Adds combobox to node with name name and label label. values are taken form map,
     * with dafualt value specified.
     *
     * @param name
     * @param label
     * @param values
     * @param value
     */
    public void addList(String name, String label, Map<String, String> values, String value) {
        if(lists==null) {
            lists = new HashMap<String, Map<String, String>>();
            listLabels = new HashMap<String, String>();
            listValues = new HashMap<String, String>();
        }
        lists.put(name, values);
        listLabels.put(name, label);
        listValues.put(name, value);
    }

    public boolean isHaveLists() {
        return lists!=null;
    }

    public Map<String, Map<String, String>> getLists() {
        return lists;
    }

    public Map<String, String> getListLabels() {
        return listLabels;
    }

    public Map<String, String> getListValues() {
        return listValues;
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

    public String getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(String checkbox) {
        this.checkbox = checkbox;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getType() {
        return subNodes.isEmpty() ? FILE : FOLDER;
    }

}
