package ru.otus.dto;

import java.util.List;
import ru.otus.crm.model.Client;

public record ClientDto(Long id, String name, AddressDto address, List<PhoneDto> phones) {
    public static ClientDto fromEntity(Client entity) {
        AddressDto address = entity.getAddress() == null ? null : AddressDto.fromEntity(entity.getAddress());
        List<PhoneDto> phones = entity.getPhones() == null
                ? null
                : entity.getPhones().stream().map(PhoneDto::fromEntity).toList();
        return new ClientDto(entity.getId(), entity.getName(), address, phones);
    }
}
