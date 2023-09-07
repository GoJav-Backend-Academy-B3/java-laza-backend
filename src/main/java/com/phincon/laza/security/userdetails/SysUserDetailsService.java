package com.phincon.laza.security.userdetails;

import com.phincon.laza.model.entity.Role;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SysUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> findUser = userRepository.findByUsername(username);
        List<String> roles = new ArrayList<>();

        for (Role role : findUser.get().getRoles()) {
            roles.add(role.getName().toString());
        }

        if (findUser.isPresent()) {
            UserDetails user = org.springframework.security.core.userdetails.User
                    .withUsername(findUser.get().getUsername())
                    .password(findUser.get().getPassword())
                    .roles(roles.toArray(new String[0]))
                    .build();
            return user;
        }

        throw new UsernameNotFoundException(String.format("User not found with username: %s", username));
    }
}
