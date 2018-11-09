package com.foxminded.airline.web.controller;

import com.foxminded.airline.domain.entity.Flight;
import com.foxminded.airline.domain.entity.LevelTicket;
import com.foxminded.airline.domain.entity.Sit;
import com.foxminded.airline.domain.entity.Ticket;
import com.foxminded.airline.domain.service.FlightService;
import com.foxminded.airline.domain.service.SitService;
import com.foxminded.airline.domain.service.UserService;
import com.foxminded.airline.dto.FlightPriceDTO;
import com.foxminded.airline.dto.TicketDTO;
import com.foxminded.airline.utils.FlightPriceConverter;
import com.foxminded.airline.utils.TicketConverter;
import com.foxminded.airline.web.dao.FlightPriceRepository;
import com.foxminded.airline.web.dao.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@Transactional
public class TicketController {
    Flight flight;

    @Autowired
    FlightService flightService;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    FlightPriceRepository flightPriceRepository;

    @Autowired
    TicketConverter ticketConverter;

    @Autowired
    FlightPriceConverter flightPriceConverter;

    @Autowired
    SitService sitService;

    @Autowired
    UserService userService;

    @GetMapping(value = "/buyticket",
            params = {"number", "dateString", "timeString"})
    @Transactional
    public String showTicket(@RequestParam("number") String number,
                             @RequestParam("dateString") String dateString,
                             @RequestParam("timeString") String timeString) {
        flight = flightService.findFlightByNumberAndDateAndTime(number, LocalDate.parse(dateString), LocalTime.parse(timeString));
        return "buyTicket";
    }

    @GetMapping(value = "/buyticket/flightprices",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FlightPriceDTO>> getFlightPrices() throws IOException {
        return new ResponseEntity<>(flightPriceConverter.createDTOsForFlightPrices(flight.getFlightPrices()), HttpStatus.OK);
    }

    @PostMapping(value = "/buyticket/sits",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sit>> getSits(@RequestBody Sit sit) throws IOException {
        String levelTicket;
        if (sit.getLevelTicket() == null)
            levelTicket = LevelTicket.ECONOM.getLevelTicket();
        else
            levelTicket = sit.getLevelTicket().split(" - ")[0];
        return new ResponseEntity<>(sitService.findAvailableSitsForFlightAndLevelTicket(flight, levelTicket), HttpStatus.OK);
    }

    @PostMapping(value = "/buyticket",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createTicket(@RequestBody TicketDTO ticketDTO) {
        ticketRepository.save(ticketConverter.createTicketFromDTO(ticketDTO, flight));
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping(value = "/user/buyticket",
            params = {"number", "dateString", "timeString"})
    @Transactional
    public String showTicketForUser(@RequestParam("number") String number,
                                    @RequestParam("dateString") String dateString,
                                    @RequestParam("timeString") String timeString) {
        flight = flightService.findFlightByNumberAndDateAndTime(number, LocalDate.parse(dateString), LocalTime.parse(timeString));
        return "user/buyTicket";
    }

    @PostMapping(value = "/user/buyticket",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createTicketForUser(@RequestBody TicketDTO ticketDTO) {
        Ticket ticket = ticketConverter.createTicketFromDTOForUser(ticketDTO, flight, userService.getCurrentUser());
        ticketRepository.save(ticket);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping(value = "/admin/listtickets",
            params = {"number", "dateString", "timeString"})
    public String showPurchasedTickets(@RequestParam("number") String number,
                                       @RequestParam("dateString") String dateString,
                                       @RequestParam("timeString") String timeString) {
        flight = flightService.findFlightByNumberAndDateAndTime(number, LocalDate.parse(dateString), LocalTime.parse(timeString));
        return "admin/listTickets";
    }

    @PostMapping(value = "/admin/listtickets")
    public ResponseEntity<List<TicketDTO>> getListTickets() {
        return new ResponseEntity<>(ticketConverter.createTicketDTOsFromTickets(ticketRepository.findByFlight(flight)), HttpStatus.OK);
    }
}