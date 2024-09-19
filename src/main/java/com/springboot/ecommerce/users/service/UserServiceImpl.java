package com.springboot.ecommerce.users.service;

import com.springboot.ecommerce.dto.UserDto;
import com.springboot.ecommerce.exceptions.AlreadyExistException;
import com.springboot.ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.ecommerce.models.Role;
import com.springboot.ecommerce.users.model.User;
import com.springboot.ecommerce.repository.RoleRepository;
import com.springboot.ecommerce.users.repository.UserRepository;
import com.springboot.ecommerce.request.CreateUserRequest;
import com.springboot.ecommerce.request.UpdateUserRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 * @author prabhakar, @Date 10-09-2024
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        Role userRole = this.roleRepository.findByName("ROLE_USER").get();
        return Optional.of(request)
                .filter(user -> !this.userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setRoles(Set.of(userRole));
                    user.setEmail(request.getEmail());
                    user.setPassword(this.passwordEncoder.encode(request.getPassword()));
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    return this.userRepository.save(user);
                }).orElseThrow(() -> new AlreadyExistException("OOPS! "+request.getEmail()+" Already exist."));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return this.userRepository.findById(userId).map(existUser ->{
            existUser.setFirstName(request.getFirstName());
            existUser.setLastName(request.getLastName());
            return this.userRepository.save(existUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User Not Found!."));
    }

    @Override
    public void deleteUser(Long userId) {
        this.userRepository.findById(userId).
                ifPresentOrElse(this.userRepository::delete, () -> {
                    throw new ResourceNotFoundException("User Not Found!.");
                });
    }

    @Override
    public UserDto convertUserToDto(User user){
        return this.modelMapper.map(user,UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return this.userRepository.findByEmail(email);
    }
}
