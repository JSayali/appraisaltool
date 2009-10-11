/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.util.tree;

import java.awt.Color;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class TreeNodeColor extends TreeNode {

    private Color color;

    public TreeNodeColor(String label, String link, Color color) {
        super(label, link, null);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
