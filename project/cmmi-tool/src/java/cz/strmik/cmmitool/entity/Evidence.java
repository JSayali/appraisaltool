/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.entity;

import cz.strmik.cmmitool.enums.EvidenceStatus;
import cz.strmik.cmmitool.enums.EvidenceType;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Evidence.findByProjectMapped", query="SELECT DISTINCT m.evidence FROM EvidenceMapping m WHERE m.project = :project ORDER BY m.evidence.name"),
    @NamedQuery(name="Evidence.findByProject", query="SELECT e FROM Evidence e WHERE e.project = :project ORDER BY e.name"),
    @NamedQuery(name="Evidence.findByProjectPractice", query="SELECT DISTINCT m.evidence FROM EvidenceMapping m WHERE m.project = :project AND m.practice = :practice ORDER BY m.evidence.name")
})
public class Evidence extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String link;
    private String label;
    private String source;
    private String description;

    @ManyToOne
    private User modifiedBy;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date modifiedTimestamp;

    @Enumerated(EnumType.STRING)
    private EvidenceType type;
    @Enumerated(EnumType.STRING)
    private EvidenceStatus status;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "evidence")
    private Set<EvidenceMapping> mappings;

    @ManyToOne(optional=false)
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public void setModifiedTimestamp(Date modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public EvidenceStatus getStatus() {
        return status;
    }

    public void setStatus(EvidenceStatus status) {
        this.status = status;
    }

    public EvidenceType getType() {
        return type;
    }

    public void setType(EvidenceType type) {
        this.type = type;
    }

    public Set<EvidenceMapping> getMappings() {
        return mappings;
    }

    public void setMappings(Set<EvidenceMapping> mappings) {
        this.mappings = mappings;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Evidence)) {
            return false;
        }
        Evidence other = (Evidence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cz.strmik.cmmitool.entity.Evidence[id=" + id + "]";
    }

    @PreUpdate
    @PrePersist
    protected void updateTimestamp() {
        this.modifiedTimestamp = new Date();
    }

}
