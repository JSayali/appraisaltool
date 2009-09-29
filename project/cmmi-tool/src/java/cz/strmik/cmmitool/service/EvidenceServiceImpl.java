/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.Evidence;
import cz.strmik.cmmitool.entity.EvidenceMapping;
import cz.strmik.cmmitool.entity.Practice;
import cz.strmik.cmmitool.entity.Project;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class EvidenceServiceImpl implements EvidenceService {

    @Autowired
    private GenericDao<Evidence, Long> evidenceDao;
    @Autowired
    private GenericDao<Project, String> projectDao;
    @Autowired
    private GenericDao<EvidenceMapping, Long> evidenceMappingDao;

    @Override
    public List<Evidence> getAllEvidenceOfProject(Project project) {
        return evidenceDao.findByNamedQuery("findByProject", "project", project);
    }

    @Override
    public void linkEvidenceToPractices(Evidence evidence, Set<Practice> practices) {
        evidence = evidenceDao.read(evidence.getId());
        Iterator<EvidenceMapping> it = evidence.getMappings().iterator();
        while(it.hasNext()) {
            EvidenceMapping mapping = it.next();
            // delete removed
            if(!practices.contains(mapping.getPractice())) {
                evidenceMappingDao.delete(mapping.getId());
                it.remove();
            }
            // ignore existing
            if(practices.contains(mapping.getPractice())) {
                practices.remove(mapping.getPractice());
            }
        }
        // add new mapping
        Project project = evidence.getProject();
        for(Practice practice : practices) {
            EvidenceMapping em = new EvidenceMapping();
            em.setProject(project);
            project.getEvidenceMappings().add(em);
            em.setEvidence(evidence);
            evidence.getMappings().add(em);
            em.setPractice(practice);
            evidenceMappingDao.create(em);
            project.getEvidenceMappings().add(em);
        }
        evidenceDao.update(evidence);
        projectDao.update(project);
    }

}
