package com.closeit.interview.dataloader;

import com.closeit.interview.dataobject.Airport;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class AirlineDataFileProcessor {

    private static final String ORIGIN = "Origin";
    private static final String DESTINATION = "Dest";
    private static final String ARRIVAL_DELAY = "ArrDelay";
    private static final String DEPARTURE_DELAY = "DepDelay";
    private static final String CANCELLED = "Cancelled";
    private static final String CANCELLED_INDICATOR = "1"; //1 -> cancelled, 0-> not

    private String inputFileName;
    
    private int originIndex, destinationIndex, arrivalDelayIndex, departureDelayIndex, cancelledIndex;

    AirlineDataFileProcessor(String inputFileName) {
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
            boolean flightWasCancelled = CANCELLED_INDICATOR.equals(line[cancelledIndex]);

            if (flightWasCancelled)
                continue;

            String originAirport = line[originIndex];
            String destinationAirport = line[destinationIndex];

            Airport origin = airports.getOrDefault(originAirport, new Airport(originAirport, 0, 0, 0.0, 0.0));
            Airport destination = airports.getOrDefault(destinationAirport, new Airport(destinationAirport, 0, 0, 0.0, 0.0));

            // The file contains NA on some places
            double departureDelay = isDelayPresent(line[departureDelayIndex]) ? 0.0 : Double.valueOf(line[departureDelayIndex]);
            double arrivalDelay = isDelayPresent(line[arrivalDelayIndex]) ? 0.0 : Double.valueOf(line[arrivalDelayIndex]);

            airports.put(originAirport, new Airport(originAirport, origin.arrivalsCount, origin.departureCount + 1, origin.arrivalsDelay, origin.departuresDelay + departureDelay));
            airports.put(destinationAirport, new Airport(destinationAirport, destination.arrivalsCount + 1, destination.departureCount, destination.arrivalsDelay + arrivalDelay, destination.departuresDelay));

            if (readLines % 10000 == 0)
                System.out.println("Read and processed " + readLines + "lines, currently known " + airports.entrySet().size() + " airports");
        }

        reader.close();

        return airports.values();
    }

    private boolean isDelayPresent(String data)
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
