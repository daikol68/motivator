package de.daikol.motivator.repository.extension;

import de.daikol.motivator.model.user.User;

public interface UserRepositoryExtension {
    void detachUser(User user);
}
