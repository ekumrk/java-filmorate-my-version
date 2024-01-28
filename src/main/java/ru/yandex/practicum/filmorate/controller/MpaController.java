package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.supportive.MPA;
import ru.yandex.practicum.filmorate.service.supportive.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<MPA> getRatingsMpa() {
        log.info("Request for all MPas");
        return mpaService.getRatingsMpa();
    }

    @GetMapping("/{id}")
    public MPA getRatingMpaById(@PathVariable Integer id) {
        log.info("Request to get an MPA with id {}", id);
        return mpaService.getRatingMpaById(id);
    }
}