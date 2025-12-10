package ru.otus.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "phone")
public class Phone {
    @Id
    public Long id;

    @Column(value = "number")
    public String number;

    @Column(value = "clientid")
    public Long clientid;
}
