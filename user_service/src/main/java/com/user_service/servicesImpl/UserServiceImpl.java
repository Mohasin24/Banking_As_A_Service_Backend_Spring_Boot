package com.user_service.servicesImpl;

import com.user_service.constant.KycStatus;
import com.user_service.dto.UserProfileRequest;
import com.user_service.dto.UserProfileResponse;
import com.user_service.dto.UserProfileUpdateRequest;
import com.user_service.entity.User;
import com.user_service.kafka.UserServiceEventProducer;
import com.user_service.repository.UserRepo;
import com.user_service.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserServiceEventProducer userServiceEventProducer;

    public UserServiceImpl(UserRepo userRepo, UserServiceEventProducer userServiceEventProducer){
        this.userRepo=userRepo;
        this.userServiceEventProducer=userServiceEventProducer;
    }

    @Override
    public UserProfileResponse createProfile(UserProfileRequest userProfileRequest) {
        User user = User.builder()
                .userId(userProfileRequest.userId())
                .firstName(userProfileRequest.firstName())
                .lastName(userProfileRequest.lastName())
                .address(userProfileRequest.address())
                .dateOfBirth(userProfileRequest.dob())
                .phoneNo(userProfileRequest.phoneNo())
                .kycStatus(KycStatus.PENDING)
                .build();

        User createdUser = userRepo.saveAndFlush(user);

        return UserProfileResponse.builder()
                .id(createdUser.getId())
                .userId(createdUser.getUserId())
                .firstName(createdUser.getFirstName())
                .lastName(createdUser.getLastName())
                .dob(createdUser.getDateOfBirth())
                .address(createdUser.getAddress())
                .phoneNo(createdUser.getPhoneNo())
                .kycStatus(createdUser.getKycStatus().name())
                .build();
    }

    @Override
    public UserProfileResponse updateProfile(long userId, UserProfileUpdateRequest userProfileUpdateRequest) {

        User user = userRepo.findByUserId(userId).get();

        //checks the field if its null if not then compares the value and then sets the new value
        Optional.ofNullable(userProfileUpdateRequest.firstName())
                .filter(f->!f.equals(user.getFirstName()))
                .ifPresent(user::setFirstName);

        Optional.ofNullable(userProfileUpdateRequest.lastName())
                .filter(f->!f.equals(user.getLastName()))
                .ifPresent(user::setLastName);

        Optional.ofNullable(userProfileUpdateRequest.dob())
                .filter(f->!f.equals(user.getDateOfBirth()))
                .ifPresent(user::setDateOfBirth);

        Optional.ofNullable(userProfileUpdateRequest.address())
                .filter(f->!f.equals(user.getAddress()))
                .ifPresent(user::setAddress);

        Optional.ofNullable(userProfileUpdateRequest.phoneNo())
                .filter(f->!f.equals(user.getPhoneNo()))
                .ifPresent(user::setPhoneNo);

        User updatedUser = userRepo.save(user);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userId",updatedUser.getUserId());
        metadata.put("fullName",updatedUser.getFullName());

        userServiceEventProducer.userProfileUpdateEvent(String.valueOf(updatedUser.getUserId()),metadata);

        return UserProfileResponse.builder()
                .id(updatedUser.getId())
                .userId(updatedUser.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNo(user.getPhoneNo())
                .address(user.getAddress())
                .dob(user.getDateOfBirth())
                .kycStatus(user.getKycStatus().toString())
                .build();
    }

    @Override
    public UserProfileResponse getProfile(long userId) {
        User user = userRepo.findByUserId(userId).get();

        return UserProfileResponse.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dob(user.getDateOfBirth())
                .address(user.getAddress())
                .phoneNo(user.getPhoneNo())
                .kycStatus(user.getKycStatus().toString())
                .build();
    }

    @Override
    public boolean deleteProfile(long userId) {
        User user = userRepo.findByUserId(userId).get();

        //Metadata for kafka event
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userId",user.getUserId());
        metadata.put("fullName",user.getFullName());

        userRepo.delete(user);

        if(userRepo.findByUserId(userId).get()==null){

            metadata.put("message",String.format("Failed to delete user with User Id: %d",user.getUserId()));
            userServiceEventProducer.userProfileDeleteEvent(String.valueOf(user.getUserId()),metadata);

            return false;
        }else {
            metadata.put("message",String.format("Successfully deleted user with User Id: %d",user.getUserId()));
            userServiceEventProducer.userProfileDeleteEvent(String.valueOf(user.getUserId()),metadata);

            return true;
        }
    }

    @Override
    public UserProfileResponse updateKycStatus(long userId, String status) {

        User user = userRepo.findByUserId(userId).get();

        user.setKycStatus(KycStatus.valueOf(status.toUpperCase().trim()));
        User updatedUser = userRepo.save(user);

        Map<String,Object> metadata = new HashMap<>();
        metadata.put("userId",user.getUserId());
        metadata.put("fullName",user.getFullName());
        metadata.put("kycStatus",user.getKycStatus().toString());

        userServiceEventProducer.kycStatusUpdateEvent(String.valueOf(user.getUserId()),metadata);

        return UserProfileResponse.builder()
                .id(updatedUser.getId())
                .userId(updatedUser.getUserId())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .dob(updatedUser.getDateOfBirth())
                .address(updatedUser.getAddress())
                .phoneNo(updatedUser.getPhoneNo())
                .kycStatus(updatedUser.getKycStatus().toString())
                .build();
    }
}
