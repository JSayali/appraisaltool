/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.project.Evidence;
import cz.strmik.cmmitool.entity.project.EvidenceMapping;
import cz.strmik.cmmitool.entity.project.EvidenceRating;
import cz.strmik.cmmitool.entity.model.Practice;
import cz.strmik.cmmitool.entity.project.Project;
import cz.strmik.cmmitool.entity.method.RatingScale;
import cz.strmik.cmmitool.entity.project.ProcessInstantiation;
import cz.strmik.cmmitool.enums.EvidenceCharacteristic;
import cz.strmik.cmmitool.enums.EvidenceStatus;
import cz.strmik.cmmitool.enums.EvidenceType;
import cz.strmik.cmmitool.enums.IndicatorType;
import cz.strmik.cmmitool.enums.PracticeEvidenceAdequacy;
import cz.strmik.cmmitool.service.EvidenceService;
import cz.strmik.cmmitool.util.tree.TreeGenerator;
import cz.strmik.cmmitool.util.validator.EvidenceValidator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/appraisal/evidence")
@SessionAttributes({Attribute.PROJECT, Attribute.EVIDENCE})
public class EvidenceController extends AbstractController {

    private static final String DASHBOARD = "/appraisal/evidence/dashboard";
    private static final String FORM = "/appraisal/evidence/form";
    private static final String LINK = "/appraisal/evidence/link";
    private static final String CHARACTERIZE = "/appraisal/evidence/characterize";
    @Autowired
    private EvidenceService evidenceService;
    @Autowired
    private GenericDao<Evidence, Long> evidenceDao;
    @Autowired
    private GenericDao<ProcessInstantiation, Long> processInstantiationDao;
    @Autowired
    private GenericDao<Project, String> projectDao;
    @Autowired
    private GenericDao<Practice, Long> practiceDao;
    @Autowired
    private GenericDao<RatingScale, Long> ratingScaleDao;
    @Autowired
    private GenericDao<EvidenceRating, Long> evidenceRatingDao;

    @ModelAttribute("types")
    public Collection<EvidenceType> getTypes() {
        List<EvidenceType> levels = new ArrayList<EvidenceType>(EvidenceType.values().length);
        for (EvidenceType type : EvidenceType.values()) {
            levels.add(type);
        }
        return levels;
    }

