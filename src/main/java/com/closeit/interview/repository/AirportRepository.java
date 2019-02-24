package com.closeit.interview.repository;

import com.closeit.interview.dataobject.Airport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AirportRepository extends MongoRepository<Airport, String> {

    Airport findByAirportCode(String airportCode);
}
