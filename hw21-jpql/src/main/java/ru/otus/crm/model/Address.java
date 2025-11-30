package ru.otus.crm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "address")
@NoArgsConstructor
public class Address {
    public Address(Long id, String street) {
        this.street = street;
        this.id = id;
    }

    @Id
    @SequenceGenerator(name = "address_gen", sequenceName = "address_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_gen")
    private Long id;

    @Column(name = "street")
    private String street;
}
