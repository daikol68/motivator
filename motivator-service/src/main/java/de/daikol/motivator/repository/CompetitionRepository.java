package de.daikol.motivator.repository;

import de.daikol.motivator.model.Achievement;
import de.daikol.motivator.model.Competition;
import de.daikol.motivator.repository.extension.CompetitionRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionRepository extends JpaRepository<Competition, Long>, CompetitionRepositoryExtension {
    Competition findByAchievementsIsIn(Achievement achievement);
    Competition findByRewardsIsIn(long id);
}
