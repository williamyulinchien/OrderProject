package com.Ecommence.backend.controller;

import com.Ecommence.backend.dto.UserRequestDto;
import com.Ecommence.backend.dto.UserResponseDto;
import com.Ecommence.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    // DI dependency
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    // 1. Create a user
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto savedUser = userService.createUser(userRequestDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

    }
    // 2. Get all users
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    // 3. Get a specific user
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


    // 4.Update a specific user
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@Valid@PathVariable Long id, @RequestBody UserRequestDto  userRequestDto ) {
        return ResponseEntity.ok(userService.updateUser(id, userRequestDto));
    }
    // 5.Delete a specific user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("{user:User delete successfully!}");
    }
}
