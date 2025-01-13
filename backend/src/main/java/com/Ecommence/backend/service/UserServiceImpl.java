package com.Ecommence.backend.service;

import com.Ecommence.backend.dto.AddressDto;
import com.Ecommence.backend.dto.UserRequestDto;
import com.Ecommence.backend.dto.UserResponseDto;
import com.Ecommence.backend.entity.Address;
import com.Ecommence.backend.entity.User;
import com.Ecommence.backend.exception.UserException;
import com.Ecommence.backend.mapper.AddressMapper;
import com.Ecommence.backend.mapper.UserMapper;
import com.Ecommence.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;





@Service
public class UserServiceImpl implements UserService {
    //DI dependencies
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    //Method to get all users
    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUserResponseDto)
                .collect(Collectors.toList());
    }

    //Method to get  a specific user
    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return UserMapper.mapToUserResponseDto(user);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        // check the password
        if (userRequestDto.getPassword() == null || userRequestDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        // check the email
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new UserException("Email already exists: " + userRequestDto.getEmail());
        }

        // Check the username
        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            throw new UserException("Username already exists: " + userRequestDto.getUsername());
        }

        // userRequestDto to user
        User user = UserMapper.mapToUser(userRequestDto);

        // password encryption
        String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());
        user.setPassword(encodedPassword);

        //  address
        if (userRequestDto.getAddresses() != null && !userRequestDto.getAddresses().isEmpty()) {
            for (AddressDto addressDto : userRequestDto.getAddresses()) {
                Address address = AddressMapper.mapToAddress(addressDto, user);
                user.getAddresses().add(address);
            }
        }
        // image url
        if (userRequestDto.getProfileImageUrl() != null && !userRequestDto.getProfileImageUrl().isEmpty()) {
            user.setProfileImageUrl(userRequestDto.getProfileImageUrl());
        } else {
            user.setProfileImageUrl("https://example.com/default_user.png");
        }


        // save to database
        User savedUser = userRepository.save(user);

        // return to mapToUserResponseDto
        return UserMapper.mapToUserResponseDto(savedUser);
    }
    // Method: Update a user
    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        // check the user id
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with id: " + id));

        // check the existing email
        if (userRepository.existsByEmailAndIdNot(userRequestDto.getEmail(), id)) {
            throw new UserException("Email already exists: " + userRequestDto.getEmail());
        }
        // check the existing username
        if (userRepository.existsByUsernameAndIdNot(userRequestDto.getUsername(), id)) {
            throw new UserException("Username already exists: " + userRequestDto.getUsername());
        }

        user.setUsername(userRequestDto.getUsername());
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setDateOfBirth(userRequestDto.getDateOfBirth());
        if (userRequestDto.getProfileImageUrl() != null && !userRequestDto.getProfileImageUrl().isEmpty()) {
            user.setProfileImageUrl(userRequestDto.getProfileImageUrl());
        } else {
            user.setProfileImageUrl("https://example.com/default_user.png");
        }

        // password update
        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        }

        // address update
        if (userRequestDto.getAddresses() != null) {
            user.getAddresses().clear();
            for (AddressDto addressDto : userRequestDto.getAddresses()) {
                Address address = AddressMapper.mapToAddress(addressDto, user);
                user.getAddresses().add(address);
            }
        }
        // save to database
        User updatedUser = userRepository.save(user);

        // return to UserResponseDto
        return UserMapper.mapToUserResponseDto(updatedUser);
    }


    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
