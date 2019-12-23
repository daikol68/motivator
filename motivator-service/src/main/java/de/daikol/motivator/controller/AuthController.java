package de.daikol.motivator.controller;

import de.daikol.motivator.Messages;
import de.daikol.motivator.model.user.LoginData;
import de.daikol.motivator.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Transactional
    @PostMapping("/login")
    public Boolean login(@RequestBody LoginData data) {
        if (data == null || StringUtils.isBlank(data.getUsername())) {
            throw new IllegalArgumentException(Messages.NOT_PROVIDED);
        }
        return true;
    }
}
