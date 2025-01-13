package com.Ecommence.backend.mapper;

import com.Ecommence.backend.dto.UserRequestDto;
import com.Ecommence.backend.dto.UserResponseDto;
import com.Ecommence.backend.entity.User;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserMapper {

    // User entity -> UserResponseDto
    public static UserResponseDto mapToUserResponseDto(User user) {
        if (user == null) {
            return null;
        }
        UserResponseDto UserResponseDto = new UserResponseDto();
        UserResponseDto.setUserId(user.getId());
        UserResponseDto.setUsername(user.getUsername());
        UserResponseDto.setFirstName(user.getFirstName());
        UserResponseDto.setLastName(user.getLastName());
        UserResponseDto.setEmail(user.getEmail());
        UserResponseDto.setPhoneNumber(user.getPhoneNumber());
        UserResponseDto.setDateOfBirth(user.getDateOfBirth());
        //address can be null when user is created
        UserResponseDto.setAddresses(user.getAddresses() != null
                        ? user.getAddresses().stream()
                        .map(AddressMapper::mapToAddressDto)
                        .collect(Collectors.toList())
                        : new ArrayList<>());
        UserResponseDto.setOrders(user.getOrders() != null
                        ? user.getOrders().stream()
                        .map(OrderMapper::mapToOrderSummaryDto)
                        .collect(Collectors.toList())
                        : new ArrayList<>());
        UserResponseDto.setProfileImageUrl(user.getProfileImageUrl());
        return UserResponseDto;
    }

    // UserRequestDto -> User entity
    public static User mapToUser(UserRequestDto userRequestDto) {
        if (userRequestDto == null) {
            return null;
        }
        User user = new User();
        user.setUsername(userRequestDto.getUsername());
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setDateOfBirth(userRequestDto.getDateOfBirth());
        user.setPassword(userRequestDto.getPassword());
        if (userRequestDto.getProfileImageUrl() != null && !userRequestDto.getProfileImageUrl().isEmpty()) {
            user.setProfileImageUrl(userRequestDto.getProfileImageUrl());
        }
        return user;
    }
}

