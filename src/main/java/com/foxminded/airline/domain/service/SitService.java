package com.foxminded.airline.domain.service;

import com.foxminded.airline.domain.entity.Flight;
import com.foxminded.airline.domain.entity.Sit;

import java.util.List;

public interface SitService {
    List<Sit> findAvailableSitsForFlightAndLevelTicket(Flight flight, String levelTicket);

    String getLevelTicketFromSitOrDefault(Sit sit);
}
