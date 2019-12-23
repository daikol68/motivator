package de.daikol.motivator.repository.impl;

import de.daikol.motivator.model.user.User;
import de.daikol.motivator.repository.extension.UserRepositoryExtension;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserRepositoryImpl implements UserRepositoryExtension {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void detachUser(User user) {
        entityManager.detach(user);
    }
}
