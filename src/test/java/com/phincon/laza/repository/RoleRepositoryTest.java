package com.phincon.laza.repository;

import com.phincon.laza.model.entity.ERole;
import com.phincon.laza.model.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testSaveAndFindByNameToRole_thenCorrect() {
        Role role = new Role();
        role.setName(ERole.USER);

        roleRepository.save(role);

        Optional<Role> findRole = roleRepository.findByName(role.getName());

        assertTrue(findRole.isPresent());
        assertEquals(findRole.get().getName(), role.getName());
    }
}
