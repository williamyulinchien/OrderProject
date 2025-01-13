package com.Ecommence.backend.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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

public class UserResponseDto {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private List<AddressDto> addresses;
    private List<OrderSummaryDto> orders;
    private String profileImageUrl;
}
