package com.springboot.ecommerce.controller;

import com.springboot.ecommerce.request.LoginRequest;
import com.springboot.ecommerce.response.ApiResponse;
import com.springboot.ecommerce.response.JwtResponse;
import com.springboot.ecommerce.security.jwt.JwtUtils;
import com.springboot.ecommerce.security.user.ShopUserDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author prabhakar, @Date 11-09-2024
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request){
        try {
            Authentication authentication = this.authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = this.jwtUtils.generateTokenForUser(authentication);
            ShopUserDetail userDetail = (ShopUserDetail) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(userDetail.getId(), jwtToken);
            return ResponseEntity.ok(new ApiResponse("Login Successful: ",jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Invalid email or password",e.getMessage()));
        }
    }


}
