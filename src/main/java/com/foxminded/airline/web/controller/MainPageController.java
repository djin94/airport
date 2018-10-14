package com.foxminded.airline.web.controller;

import com.foxminded.airline.web.dao.AirportRepository;
import com.foxminded.airline.domain.entity.Airport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainPageController {

    @Autowired
    AirportRepository airportRepository;

    @GetMapping(path = "/")
    public String showMainPage() {
        return "index";
    }

    @PostMapping(path="/searchAirport")
    public ResponseEntity<List<Airport>> searchAirport(@RequestBody Airport airport) {
        return new ResponseEntity<>(airportRepository.findByNameLikeIgnoreCase("%" + airport.getName() + "%"), HttpStatus.OK);
    }
}
