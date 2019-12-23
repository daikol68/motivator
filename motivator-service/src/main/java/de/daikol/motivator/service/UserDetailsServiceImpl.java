package de.daikol.motivator.service;

import de.daikol.motivator.model.user.User;
import de.daikol.motivator.model.user.Role;
import de.daikol.motivator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findUserByNameEquals(s);

        if (user == null) {
            throw new UsernameNotFoundException("User " + s + " konnte nicht gefunden werden.");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRoles() == null) {
            return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);
        }

        for (Role role : user.getRoles()) {
            GrantedAuthority authority = new SimpleGrantedAuthority(role.getType().toString());
            authorities.add(authority);
        }
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);
    }
}










