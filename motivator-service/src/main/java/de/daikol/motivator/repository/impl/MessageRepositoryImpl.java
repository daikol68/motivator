package de.daikol.motivator.repository.impl;

import de.daikol.motivator.model.Message;
import de.daikol.motivator.repository.extension.MessageRepositoryExtension;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MessageRepositoryImpl implements MessageRepositoryExtension {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void detachMessage(Message message) {
        entityManager.detach(message);
    }
}
