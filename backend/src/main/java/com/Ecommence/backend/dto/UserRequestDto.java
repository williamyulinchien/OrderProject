package com.Ecommence.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Username is required.")
    private String username;
    @NotBlank(message = "First name is required.")
    private String firstName;
    @NotBlank(message = "Last name is required.")
    private String lastName;
    @Email(message = "Email format is invalid.")
    @NotBlank(message = "Email is required.")
    private String email;
    @NotBlank(message = "Phone number is required.")
    private String phoneNumber;
    @NotBlank(message = "Password is required.")
    @Size(min = 6, message = "Password must have at least 6 characters.")
    private String password;
    @NotNull(message = "Date of birth cannot be null.")
    private LocalDate dateOfBirth;
    private List<AddressDto> addresses;
    private String profileImageUrl;
}
