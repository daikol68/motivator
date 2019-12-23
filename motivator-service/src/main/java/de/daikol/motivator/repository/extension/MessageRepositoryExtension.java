package de.daikol.motivator.repository.extension;

import de.daikol.motivator.model.Message;

public interface MessageRepositoryExtension {
    void detachMessage(Message message);
}
