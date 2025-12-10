package ru.otus.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.model.Client;
import ru.otus.repostory.ClientRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbServiceClientImpl implements DBServiceClient {

    private final ClientRepository clientRepository;

    @Override
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Optional<Client> getClient(long id) {
        return clientRepository.findById(id);
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}
