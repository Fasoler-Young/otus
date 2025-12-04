package ru.otus.dto;

import ru.otus.crm.model.Phone;

public record PhoneDto(Long id, String number) {
    public static PhoneDto fromEntity(Phone phone) {
        return new PhoneDto(phone.getId(), phone.getNumber());
    }
}
