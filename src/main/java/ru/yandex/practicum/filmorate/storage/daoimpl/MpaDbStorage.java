package ru.yandex.practicum.filmorate.storage.daoimpl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.supportive.MPA;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MPA getRatingMpaById(int ratingId) {
        String sqlQuery = "SELECT * FROM MPA_RATING WHERE RATING_ID  = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, ratingId);
        if (srs.next()) {
            return new MPA(ratingId, srs.getString("rating"));
        }
        return null;
    }

    public List<MPA> getRatingsMpa() {
        List<MPA> ratingsMpa = new ArrayList<>();
        String sqlQuery = "SELECT * FROM MPA_RATING";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery);
        while (srs.next()) {
            ratingsMpa.add(new MPA(srs.getInt("rating_id"), srs.getString("rating")));
        }
        return ratingsMpa;
    }
}