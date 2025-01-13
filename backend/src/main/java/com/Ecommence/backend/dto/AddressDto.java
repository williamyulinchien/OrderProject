package com.Ecommence.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private Long addressId;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
