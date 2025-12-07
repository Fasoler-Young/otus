package ru.otus.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import ru.otus.model.Client;
import ru.otus.service.DBServiceClient;

@RestController
public class ClientRestController {

    private final DBServiceClient clientService;

    public ClientRestController(DBServiceClient clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/api/client/{id}")
    public Client getClientById(@PathVariable(name = "id") long id) {
        return clientService.getClient(id).orElse(null);
    }

    @SuppressWarnings("squid:S4488")
    @RequestMapping(method = RequestMethod.GET, value = "/api/client/rand")
    public Client findRandomClient() {
        List<Client> clients = clientService.findAll();
        return clients.get((int) (Math.random() * clients.size()));
    }
}
