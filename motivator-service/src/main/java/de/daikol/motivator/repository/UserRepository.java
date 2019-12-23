package de.daikol.motivator.repository;

import de.daikol.motivator.model.user.User;
import de.daikol.motivator.repository.extension.UserRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryExtension {

    User findUserByNameEquals(String name);

    User findUserByEmailEqualsAndCodeEquals(String email, String code);

    List<User> findUsersByNameContainingIgnoreCase(String name);

    User findUserByNameEqualsAndPasswordEquals(String name, String password);
}
