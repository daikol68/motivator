package de.daikol.motivator.controller;

import de.daikol.motivator.Messages;
import de.daikol.motivator.exception.NotFoundException;
import de.daikol.motivator.model.Competition;
import de.daikol.motivator.model.Competitor;
import de.daikol.motivator.model.Message;
import de.daikol.motivator.model.user.User;
import de.daikol.motivator.repository.CompetitionRepository;
import de.daikol.motivator.repository.CompetitorRepository;
import de.daikol.motivator.repository.MessageRepository;
import de.daikol.motivator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/message")
public class MessageController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserController userController;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    CompetitionRepository competitionRepository;

    @Autowired
    CompetitorRepository competitorRepository;

    @Transactional
    @PostMapping("/send")
    public boolean save(@RequestBody Message message) {

        final User user = userController.getAuthenticatedUser();

        if (message == null || message.getType() == null) {
            throw new IllegalArgumentException(Messages.NOT_PROVIDED);
        }

        message.setSender(user);
        messageRepository.save(message);

        return true;
    }

    @Transactional
    @GetMapping("/read/{id}")
    public void read(@PathVariable Long id) {

        final User user = userController.getAuthenticatedUser();

        Optional<Message> optionalNews = messageRepository.findById(id);

        if (!optionalNews.isPresent()) {
            throw new NotFoundException(Messages.NOT_FOUND, new IllegalStateException(Messages.ENTITY_NOT_FOUND_BY_ID));
        }

        Message message = optionalNews.get();

        if (message.getReceiver().equals(user)) {
            message.setRead(true);
            messageRepository.save(message);
        }
    }

    @Transactional
    @GetMapping("/listUnread")
    public List<Message> listUnreadMessages() {
        final User user = userController.getAuthenticatedUser();
        List<Message> messages = messageRepository.findAllByReceiverEqualsAndReadFalseOrderByCreationDateAsc(user);

        for (Message message : messages) {
            messageRepository.detachMessage(message);
            final User sender = message.getSender();
            detachUser(sender);

            final User receiver = message.getReceiver();
            detachUser(receiver);
        }

        return messages;
    }

    public void createCompetitionMessage(User sender, Competition competition, String text, Message.MessageType messageType) {
        for (Competitor competitor : competition.getCompetitors()) {
            if (sender.getId() == competitor.getUser().getId()) {
                continue;
            }
            Message message = new Message();
            message.setText(text);
            message.setSender(sender);
            message.setReceiver(competitor.getUser());
            message.setType(messageType);
            messageRepository.save(message);
        }
    }

    private void detachUser(User sender) {
        userRepository.detachUser(sender);
        sender.setCode(null);
        sender.setPassword(null);
        sender.setRoles(null);
        sender.setEmail(null);
    }

}
