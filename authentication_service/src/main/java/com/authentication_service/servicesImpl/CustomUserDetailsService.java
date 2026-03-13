package com.authentication_service.servicesImpl;

import com.authentication_service.entity.Auth;
import com.authentication_service.repository.AuthRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

//    private final AuthService authService;

    private final AuthRepo authRepo;

    @Autowired
    public CustomUserDetailsService(AuthRepo authRepo){
        this.authRepo=authRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Auth auth = authRepo.findUserByUsername(username);
        return new com.authentication_service.servicesImpl.CustomUserDetail(auth);
    }
}
