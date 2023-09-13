package com.phincon.laza.repository;

import com.phincon.laza.model.entity.Provider;
import com.phincon.laza.model.entity.Role;
import com.phincon.laza.model.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testFindAlToUser_thenCorrect() {
        Set<Role> listRole = new HashSet<>();
        Set<Provider> listProvider = new HashSet<>();

        User user1 = new User();
        user1.setName("John Doe");
        user1.setUsername("johndoe");
        user1.setEmail("johndoe@mail.com");
        user1.setPassword(passwordEncoder.encode("password"));
        user1.setRoles(listRole);
        user1.setProviders(listProvider);

        User user2 = new User();
        user2.setName("Alice Smith");
        user2.setUsername("alicesmith");
        user2.setEmail("alicesmith@mail.com");
        user2.setPassword(passwordEncoder.encode("password"));
        user2.setRoles(listRole);
        user2.setProviders(listProvider);

        userRepository.saveAll(List.of(user1, user2));

        List<User> listUser = userRepository.findAll();

        assertEquals(2, listUser.size());
    }

    @Test
    public void testSaveAndFindByIdToUser_thenCorrect() {
        Set<Role> listRole = new HashSet<>();
        Set<Provider> listProvider = new HashSet<>();

        User user = new User();
        user.setName("John Doe");
        user.setUsername("johndoe");
        user.setEmail("johndoe@mail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(listRole);
        user.setProviders(listProvider);

        userRepository.save(user);

        Optional<User> findUser = userRepository.findById(user.getId());

        assertTrue(findUser.isPresent());
        assertEquals(findUser.get().getId(), user.getId());
    }

    @Test
    public void testSaveAndFindByUsernameToUser_thenCorrect() {
        Set<Role> listRole = new HashSet<>();
        Set<Provider> listProvider = new HashSet<>();

        User user = new User();
        user.setName("John Doe");
        user.setUsername("johndoe");
        user.setEmail("johndoe@mail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(listRole);
        user.setProviders(listProvider);

        userRepository.save(user);

        Optional<User> findUser = userRepository.findByUsername(user.getUsername());

        assertTrue(findUser.isPresent());
        assertEquals(findUser.get().getUsername(), user.getUsername());
    }

    @Test
    public void testUpdateAndFindByIdToUser_thenCorrect() {
        Set<Role> listRole = new HashSet<>();
        Set<Provider> listProvider = new HashSet<>();

        User user = new User();
        user.setName("John Doe");
        user.setUsername("johndoe");
        user.setEmail("johndoe@mail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(listRole);
        user.setProviders(listProvider);

        userRepository.save(user);

        Optional<User> findUser = userRepository.findById(user.getId());

        findUser.get().setName("John Doe Updated");
        findUser.get().setUsername("johndoeupdated");
        findUser.get().setEmail("johndoeupdated@mail.com");

        userRepository.save(user);

        assertTrue(findUser.isPresent());
        assertEquals(findUser.get().getId(), user.getId());
        assertEquals(findUser.get().getName(), user.getName());
        assertEquals(findUser.get().getUsername(), user.getUsername());
        assertEquals(findUser.get().getEmail(), user.getEmail());
    }

}
