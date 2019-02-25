package com.closeit.interview.dataloader;

import com.closeit.interview.dataobject.Airport;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
class AirlineDataFileProcessor {

    private static final String ORIGIN = "Origin";
    private static final String DESTINATION = "Dest";
    private static final String ARRIVAL_DELAY = "ArrDelay";
    private static final String DEPARTURE_DELAY = "DepDelay";
    private static final String CANCELLED = "Cancelled";
    private static final String CANCELLED_INDICATOR = "1"; //1 -> cancelled, 0-> not

    private String inputFileName;
    
    private int originIndex, destinationIndex, arrivalDelayIndex, departureDelayIndex, cancelledIndex;


    void setInputFileName(String inputFileName)
    {
        this.inputFileName = inputFileName;
    }

    Collection<Airport> processFile() throws IOException
    {

        CSVReader reader = new CSVReader(new BufferedReader(new FileReader(inputFileName)));

        String[] header = reader.readNext();

        findIndicesInHeader(header);


        int readLines = 0;
        Map<String, Airport> airports = new HashMap<>();

        String[] line;
        while ((line = reader.readNext()) != null) {

            readLines++;

            if (CANCELLED_INDICATOR.equals(line[cancelledIndex]))
                continue;

            String originAirport = line[originIndex];
            String destinationAirport = line[destinationIndex];

            Airport origin = airports.getOrDefault(originAirport, new Airport(originAirport));
            Airport destination = airports.getOrDefault(destinationAirport, new Airport(destinationAirport));

            // The file contains NA on some places
            double departureDelay = isStringNotAvailable(line[departureDelayIndex]) ? 0.0 : Double.valueOf(line[departureDelayIndex]);
            double arrivalDelay = isStringNotAvailable(line[arrivalDelayIndex]) ? 0.0 : Double.valueOf(line[arrivalDelayIndex]);

            airports.put(originAirport, airportWithDepartureDelay(originAirport, origin, departureDelay));
            airports.put(destinationAirport, airportWithArrivalDelay(destinationAirport, destination, arrivalDelay));

            if (readLines % 10000 == 0)
                System.out.println("Read and processed " + readLines + "lines, currently known " + airports.entrySet().size() + " airports");
        }

        reader.close();

        return airports.values();
    }

    private Airport airportWithArrivalDelay(String destinationAirport, Airport destination, double arrivalDelay) {
        return new Airport(destinationAirport, destination.arrivalsCount + 1, destination.departureCount, destination.arrivalsDelay + arrivalDelay, destination.departuresDelay);
    }

    private Airport airportWithDepartureDelay(String originAirport, Airport origin, double departureDelay) {
        return new Airport(originAirport, origin.arrivalsCount, origin.departureCount + 1, origin.arrivalsDelay, origin.departuresDelay + departureDelay);
    }

    private boolean isStringNotAvailable(String data)
    {
        return "NA".equals(data);
    }

    private void findIndicesInHeader(String[] header)
    {
        List<String> searchableHeader = Arrays.asList(header);

        originIndex = searchableHeader.indexOf(ORIGIN);
        destinationIndex = searchableHeader.indexOf(DESTINATION);
        arrivalDelayIndex = searchableHeader.indexOf(ARRIVAL_DELAY);
        departureDelayIndex = searchableHeader.indexOf(DEPARTURE_DELAY);
        cancelledIndex = searchableHeader.indexOf(CANCELLED);
       }
}
