package com.foxminded.airline.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SearchFlightControllerSmokeTest {

    @Autowired
    private RequestMappingHandlerAdapter handleAdapter;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void whenGetListFlightsPage_thenReturnListFlightsPage() throws Exception {
        request.setRequestURI("/searchflight");
        request.setMethod("GET");
        request.setParameter("nameDepartureAirport", "");
        request.setParameter("nameArrivalAirport", "");
        request.setParameter("date", "");

        ModelAndView mav = handleAdapter.handle(request, response, handlerMapping.getHandler(request).getHandler());

        ModelAndViewAssert.assertViewName(mav, "searchFlight");
    }

    @Test
    public void whenGetListFlightsPageForUser_thenReturnListFlightsPageForUser() throws Exception {
        request.setRequestURI("/user/searchflight");
        request.setMethod("GET");
        request.setParameter("nameDepartureAirport", "");
        request.setParameter("nameArrivalAirport", "");
        request.setParameter("date", "");

        ModelAndView mav = handleAdapter.handle(request, response, handlerMapping.getHandler(request).getHandler());

        ModelAndViewAssert.assertViewName(mav, "user/searchFlight");
    }

    @Test
    public void whenGetListFlightsPageForAdmin_thenReturnListFlightsPageForAdmin()throws Exception{
        request.setRequestURI("/admin/listflights");
        request.setMethod("GET");
        request.setParameter("nameAirport", "");
        request.setParameter("date", "");

        ModelAndView mav = handleAdapter.handle(request, response, handlerMapping.getHandler(request).getHandler());

        ModelAndViewAssert.assertViewName(mav, "admin/listFlights");
    }
}