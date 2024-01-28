package ru.yandex.practicum.filmorate.storage.daoimpl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.supportive.Genre;
import ru.yandex.practicum.filmorate.model.supportive.MPA;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public void addLike(int userId, int filmId) {
        String sqlQuery = "INSERT INTO likes (user_id, film_id) "
                + "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        String sqlQuery = "DELETE likes "
                + "WHERE FILM_ID = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public List<Film> getTopFilm(int count) {
        String sqlQuery = "SELECT * FROM FILMS "
                + "LEFT JOIN likes ON likes.FILM_ID = FILMS.FILM_ID "
                + "JOIN MPA_RATING ON FILMS.MPA_RATING_ID = MPA_RATING.RATING_ID "
                + "GROUP BY FILMS.FILM_ID "
                + "ORDER BY COUNT(likes.FILM_ID) DESC"
                + "LIMIT "
                + count;
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    private Film makeFilm(ResultSet rs, int id) throws SQLException {
        int filmId = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        int duration = rs.getInt("duration");
        LocalDate releaseDate = rs.getTimestamp("release_date").toLocalDateTime().toLocalDate();
        int mpaId = rs.getInt("rating_id");
        String mpaName = rs.getString("rating");
        MPA mpa = new MPA(mpaId, mpaName);
        Set<Genre> genres = new HashSet<>();
        return Film.builder()
                .id(filmId)
                .name(name)
                .description(description)
                .duration(duration)
                .genres(genres)
                .mpa(mpa)
                .releaseDate(releaseDate)
                .build();
    }

}
