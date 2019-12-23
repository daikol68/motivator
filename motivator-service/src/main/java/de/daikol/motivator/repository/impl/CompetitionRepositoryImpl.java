package de.daikol.motivator.repository.impl;

import de.daikol.motivator.model.Competition;
import de.daikol.motivator.repository.extension.CompetitionRepositoryExtension;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CompetitionRepositoryImpl implements CompetitionRepositoryExtension {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void detachCompetition(Competition competition) {
        entityManager.detach(competition);
    }
}
