/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.wrapper;

import cz.strmik.cmmitool.entity.Method;
import cz.strmik.cmmitool.entity.Model;
import cz.strmik.cmmitool.entity.Project;
import cz.strmik.cmmitool.entity.User;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class OrganizationWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    // list
    private Long selectedOrganizationId;
    private Map<Long, String> aviableOrganizations;
    private String selectedOrganizationName;
    private List<Project> projects;
    
    // form
    private boolean create;
    private Project project;
    private Map<String, Method> methods;
    private Map<String, Model> models;
    private List<User> users;

    public Map<Long, String> getAviableOrganizations() {
        return aviableOrganizations;
    }

    public void setAviableOrganizations(Map<Long, String> aviableOrganizations) {
        this.aviableOrganizations = aviableOrganizations;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public Long getSelectedOrganizationId() {
        return selectedOrganizationId;
    }

    public void setSelectedOrganizationId(Long selectedOrganizationId) {
        this.selectedOrganizationId = selectedOrganizationId;
    }

    public String getSelectedOrganizationName() {
        return selectedOrganizationName;
    }

    public void setSelectedOrganizationName(String selectedOrganizationName) {
        this.selectedOrganizationName = selectedOrganizationName;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public void setMethods(Map<String, Method> methods) {
        this.methods = methods;
    }

    public Map<String, Model> getModels() {
        return models;
    }

    public void setModels(Map<String, Model> models) {
        this.models = models;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
