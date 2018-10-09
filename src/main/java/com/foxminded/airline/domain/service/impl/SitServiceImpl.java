package com.foxminded.airline.domain.service.impl;

import com.foxminded.airline.domain.entity.Flight;
import com.foxminded.airline.domain.entity.LevelTicket;
import com.foxminded.airline.domain.entity.Sit;
import com.foxminded.airline.domain.service.SitService;
import com.foxminded.airline.web.dao.SitRepository;
import com.foxminded.airline.web.dao.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SitServiceImpl implements SitService {
    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    SitRepository sitRepository;

    @Override
    public List<Sit> findAvailableSitsForFlightAndLevelTicket(Flight flight, LevelTicket levelTicket) {
        List<Sit> allSits = sitRepository.findByPlaneAndLevelTicket(flight.getPlane(), levelTicket);
        List<Sit> busySits = new ArrayList<>();
        ticketRepository.findByFlight(flight)
                .forEach(ticket -> busySits.add(ticket.getSit()));
        List<Sit> availableSits = allSits.stream()
                .filter(sit -> !busySits.contains(sit))
                .collect(Collectors.toList());
        return availableSits;
    }
}
