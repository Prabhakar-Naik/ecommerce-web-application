package com.springboot.ecommerce.data;

import com.springboot.ecommerce.users.model.Role;
import com.springboot.ecommerce.users.model.User;
import com.springboot.ecommerce.users.repository.RoleRepository;
import com.springboot.ecommerce.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author prabhakar, @Date 10-09-2024
 */
@Transactional
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles =  Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultUsersIfNotExist();
        createDefaultRoleIfNotExits(defaultRoles);
        createDefaultAdminIfNotExits();
    }


    private void createDefaultUsersIfNotExist() {
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        for (int i=1; i<=5 ;i++){
            String defaultEmail = "user"+i+"@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setRoles(Set.of(userRole));
            user.setFirstName("The User");
            user.setLastName("User "+i);
            user.setEmail(defaultEmail);
            user.setPassword(this.passwordEncoder.encode("12345"+i));
            this.userRepository.save(user);
            System.out.println("Default vet user: "+i+" created Successfully.");
        }
    }


    private void createDefaultAdminIfNotExits(){
        Role adminRole = this.roleRepository.findByName("ROLE_ADMIN").get();
        for (int i = 1; i<=2; i++){
            String defaultEmail = "admin"+i+"@email.com";
            if (userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"+i));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("Default admin user " + i + " created successfully.");
        }
    }


    private void createDefaultRoleIfNotExits(Set<String> roles){
        roles.stream()
                .filter(role -> this.roleRepository.findByName(role)
                        .isEmpty())
                .map(Role:: new).forEach(this.roleRepository::save);

    }

}