    @ModelAttribute("statuses")
    public Collection<EvidenceStatus> getStatuses() {
        List<EvidenceStatus> levels = new ArrayList<EvidenceStatus>(EvidenceStatus.values().length);
        for (EvidenceStatus type : EvidenceStatus.values()) {
            levels.add(type);
        }
        return levels;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String showDashboard(ModelMap model, HttpSession session) {
        Project project = (Project) session.getAttribute(Attribute.PROJECT);
        if (project != null) {
            project = projectDao.read(project.getId());
            model.addAttribute(Attribute.PROJECT, project);
            model.addAttribute("evidence", evidenceService.getAllEvidenceOfProject(project));
            model.addAttribute("auditor", userIsAuditor(project));
        } else {
            log.warn("Project not found in session!");
        }
        return DASHBOARD;
    }

    @RequestMapping(value = "/add.do")
    public String addEvidence(ModelMap model) {
        Evidence evidence = new Evidence();
        evidence.setNew(true);
        evidence.setStatus(EvidenceStatus.ENTERED);
        model.addAttribute(Attribute.EVIDENCE, evidence);
        return FORM;
    }

    @RequestMapping(value = "/edit-{id}.do")
    public String editEvidence(@PathVariable("id") Long id, ModelMap model) {
        Evidence evidence = evidenceDao.read(id);
        evidence.setNew(false);
        model.addAttribute(Attribute.EVIDENCE, evidence);
        return FORM;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save-evidence.do")
    public String saveEvidence(@ModelAttribute(Attribute.EVIDENCE) Evidence evidence, BindingResult result, ModelMap modelMap,
            HttpSession session) {
        new EvidenceValidator().validate(evidence, result);
        if (result.hasErrors()) {
            return FORM;
        }
        evidence.setModifiedBy(getLoggedUser());
        Project project = projectDao.read(((Project) session.getAttribute(Attribute.PROJECT)).getId());
        if (evidence.isNew()) {
            evidence.setProject(project);
            evidence = evidenceDao.create(evidence);
        } else {
            evidence = evidenceDao.update(evidence);
        }
        modelMap.addAttribute(Attribute.EVIDENCE, evidence);
        modelMap.addAttribute("practiceTree", TreeGenerator.evidenceTree(project, evidence, evidenceDao));
        return LINK;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/link.do")
    public String linkEvidence(@ModelAttribute(Attribute.EVIDENCE) Evidence evidence, ModelMap modelMap,
            HttpSession session, WebRequest request) {
        Iterator<String> it = request.getParameterNames();
        Map<Practice, Set<ProcessInstantiation>> pis = new HashMap<Practice, Set<ProcessInstantiation>>();
        while (it.hasNext()) {
            String param = it.next();
            if (!param.matches("^practice-\\d+?,inst-\\d+$")) {
                log.debug("skipping parameter " + param);
                continue;
            }
            String[] params = param.split(",");
            Long id = Long.parseLong(params[0].substring(9));
            Practice practice = practiceDao.read(id);
            if(!pis.containsKey(practice)) {
                pis.put(practice, new HashSet<ProcessInstantiation>());
            }
            id = Long.parseLong(params[1].substring(5));
            ProcessInstantiation pi = processInstantiationDao.read(id);
            pis.get(practice).add(pi);
        }
        evidenceService.linkEvidenceToPractices(evidence, pis);
        return "redirect:/appraisal/evidence/";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/characterize.do")
    public String characterizeEvidence(@ModelAttribute(Attribute.PROJECT) Project project,
            ModelMap modelMap, HttpSession session) {
        project = projectDao.read(project.getId());
        checkIsAudior(project);
        modelMap.addAttribute("practiceTree", TreeGenerator.evidenceTree(project, null, evidenceDao));
        return CHARACTERIZE;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/characterize.do")
    public String characterizeEvidenceSave(@ModelAttribute(Attribute.PROJECT) Project project,
            ModelMap modelMap, HttpSession session, WebRequest request) {
        project = projectDao.read(project.getId());
        checkIsAudior(project);
        Iterator<String> it = request.getParameterNames();
        while (it.hasNext()) {
            String param = it.next();
            String value = request.getParameter(param);
            if (log.isDebugEnabled()) {
                log.debug("Read parameter: " + param + " = " + value);
            }
            if (param.matches("^practice-char-\\d+?-\\d+$")) {
                setPracticeChar(param, value, project);
            } else if (param.matches("^practice-adequacy-\\d+?-\\d+$")) {
                setPracticeAdequacy(param, value, project);
            } else if (param.matches("^evidence-char-\\d+#\\d+#\\d+$")) {
                setEvidenceChar(param, value, project);
            } else if (param.matches("^evidence-ind-\\d+#\\d+#\\d+$")) {
                setEvidenceInd(param, value, project);
            } else {
                log.debug("skipping parameter " + param);
            }
        }
        projectDao.update(project);
        return "redirect:/appraisal/evidence/";
    }

    private void setPracticeChar(String param, String value, Project project) {
        String idString[] = param.substring(14).split("-");
        Long practiceId = Long.parseLong(idString[0]);
        Long instId = Long.parseLong(idString[1]);
        boolean found = false;
        for (EvidenceRating er : project.getEvidenceRating()) {
            if (er.getPractice().getId().equals(practiceId)
                    &&er.getProcessInstantiation().getId().equals(instId)) {
                er.setCharacterizePracticeImplementation(ratingScaleDao.read(Long.parseLong(value)));
                er.setModifiedBy(getLoggedUser());
                if (log.isDebugEnabled()) {
                    log.debug("Evidence charcterization of practice " + er.getPractice() + " and pi " +
                            er.getProcessInstantiation()+ " set to " +
                            er.getCharacterizePracticeImplementation());
                }
                found = true;
                break;
            }
        }
        if (!found) {
            EvidenceRating er = createEvidenceRating(project, practiceId, instId);
            er.setCharacterizePracticeImplementation(ratingScaleDao.read(Long.parseLong(value)));
        }
    }

    private void setPracticeAdequacy(String param, String value, Project project) {
        String idString[] = param.substring(18).split("-");
        Long practiceId = Long.parseLong(idString[0]);
        Long instId = Long.parseLong(idString[1]);
        boolean found = false;
        for (EvidenceRating er : project.getEvidenceRating()) {
            if (er.getPractice().getId().equals(practiceId) &&
                    er.getProcessInstantiation().getId().equals(instId)) {
                er.setEvidenceAdequacy(PracticeEvidenceAdequacy.valueOf(value));
                er.setModifiedBy(getLoggedUser());
                if (log.isDebugEnabled()) {
                    log.debug("Evidence adequacy of practice " + er.getPractice() + " and pi " +
                            er.getProcessInstantiation()+ " set to " +
                            er.getEvidenceAdequacy());
                }
                found = true;
                break;
            }
        }
        if (!found) {
            EvidenceRating er = createEvidenceRating(project, practiceId, instId);
            er.setEvidenceAdequacy(PracticeEvidenceAdequacy.valueOf(value));
        }
    }

    private void setEvidenceChar(String param, String value, Project project) {
        String idString[] = param.substring(14).split("#");
        Long evidenceId = Long.parseLong(idString[0]);
        Long practiceId = Long.parseLong(idString[1]);
        Long instanceId = Long.parseLong(idString[2]);

        for (EvidenceMapping em : project.getEvidenceMappings()) {
            if (em.getEvidence().getId().equals(evidenceId) && em.getPractice().getId().equals(practiceId) &&
                    em.getProcessInstantiation().getId().equals(instanceId)) {
                em.setCharacteristic(EvidenceCharacteristic.valueOf(value));
                em.getEvidence().setModifiedBy(getLoggedUser());
                if (log.isDebugEnabled()) {
                    log.debug("Characteristic of evidence " + em.getEvidence() + " set to " +
                            em.getCharacteristic());
                }
                break;
            }
        }
    }

    private void setEvidenceInd(String param, String value, Project project) {
        String idString[] = param.substring(13).split("#");
        Long evidenceId = Long.parseLong(idString[0]);
        Long practiceId = Long.parseLong(idString[1]);
        Long instanceId = Long.parseLong(idString[2]);

        for (EvidenceMapping em : project.getEvidenceMappings()) {
            if (em.getEvidence().getId().equals(evidenceId) && em.getPractice().getId().equals(practiceId) &&
                    em.getProcessInstantiation().getId().equals(instanceId)) {
                em.setIndicatorType(IndicatorType.valueOf(value));
                em.getEvidence().setModifiedBy(getLoggedUser());
                if (log.isDebugEnabled()) {
                    log.debug("Indicator type of evidence " + em.getEvidence() + " set to " +
                            em.getIndicatorType());
                }
                break;
            }
        }
    }

    private EvidenceRating createEvidenceRating(Project project, long practiceId, long instId) {
        EvidenceRating er = new EvidenceRating();
        er.setModifiedBy(getLoggedUser());
        er.setProject(project);
        er.setPractice(practiceDao.read(practiceId));
        er.setProcessInstantiation(processInstantiationDao.read(instId));
        er = evidenceRatingDao.create(er);
        project.getEvidenceRating().add(er);
        if (log.isDebugEnabled()) {
            log.debug("Created new Evidence rating for practice " + er.getPractice() + " and inst "+er.getProcessInstantiation());
        }
        return er;
    }

    private static final Log log = LogFactory.getLog(EvidenceController.class);
    
}
