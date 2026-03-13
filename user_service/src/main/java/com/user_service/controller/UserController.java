package com.user_service.controller;

import com.user_service.dto.KycStatus;
import com.user_service.dto.UserProfileRequest;
import com.user_service.dto.UserProfileResponse;
import com.user_service.dto.UserProfileUpdateRequest;
import com.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController
{
    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    ////////////////////////////////////////////////////////////////////////
    //                                TEST                                //
    ////////////////////////////////////////////////////////////////////////
    @GetMapping("/test")
    public String test(){
        return "UserController controller";
    }
    ////////////////////////////////////////////////////////////////////////

    @PostMapping("/create")
    public ResponseEntity<UserProfileResponse> createProfile(@RequestBody UserProfileRequest userProfileRequest){
        return ResponseEntity.status(HttpStatus.OK).body(userService.createProfile(userProfileRequest));
    }

    @PatchMapping("/update-kyc-status/{user-id}")
    public ResponseEntity<UserProfileResponse> updateKycStatus(@PathVariable("user-id") long userId, @RequestBody KycStatus kycStatus){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateKycStatus(userId,kycStatus.kycStatus()));
    }

    @PatchMapping("/update/{user-id}")
    ResponseEntity<UserProfileResponse> updateProfile(@PathVariable("user-id") long userId, @RequestBody UserProfileUpdateRequest userProfileUpdateRequest){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateProfile(userId,userProfileUpdateRequest));
    }

    @GetMapping("/profile/{user-id}")
    ResponseEntity<UserProfileResponse> getProfile(@PathVariable("user-id") long userId){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getProfile(userId));
    }

    @DeleteMapping("/delete/{user-id}")
    ResponseEntity<String> deleteProfile(@PathVariable("user-id") long userId){
        if(!userService.deleteProfile(userId)){
            throw new RuntimeException("User Profile Delete failed");
        }

        return ResponseEntity.status(HttpStatus.OK).body(String.format("Profile with user-id %d successfully deleted.",userId));
    }
}