package com.Ecommence.backend.mapper;

import com.Ecommence.backend.dto.AddressDto;
import com.Ecommence.backend.entity.Address;
import com.Ecommence.backend.entity.User;

public class AddressMapper {

    // Address entity -> AddressDto
    public static AddressDto mapToAddressDto(Address address) {
        if (address == null) {
            return null;
        }
        AddressDto dto = new AddressDto();
        dto.setAddressId((address.getId()));
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountry(address.getCountry());
        return dto;
    }

    // AddressDto -> Address entity
    public static Address mapToAddress(AddressDto addressDto, User user) {
        if (addressDto == null) {
            return null;
        }
        Address address = new Address();
        address.setStreet(addressDto.getStreet());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPostalCode(addressDto.getPostalCode());
        address.setCountry(addressDto.getCountry());
        address.setUser(user); //  Address with user
        return address;
    }
}

