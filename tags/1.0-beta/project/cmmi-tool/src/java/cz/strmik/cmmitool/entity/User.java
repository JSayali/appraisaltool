/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.entity;

import cz.strmik.cmmitool.entity.project.TeamMember;
import cz.strmik.cmmitool.enums.ApplicationRole;
import cz.strmik.cmmitool.util.Authority;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Entity
@Table(name="user_")
@NamedQueries({
    @NamedQuery(name="User.findAll", query="SELECT u FROM User u ORDER BY u.name"),
    @NamedQuery(name="User.findActive", query="SELECT u FROM User u WHERE u.enabled = true AND u.nonExpired = true ORDER BY u.name")
})
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private ApplicationRole applicationRole;

    private String password;
    private String name;
    private String email;

    private boolean nonExpired;
    private boolean nonLocked;
    private boolean enabled;

    @OneToMany(mappedBy = "user")
    private Set<TeamMember> memberOfTeams;

    @Transient
    private List<GrantedAuthority> authority;

    @Transient
    private String password2;

    @Transient
    private boolean newUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountNonExpired(boolean nonExpired) {
        this.nonExpired = nonExpired;
    }

    public void setAccountNonLocked(boolean nonLocked) {
        this.nonLocked = nonLocked;
    }

    public ApplicationRole getApplicationRole() {
        return applicationRole;
    }

    public void setApplicationRole(ApplicationRole applicationRole) {
        this.applicationRole = applicationRole;
    }

    public void setRole(String name) {
        applicationRole = (name==null||name.equals("") ? null : ApplicationRole.valueOf(name));
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public String getRole() {
        return applicationRole==null ? null : applicationRole.toString();
    }

    public String getRoleLowerCase() {
        return applicationRole==null ? null : applicationRole.toString().toLowerCase();
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public Set<TeamMember> getMemberOfTeams() {
        return memberOfTeams;
    }

    public void setMemberOfTeams(Set<TeamMember> memberOfTeams) {
        this.memberOfTeams = memberOfTeams;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User[id=" + id + ", applicationRole="+applicationRole+"]";
    }

    @PostLoad
    protected void fillAuthorities() {
        authority = new ArrayList<GrantedAuthority>(1);
        authority.add(new Authority(applicationRole));
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        if(authority==null) {
            fillAuthorities();
        }
        return authority;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return nonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return nonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
