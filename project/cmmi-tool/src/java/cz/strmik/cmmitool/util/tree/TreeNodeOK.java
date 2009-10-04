/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.util.tree;

/**
 * Extending treenode, and can show OK or NOK type.
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class TreeNodeOK extends TreeNode {

    public static final String OK = "ok";
    public static final String NOK = "nok";

    private Boolean isOK;

    public TreeNodeOK(String label, Boolean isOK) {
        super(label);
        this.isOK = isOK;
    }

    public Boolean isIsOK() {
        return isOK;
    }

    public void setIsOK(Boolean isOK) {
        this.isOK = isOK;
    }

    /**
     * Returns "ok" or "nok" depending on isOK.
     * 
     * @return
     */
    @Override
    public String getType() {
        return isOK ? OK : NOK;
    }

}
