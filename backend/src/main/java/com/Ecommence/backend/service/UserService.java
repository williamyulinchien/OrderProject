package com.Ecommence.backend.service;


  import com.Ecommence.backend.dto.UserRequestDto;
  import com.Ecommence.backend.dto.UserResponseDto;

  import java.util.List;

    public interface UserService {

        List<UserResponseDto> getAllUsers();

        UserResponseDto getUserById(Long id);

        UserResponseDto createUser(UserRequestDto userRequestDto);

        UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);

        void deleteUser(Long id);
    }

