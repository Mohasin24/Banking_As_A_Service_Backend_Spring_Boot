package com.authentication_service.servicesImpl;

import com.authentication_service.JWT.JwtService;
import com.authentication_service.constant.Role;
import com.authentication_service.constant.Status;
import com.authentication_service.dto.LoginDto;
import com.authentication_service.dto.LoginResponseDto;
import com.authentication_service.dto.RegisterDto;
import com.authentication_service.dto.RegisterResponseDto;
import com.authentication_service.entity.Auth;
import com.authentication_service.kafka.AuthenticationEventProducer;
import com.authentication_service.repository.AuthRepo;
import com.authentication_service.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepo authRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthenticationEventProducer authenticationEventProducer;

    @Autowired
    public AuthServiceImpl(AuthRepo authRepo,BCryptPasswordEncoder passwordEncoder,AuthenticationManager authenticationManager,JwtService jwtService,AuthenticationEventProducer authenticationEventProducer){
        this.authRepo=authRepo;
        this.passwordEncoder=passwordEncoder;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
        this.authenticationEventProducer=authenticationEventProducer;
    }

    @Override
    public RegisterResponseDto register(RegisterDto registerDto) {
        Auth auth = Auth.builder()
                .username(registerDto.username())
                .email(registerDto.email())
                .password(passwordEncoder.encode(registerDto.password()))
                .role(Role.valueOf(registerDto.role().toUpperCase()))
                .status(Status.ACTIVE)
                .build();

        Auth savedObject = authRepo.save(auth);

        // Trigger kafka event notification
        authenticationEventProducer.newUserRegistrationEvent(String.valueOf(savedObject.getUserId()),null);

        return new RegisterResponseDto(
                savedObject.getUsername(),
                savedObject.getEmail(),
                savedObject.getRole().name(),
                savedObject.getStatus().name(),
                savedObject.getCreatedAt(),
                savedObject.getUpdatedAt()
        );
    }

    @Override
    public LoginResponseDto validateLogin(LoginDto loginDto) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password()));

        if(authentication.isAuthenticated()){
            Auth loggedInUser = authRepo.findUserByUsername(loginDto.username());

            String token = jwtService.generateToken(loggedInUser);

            //kafka notification
            Map<String, Object> metadata = new HashMap<>();

            metadata.put("username",loggedInUser.getUsername());
            metadata.put("date", new Date().getTime());

            authenticationEventProducer.userLoginEvent(String.valueOf(loggedInUser.getUserId()),metadata);

            return new LoginResponseDto(
                    loggedInUser.getUsername(),
                    loggedInUser.getEmail(),
                    loggedInUser.getRole().name(),
                    loggedInUser.getStatus().name(),
                    token
            );
        }

        throw new RuntimeException("User not valid!");
    }

    @Override
    public String updateEmail(long userId, String email) {
        Auth loggedInUser = authRepo.findById(userId).get();

        loggedInUser.setEmail(email);

        loggedInUser = authRepo.saveAndFlush(loggedInUser);

        //Trigger kafka event notification
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("updatedEmail", loggedInUser.getEmail());
        authenticationEventProducer.userEmailUpdateEvent(String.valueOf(userId),metadata);

        return loggedInUser.getEmail();
    }

    @Override
    public String updatePassword(long userId, String password) {
        Auth loggedInUser = authRepo.findById(userId).get();
        
        loggedInUser.setPassword(passwordEncoder.encode(password));

        loggedInUser = authRepo.saveAndFlush(loggedInUser);

        // Kafka Notification
        authenticationEventProducer.updateUserPasswordEvent(String.valueOf(userId),null);

        return loggedInUser.getPassword();
    }

    @Override
    public String updateStatus(long userId, String status) {
        Auth loggedInUser = authRepo.findById(userId).get();
        loggedInUser.setStatus(Status.valueOf(status.toUpperCase()));
        loggedInUser = authRepo.saveAndFlush(loggedInUser);
        return loggedInUser.getStatus().name();
    }
}

