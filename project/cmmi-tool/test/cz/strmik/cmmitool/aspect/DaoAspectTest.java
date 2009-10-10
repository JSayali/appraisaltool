/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */

package cz.strmik.cmmitool.aspect;

import cz.strmik.cmmitool.entity.Method;
import cz.strmik.cmmitool.entity.RatingScale;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class DaoAspectTest {

    public DaoAspectTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setupMethodBools method, of class DaoAspect.
     */
    @Test
    public void testSetupMethodBools() {
        System.out.println("setupMethodBools");
        Method method = null;
        DaoAspect instance = new DaoAspect();
        instance.setupMethodBools(method);
        method = new Method();
        instance.setupMethodBools(method);
        method.setProcessAreaCapLevel(new HashSet<RatingScale>());
        instance.setupMethodBools(method);
        assertFalse(method.isRateProcessAreaCapLevel());
        method.getProcessAreaCapLevel().add(new RatingScale());
        instance.setupMethodBools(method);
        assertTrue(method.isRateProcessAreaCapLevel());
        List<Method> methods = new ArrayList<Method>();
        method.getProcessAreaCapLevel().clear();
        methods.add(method);
        instance.setupMethodBools(methods);
        assertFalse(method.isRateProcessAreaCapLevel());
        assertFalse(method.isCharPracticeImplementation());
        method.setPracticeImplementation(new HashSet<RatingScale>());
        method.getPracticeImplementation().add(new RatingScale());
        instance.setupMethodBools(methods);
        assertTrue(method.isCharPracticeImplementation());
    }

}