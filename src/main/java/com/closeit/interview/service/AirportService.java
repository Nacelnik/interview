package com.closeit.interview.service;

import com.closeit.interview.dataobject.Airport;
import com.closeit.interview.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AirportService {

    @Autowired
    private AirportRepository repository;

    @RequestMapping("/")
    public String home() {
        Airport lax = repository.findByAirportCode("LAX");

        if (lax != null)
            return  "Average delay to LAX : " +  String.valueOf(lax.arrivalsDelay / lax.arrivalsCount) + " minutes";

        return "Application has not been initialized properly yet, data not found";
    }


    @RequestMapping(value = "/delays/airport/{airportCode}", method = RequestMethod.GET)
    public String getData(@PathVariable String airportCode)
    {
        Airport airport = repository.findByAirportCode(airportCode);

        if (airport != null)
            return String.valueOf(airport);

        return "No airport found with the specified code: " + airportCode;
    }
}
