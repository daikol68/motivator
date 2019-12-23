package de.daikol.motivator.repository;

import de.daikol.motivator.model.Message;
import de.daikol.motivator.model.user.User;
import de.daikol.motivator.repository.extension.MessageRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryExtension {
    List<Message> findAllByReceiverEqualsAndReadFalseOrderByCreationDateAsc(User receiver);
}
