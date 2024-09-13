package com.springboot.ecommerce.controller;

import com.springboot.ecommerce.dto.UserDto;
import com.springboot.ecommerce.exceptions.AlreadyExistException;
import com.springboot.ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.ecommerce.models.User;
import com.springboot.ecommerce.request.CreateUserRequest;
import com.springboot.ecommerce.request.UpdateUserRequest;
import com.springboot.ecommerce.response.ApiResponse;
import com.springboot.ecommerce.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author prabhakar, @Date 10-09-2024
 */
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping(value = "/getUserById/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = this.userService.getUserById(userId);
            UserDto userDto = this.userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PostMapping(value = "/createRequest")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request){
        try {
            User user = this.userService.createUser(request);
            UserDto userDto = this.userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Success",userDto));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(CONFLICT)
                    .body(new ApiResponse("OOPS! ",e.getMessage()));
        }
    }


    @PutMapping(value = "/updateUserById/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request,@PathVariable Long userId){
        try {
            User user = this.userService.updateUser(request,userId);
            UserDto userDto = this.userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Update User Success",userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping(value = "/deleteUserById/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        try {
            this.userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("User Deleted Success",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }



}
