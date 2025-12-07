package ru.otus.repostory;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import ru.otus.model.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {

    Optional<Client> findById(long id);

    @NonNull
    List<Client> findAll();
}
