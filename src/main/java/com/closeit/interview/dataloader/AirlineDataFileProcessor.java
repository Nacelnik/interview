package com.closeit.interview.dataloader;

import com.closeit.interview.dataobject.Airport;
import com.closeit.interview.dataloader.helper.HeaderInfo;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
class AirlineDataFileProcessor
{

    private static final String CANCELLED_INDICATOR = "1"; //1 -> cancelled, 0-> not
    static final String TOO_SHORT_LINE_MESSAGE = "The line cannot be processed - it is too short.";

    Collection<Airport> processFile(String inputFileName) throws IOException
    {
        CSVReader reader = getCSVReader(inputFileName);
        String[] header = reader.readNext();

        HeaderInfo headerInfo = HeaderInfo.fromLine(header);

        int readLines = 0;
        Map<String, Airport> airports = new HashMap<>();
        String[] line;

        while ((line = reader.readNext()) != null) {

            readLines++;

            if (CANCELLED_INDICATOR.equals(line[headerInfo.getCancelledIndex()]))
                continue;

            processLine(headerInfo, airports, line);

            if (readLines % 10000 == 0)
                System.out.println("Read and processed " + readLines + "lines, currently known " + airports.entrySet().size() + " airports");
        }

        reader.close();

        return airports.values();
    }

    void processLine(HeaderInfo headerInfo, Map<String, Airport> airports, String[] line)
    {
        if (line.length < headerInfo.getMinimumLineLength())
            throw new IllegalArgumentException(TOO_SHORT_LINE_MESSAGE);

        String originAirport = line[headerInfo.getOriginIndex()];
        String destinationAirport = line[headerInfo.getDestinationIndex()];

        Airport origin = airports.getOrDefault(originAirport, new Airport(originAirport));
        Airport destination = airports.getOrDefault(destinationAirport, new Airport(destinationAirport));

        // The file contains NA on some places
        double departureDelay = isStringNotAvailable(line[headerInfo.getDepartureDelayIndex()]) ? 0.0 : Double.valueOf(line[headerInfo.getDepartureDelayIndex()]);
        double arrivalDelay = isStringNotAvailable(line[headerInfo.getArrivalDelayIndex()]) ? 0.0 : Double.valueOf(line[headerInfo.getArrivalDelayIndex()]);

        airports.put(originAirport, airportWithDepartureDelay(originAirport, origin, departureDelay));
        airports.put(destinationAirport, airportWithArrivalDelay(destinationAirport, destination, arrivalDelay));
    }

    private CSVReader getCSVReader(String inputFileName) throws FileNotFoundException
    {
        return new CSVReader(new BufferedReader(new FileReader(inputFileName)));
    }

    private Airport airportWithArrivalDelay(String destinationAirport, Airport destination, double arrivalDelay)
    {
        return new Airport(destinationAirport, destination.arrivalsCount + 1, destination.departureCount, destination.arrivalsDelay + arrivalDelay, destination.departuresDelay);
    }

    private Airport airportWithDepartureDelay(String originAirport, Airport origin, double departureDelay)
    {
        return new Airport(originAirport, origin.arrivalsCount, origin.departureCount + 1, origin.arrivalsDelay, origin.departuresDelay + departureDelay);
    }

    private boolean isStringNotAvailable(String data)
    {
        return "NA".equals(data);
    }
}
