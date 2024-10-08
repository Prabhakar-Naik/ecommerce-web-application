package com.springboot.ecommerce.users.service;

import com.springboot.ecommerce.dto.UserDto;
import com.springboot.ecommerce.users.model.User;
import com.springboot.ecommerce.request.CreateUserRequest;
import com.springboot.ecommerce.request.UpdateUserRequest;

/**
 * @author prabhakar, @Date 10-09-2024
 */
public interface UserService {

    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(UpdateUserRequest request, Long userId);

    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
