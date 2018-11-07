package com.foxminded.airline.domain.service;

import com.foxminded.airline.domain.entity.Airport;
import com.foxminded.airline.domain.entity.Flight;
import com.foxminded.airline.domain.entity.Plane;
import com.foxminded.airline.dto.FlightDTO;
import com.foxminded.airline.web.dao.AirportRepository;
import com.foxminded.airline.web.dao.FlightRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlightServiceTest {
    @Autowired
    private FlightService flightService;

    @MockBean
    private FlightRepository flightRepository;

    @MockBean
    private AirportRepository airportRepository;

    @Mock
    private Flight flight;
    private LocalDate date;
    private LocalTime time;
    private Plane plane;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private String number;
    private FlightDTO flightDTO;

    @Before
    public void setUp() {

        arrivalAirport = new Airport();
        arrivalAirport.setName("London");

        departureAirport = new Airport();
        departureAirport.setName("Berlin");

        plane = new Plane();
        plane.setName("Boeing 737");

        time = LocalTime.of(7, 45);
        date = LocalDate.of(2018, 10, 15);

        number = "7845";

        flight = new Flight();
        flight.setArrivalAirport(arrivalAirport);
        flight.setDepartureAirport(departureAirport);
        flight.setDate(date);
        flight.setTime(time);
        flight.setNumber(number);

        flightDTO = new FlightDTO();
        flightDTO.setNumber(number);
        flightDTO.setArrivalAirport(arrivalAirport.getName());
        flightDTO.setDepartureAirport(departureAirport.getName());
        flightDTO.setDateString("2018-10-15");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void whenFindFlightByNumberAndDateAndTime_thenReturnFlight() {
        when(flightRepository.findByNumberAndDateAndTime(number, date, time)).thenReturn(Optional.of(flight));

        Flight expectedFlight = flight;
        Flight actualFlight = flightService.findFlightByNumberAndDateAndTime(number, date, time);

        assertEquals(expectedFlight, actualFlight);
    }

    @Test
    public void whenFindFlightsByFlightDTO_thenReturnFlights() {
        when(airportRepository.findByNameIgnoreCase(flightDTO.getDepartureAirport())).thenReturn(Optional.of(departureAirport));
        when(airportRepository.findByNameIgnoreCase(flightDTO.getArrivalAirport())).thenReturn(Optional.of(arrivalAirport));
        when(flightRepository.findByDepartureAirportAndArrivalAirportAndDate(departureAirport, arrivalAirport, date)).thenReturn(Arrays.asList(flight));

        List<Flight> actualFlights = flightService.findFlightsByFlightDTO(flightDTO);

        assertThat(actualFlights, hasItems(flight));
    }

    @Test
    public void whenFindFlightsForAirportByDate_thenFindFlightsForAirportByDate() {
        when(airportRepository.findByNameIgnoreCase(flightDTO.getDepartureAirport())).thenReturn(Optional.of(departureAirport));
        when(flightRepository.findByDepartureAirportAndDate(departureAirport, date)).thenReturn(Arrays.asList(flight));

        List<Flight> actualFlights = flightService.findFlightsForAirportByDate(flightDTO);

        assertThat(actualFlights, hasItems(flight));
    }
}