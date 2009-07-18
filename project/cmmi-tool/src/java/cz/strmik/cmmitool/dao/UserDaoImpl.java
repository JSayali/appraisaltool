/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.dao;

import cz.strmik.cmmitool.entity.User;
import cz.strmik.cmmitool.enums.ApplicationRole;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    private String defaultAdminPassword;
    private String defaultAdminAccount;
    
    @Autowired
    private MessageDigestPasswordEncoder passwordEncoder;

    public void setDefaultAdminPassword(String defaultAdminPassword) {
        this.defaultAdminPassword = defaultAdminPassword;
    }

    public void setDefaultAdminAccount(String defaultAdminAccount) {
        this.defaultAdminAccount = defaultAdminAccount;
    }

    @Override
    public void createUser(User user) {
        hashUserPassword(user);
        entityManager.persist(user);
    }

    @Override
    public User updateUser(User user) {
        User current = entityManager.find(User.class, user.getId());
        current.setApplicationRole(user.getApplicationRole());
        current.setEmail(user.getEmail());
        current.setEnabled(user.isEnabled());
        current.setName(user.getName());
        current.setAccountNonExpired(user.isAccountNonExpired());
        current.setAccountNonLocked(user.isAccountNonLocked());
        if(!StringUtils.isEmpty(user.getPassword2())) {
            current.setPassword(user.getPassword2());
            hashUserPassword(current);
        }
        entityManager.merge(current);
        return current;
    }

    @Override
    public User findUser(String id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void removeUser(String id) {
        entityManager.remove(findUser(id));
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
        User user = findUser(userName);
        if (user == null & userName.equals(defaultAdminAccount)) {
            _log.info("Administartor user not found in DB, creating one.");
            user = getDefaultAdminUser();
            createUser(user);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User with id=" + userName + " not found!");
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return entityManager.createNamedQuery("User.findAll").getResultList();
    }

    @Override
    public List<User> findActive() {
        return entityManager.createNamedQuery("User.findActive").getResultList();
    }

    private void hashUserPassword(User user) {
        String hashed = passwordEncoder.encodePassword(user.getPassword(), user.getUsername());
        user.setPassword(hashed);
        user.setPassword2(null);
    }

    private User getDefaultAdminUser() {
        User admin = new User();
        admin.setId(defaultAdminAccount);
        admin.setApplicationRole(ApplicationRole.ROLE_SUPERVISOR);
        admin.setName("Administrator");
        admin.setEnabled(true);
        admin.setAccountNonExpired(true);
        admin.setAccountNonLocked(true);
        admin.setPassword(defaultAdminPassword);
        return admin;
    }
    
    private static Log _log = LogFactory.getLog(UserDao.class);
}
