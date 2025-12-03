package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.cachehw.MyCacheLong;
import ru.otus.crm.model.Client;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private final MyCacheLong<Client> clientCache;

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        clientCache = new MyCacheLong<>();
    }

    public DbServiceClientImpl(
            TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate, MyCacheLong<Client> clientCache) {
        this.dataTemplate = dataTemplate;
        this.transactionRunner = transactionRunner;
        this.clientCache = clientCache;
    }

    @Override
    public Client saveClient(Client client) {
        Client savedClient = transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            return client;
        });
        clientCache.put(savedClient.getId(), savedClient);
        return savedClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        return Optional.ofNullable(clientCache.get(id))
                .or(() -> transactionRunner.doInTransaction(connection -> {
                    var clientOptional = dataTemplate.findById(connection, id);
                    log.info("client: {}", clientOptional);
                    return clientOptional;
                }));
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
        });
        clients.forEach(client -> clientCache.put(client.getId(), client));
        return clients;
    }
}
