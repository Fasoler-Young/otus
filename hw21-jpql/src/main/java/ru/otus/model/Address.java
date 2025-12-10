package ru.otus.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "address")
public class Address {
    @Id
    public Long id;

    @Column(value = "street")
    public String street;
}
