package com.authentication_service.controller;

import com.authentication_service.dto.LoginDto;
import com.authentication_service.dto.LoginResponseDto;
import com.authentication_service.dto.RegisterDto;
import com.authentication_service.dto.RegisterResponseDto;
import com.authentication_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController
{
    //////////////////////// Test ///////////////////////////////////////////
    @GetMapping("/test")
    public String test(){
        return "Testing AuthenticationController";
    }

    //////////////////////////////////////////////////////////////////////////

    private final AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService){
        this.authService=authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> newRegistration(@RequestBody RegisterDto registerDto){
        return ResponseEntity.ok(authService.register(registerDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto){

        return ResponseEntity.ok(authService.validateLogin(loginDto));
    }

    @PatchMapping("/update-email/{user-id}")
    public ResponseEntity<String> updateEmail(@PathVariable("user-id") long userId,@RequestParam("email") String email){
        return ResponseEntity.status(HttpStatus.OK).body(authService.updateEmail(userId,email));
    }

    @PatchMapping("/update-password/{user-id}")
    public ResponseEntity<String> updatePassword(@PathVariable("user-id") long userId,
                                                 @RequestParam("password") String password){
        return ResponseEntity.status(HttpStatus.OK).body(authService.updatePassword(userId,password));
    }

    @PatchMapping("/update-status/{user-id}")
    public ResponseEntity<String> updateStatus(@PathVariable("user-id") long userId,
                                               @RequestParam("status") String status){
        return ResponseEntity.status(HttpStatus.OK).body(authService.updateStatus(userId,status));
    }
}
