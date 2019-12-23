package de.daikol.motivator.repository.extension;

import de.daikol.motivator.model.Competition;

public interface CompetitionRepositoryExtension {
    void detachCompetition(Competition competition);
}
