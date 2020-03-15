package de.daikol.motivator.controller;

import com.google.common.io.Files;
import de.daikol.motivator.Messages;
import de.daikol.motivator.exception.NotFoundException;
import de.daikol.motivator.model.Message;
import de.daikol.motivator.model.user.Role;
import de.daikol.motivator.model.user.RoleType;
import de.daikol.motivator.model.user.User;
import de.daikol.motivator.repository.MessageRepository;
import de.daikol.motivator.repository.RoleRepository;
import de.daikol.motivator.repository.UserRepository;
import de.daikol.motivator.service.EmailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/registration")
public class RegistrationController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    MessageRepository messageRepository;

    @Transactional
    @PostMapping("/start")
    public void registration(@RequestBody User user) {

        if (user == null || StringUtils.isBlank(user.getEmail()) || StringUtils.isBlank(user.getName()) || StringUtils.isBlank(user.getPassword())) {
            throw new IllegalArgumentException(Messages.NOT_PROVIDED);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User save = userRepository.save(user);
        emailService.sendRegistrationEmail(save);

        Message message = new Message();
        message.setText(Messages.NEWS_USER_REGISTRATION_START);
        message.setSender(null);
        message.setReceiver(save);
        message.setType(Message.MessageType.UPDATE);

        messageRepository.save(message);
    }

    @Transactional
    @GetMapping("/complete/{email}/{code}")
    public String completeRegistration(@PathVariable String email, @PathVariable String code) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(code)) {
            throw new IllegalArgumentException(Messages.NOT_PROVIDED);
        }

        User user = userRepository.findUserByEmailEqualsAndCodeEquals(email, code);

        if (user == null) {
            throw new NotFoundException(Messages.NOT_FOUND, new IllegalStateException(Messages.USER_NOT_FOUND_BY_EMAIL_AND_CODE));
        }

        user.setRegistration(true);
        Role role = new Role();
        role.setType(RoleType.USER);

        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);

        Message message = new Message();
        message.setText(Messages.NEWS_USER_REGISTRATION_COMPLETE);
        message.setSender(null);
        message.setReceiver(user);
        message.setType(Message.MessageType.UPDATE);

        messageRepository.save(message);

        try {
            String content = Files.asCharSource(new File("/html/complete.html"), StandardCharsets.UTF_8).read();
            content.replace("{email}", email);
            return content;
        } catch (IOException e) {
            throw new NotFoundException(Messages.NOT_FOUND, new IllegalStateException(Messages.FILE_NOT_FOUND));
        }
    }

    @Transactional
    @PutMapping("/resend/{id}")
    public void resendRegistration(@PathVariable Long id) {

        if (id == null) {
            throw new IllegalArgumentException(Messages.NOT_PROVIDED);
        }

        Optional<User> optional = userRepository.findById(id);

        if (!optional.isPresent()) {
            throw new NotFoundException(Messages.NOT_FOUND, new IllegalStateException(Messages.USER_NOT_FOUND_BY_ID));
        }

        emailService.sendRegistrationEmail(optional.get());
    }

}
