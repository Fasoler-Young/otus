package ru.otus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {


    @GetMapping({"/", "/client/list"})
    public String clientsListView() {
        return "clients";
    }
}
