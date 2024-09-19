package com.springboot.ecommerce.auth2.o.security.user;

import com.springboot.ecommerce.users.model.User;
import com.springboot.ecommerce.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author prabhakar, @Date 11-09-2024
 */
@Service
@RequiredArgsConstructor
public class ShopUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = Optional.ofNullable(this.userRepository.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found!"));
        return ShopUserDetail.buildUserDetails(user);
    }
}
