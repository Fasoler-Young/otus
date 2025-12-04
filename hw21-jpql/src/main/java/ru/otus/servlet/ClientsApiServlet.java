package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import ru.otus.dao.EntityDao;
import ru.otus.dto.ClientDto;

@SuppressWarnings({"java:S1989"})
@RequiredArgsConstructor
public class ClientsApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;
    private static final String RAND_URL = "rand";

    private final transient EntityDao<ClientDto> clientDao;
    private final transient Gson gson;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ClientDto client;

        String param = extractParamFromRequest(request);
        if (RAND_URL.equals(param)) {
            client = clientDao.findRandom().orElse(null);
        } else {
            long id = Long.parseLong(param);
            client = clientDao.findById(id).orElse(null);
        }

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(client));
    }

    private String extractParamFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        return (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
    }
}
