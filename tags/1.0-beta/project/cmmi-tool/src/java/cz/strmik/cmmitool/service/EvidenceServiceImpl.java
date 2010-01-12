/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.project.Evidence;
import cz.strmik.cmmitool.entity.project.EvidenceMapping;
import cz.strmik.cmmitool.entity.model.Practice;
import cz.strmik.cmmitool.entity.project.ProcessInstantiation;
import cz.strmik.cmmitool.entity.project.Project;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    public void linkEvidenceToPractices(Evidence evidence, Map<Practice, Set<ProcessInstantiation>> links) {
        evidence = evidenceDao.read(evidence.getId());
        Project project = evidence.getProject();
        Iterator<EvidenceMapping> it = evidence.getMappings().iterator();
        while(it.hasNext()) {
            EvidenceMapping mapping = it.next();
            // remove not existing mapping from DB
            if(!links.containsKey(mapping.getPractice()) ||
                    (links.containsKey(mapping.getPractice()) &&
                    !links.get(mapping.getPractice()).contains(mapping.getProcessInstantiation()))) {
                project.getEvidenceMappings().remove(mapping);
                evidenceMappingDao.delete(mapping.getId());
                it.remove();
            }
            // do not re-add existing mappings
            if(links.containsKey(mapping.getPractice()) &&
                    links.get(mapping.getPractice()).contains(mapping.getProcessInstantiation())) {
                links.get(mapping.getPractice()).remove(mapping.getProcessInstantiation());
            }
        }
        // add new mapping
        for(Practice practice : links.keySet()) {
            for(ProcessInstantiation pi : links.get(practice)) {
                EvidenceMapping em = new EvidenceMapping();
                em.setProject(project);
                project.getEvidenceMappings().add(em);
                em.setEvidence(evidence);
                evidence.getMappings().add(em);
                em.setPractice(practice);
                em.setProcessInstantiation(pi);
                evidenceMappingDao.create(em);
                project.getEvidenceMappings().add(em);
            }
        }
        evidenceDao.update(evidence);
        projectDao.update(project);
    }

}
