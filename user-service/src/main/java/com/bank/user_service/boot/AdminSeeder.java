package com.bank.user_service.boot;



import com.bank.user_service.Role.Entity.Role;
import com.bank.user_service.Role.Repository.RoleRepository;
import com.bank.user_service.User.Entity.User;
import com.bank.user_service.User.Repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createSuperAdmin();
    }

    private void createSuperAdmin() {
        String adminEmail = "karanja99erick@gmail.com";

        // Check if Super Admin already exists
        if (userRepository.findByEmail(adminEmail).isPresent()) {
            return;
        }

        // Fetch SUPER_ADMIN role, create it if missing
        Role superAdminRole = roleRepository.findByName("SUPER_ADMIN")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("SUPER_ADMIN");
                    //newRole.setDescription("Super Administrator role");
                    return roleRepository.save(newRole);
                });

        // Create new Super Admin user
        User superAdmin = new User();
        superAdmin.setName("Super Admin");
        superAdmin.setEmail(adminEmail);
        superAdmin.setPassword(passwordEncoder.encode("Test@12345"));
        superAdmin.setRoles(Set.of(superAdminRole));

        userRepository.save(superAdmin);
    }
}

