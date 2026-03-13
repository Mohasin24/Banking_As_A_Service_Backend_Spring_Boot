package com.user_service.service;

import com.user_service.constant.KycStatus;
import com.user_service.dto.UserProfileRequest;
import com.user_service.dto.UserProfileResponse;
import com.user_service.dto.UserProfileUpdateRequest;
import com.user_service.repository.UserRepo;

public interface UserService
{
    UserProfileResponse createProfile(UserProfileRequest userProfileRequest);

    UserProfileResponse updateProfile(long userId, UserProfileUpdateRequest userProfileUpdateRequest);

    UserProfileResponse getProfile(long userId);

    boolean deleteProfile(long userId);

    UserProfileResponse updateKycStatus(long userId,String status);

}
