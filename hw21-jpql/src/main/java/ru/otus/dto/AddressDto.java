package ru.otus.dto;

import ru.otus.crm.model.Address;

public record AddressDto(Long id, String street) {
    public static AddressDto fromEntity(Address address) {
        return new AddressDto(address.getId(), address.getStreet());
    }
}
