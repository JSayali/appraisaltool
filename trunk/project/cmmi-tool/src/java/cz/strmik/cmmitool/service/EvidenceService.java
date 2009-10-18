/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.entity.project.Evidence;
import cz.strmik.cmmitool.entity.model.Practice;
import cz.strmik.cmmitool.entity.project.ProcessInstantiation;
import cz.strmik.cmmitool.entity.project.Project;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public interface EvidenceService {

    List<Evidence> getAllEvidenceOfProject(Project project);

    /**
     * Links evidence to specified process instantiations of practices and unlink
     * from others.
     *
     * @param evidence Evidence to link
     * @param links Map to process linking.
     */
    void linkEvidenceToPractices(Evidence evidence, Map<Practice, Set<ProcessInstantiation>> links);

}
