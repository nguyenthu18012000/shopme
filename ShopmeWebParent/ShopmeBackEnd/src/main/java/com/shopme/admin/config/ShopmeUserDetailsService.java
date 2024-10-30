package com.shopme.admin.config;

import com.shopme.admin.repository.UserRepository;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ShopmeUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepo.getUserByEmail(email);

        if (user != null) {
            return new ShopmeUserDetails(user);
        }

        throw new UsernameNotFoundException("Could not find user with this email");
    }
}