package ru.otus.dao;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.dto.ClientDto;

@RequiredArgsConstructor
public class ClientsDao implements EntityDao<ClientDto> {

    private final DBServiceClient dbServiceClient;

    @Override
    public Optional<ClientDto> findById(long id) {
        return dbServiceClient.getClient(id).map(ClientDto::fromEntity);
    }

    @Override
    public Optional<ClientDto> findRandom() {
        List<Client> clients = dbServiceClient.findAll();
        return Optional.ofNullable(clients)
                .filter(list -> !list.isEmpty())
                .map(list -> list.get((int) (Math.random() * clients.size())))
                .map(ClientDto::fromEntity);
    }
}
