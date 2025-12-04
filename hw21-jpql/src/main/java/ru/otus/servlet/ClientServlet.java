package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import ru.otus.dao.EntityDao;
import ru.otus.dto.ClientDto;
import ru.otus.services.TemplateProcessor;

@RequiredArgsConstructor
public class ClientServlet extends HttpServlet {
    private static final String USERS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_RANDOM_CLIENT = "randomClient";

    private final transient EntityDao<ClientDto> clientsDao;
    private final transient TemplateProcessor templateProcessor;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        clientsDao.findRandom().ifPresent(randomUser -> paramsMap.put(TEMPLATE_ATTR_RANDOM_CLIENT, randomUser));

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }
}
