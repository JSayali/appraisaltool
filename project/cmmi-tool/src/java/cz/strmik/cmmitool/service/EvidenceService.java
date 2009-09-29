/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.entity.Evidence;
import cz.strmik.cmmitool.entity.Practice;
import cz.strmik.cmmitool.entity.Project;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public interface EvidenceService {

    List<Evidence> getAllEvidenceOfProject(Project project);

    void linkEvidenceToPractices(Evidence evidence, Set<Practice> practices);

}
