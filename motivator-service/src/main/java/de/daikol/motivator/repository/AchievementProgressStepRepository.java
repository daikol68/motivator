package de.daikol.motivator.repository;

import de.daikol.motivator.model.AchievementProgressStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementProgressStepRepository extends JpaRepository<AchievementProgressStep, Long> {
    List<AchievementProgressStep> findByUserIdEquals(long userId);
    List<AchievementProgressStep> findByAchievementIdEqualsAndUserIdEquals(long achievementId, long userId);
}
