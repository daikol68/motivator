package de.daikol.motivator.controller;

import de.daikol.motivator.Messages;
import de.daikol.motivator.exception.NotFoundException;
import de.daikol.motivator.model.user.User;
import de.daikol.motivator.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @GetMapping
    public User get() {
        User user = getAuthenticatedUser();
        return user;
    }

    @Transactional
    @GetMapping("/find/{name}")
    public List<User> findByName(@PathVariable String name) {
        User user = getAuthenticatedUser();

        List<User> users = userRepository.findUsersByNameContainingIgnoreCase(name);
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId() == user.getId()) {
                iterator.remove();
            }
        }
        return users;
    }

    @Transactional
    @PostMapping("/update")
    public boolean update(@RequestBody User user) {

        User authUser = getAuthenticatedUser();

        if (user == null) {
            throw new IllegalArgumentException(Messages.NOT_PROVIDED);
        }

        Optional<User> optionalUser = userRepository.findById(authUser.getId());

        if (!optionalUser.isPresent()) {
            throw new NotFoundException(Messages.NOT_FOUND, new IllegalStateException(Messages.USER_NOT_FOUND_BY_ID));
        }

        User loaded = optionalUser.get();
        if (StringUtils.isNotBlank(user.getPassword())) {
            loaded.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getPicture() != null) {
            loaded.setPicture(user.getPicture());
        }

        userRepository.save(loaded);
        return true;
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findUserByNameEquals(userName);
        if (user == null) {
            throw new IllegalArgumentException(Messages.USER_NOT_FOUND_BY_NAME);
        }
        return user;
    }


}
