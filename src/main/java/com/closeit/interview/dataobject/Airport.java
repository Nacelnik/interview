package com.closeit.interview.dataobject;


import org.springframework.data.annotation.Id;


public class Airport
{

    @Id
    public String id;

    public String airportCode;
    public int arrivalsCount;
    public int departureCount;
    public double arrivalsDelay;
    public double departuresDelay;

    public Airport()
    {
        this(null);
    }


    public Airport(String airportCode)
    {
        this(airportCode, 0, 0, 0, 0);
    }


    public Airport(String airportCode, int arrivalsCount, int departureCount, double arrivalsDelay, double departuresDelay)
    {
        this.airportCode = airportCode;
        this.arrivalsCount = arrivalsCount;
        this.departureCount = departureCount;
        this.arrivalsDelay = arrivalsDelay;
        this.departuresDelay = departuresDelay;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "airportCode='" + airportCode + '\'' +
                ", arrivalsCount=" + arrivalsCount +
                ", departureCount=" + departureCount +
                ", arrivalsDelay=" + arrivalsDelay +
                ", departuresDelay=" + departuresDelay +
                '}';
    }
}
