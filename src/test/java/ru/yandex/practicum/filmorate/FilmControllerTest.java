/*package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

@SpringBootTest
public class FilmControllerTest {
    private FilmController f;
    private HttpClient client;
    private final static Gson gson = new Gson();
    @BeforeEach
    void init() {
        f = new FilmController();
        client = HttpClient.newHttpClient();
    }

    @Test
    void shouldCreateNewFilmIfAllIsOk() {
        URI uri = URI.create("http://localhost:8080/films/newFilm");
        Film film = new Film("name", "description", LocalDate.of(2000, 01, 01), 120);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film)))
                .uri(uri)
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement = JsonParser.parseString(response.body());
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Assertions.assertEquals(film.toString(), gson.fromJson(jsonObject, Film.class));
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка");
        }
    }

} */
