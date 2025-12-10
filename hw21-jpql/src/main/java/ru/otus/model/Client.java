package ru.otus.model;

import java.util.Set;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "client")
public class Client {
    @Id
    public Long id;

    @Column(value = "name")
    public String name;

    @Column(value = "addressid")
    @MappedCollection(idColumn = "id")
    public Address address;

    @MappedCollection(idColumn = "clientid")
    public Set<ru.otus.model.Phone> phones;
}
