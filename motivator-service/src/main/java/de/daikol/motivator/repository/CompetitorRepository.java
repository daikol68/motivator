package de.daikol.motivator.repository;

import de.daikol.motivator.model.Competitor;
import de.daikol.motivator.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetitorRepository extends JpaRepository<Competitor, Long> {

    List<Competitor> findDistinctByUserEquals(User user);
}
