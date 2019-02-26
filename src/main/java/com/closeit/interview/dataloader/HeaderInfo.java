package com.closeit.interview.dataloader;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

class HeaderInfo
{
    // Kind of hard coded, but we expect one correct format of the data file anyway
    private static final String ORIGIN = "Origin";
    private static final String DESTINATION = "Dest";
    private static final String ARRIVAL_DELAY = "ArrDelay";
    private static final String DEPARTURE_DELAY = "DepDelay";
    private static final String CANCELLED = "Cancelled";

    private final int originIndex, destinationIndex, arrivalDelayIndex, departureDelayIndex, cancelledIndex;

    private Integer minimumLength = null;

    HeaderInfo(int originIndex, int destinationIndex, int arrivalDelayIndex, int departureDelayIndex, int cancelledIndex)
    {
        this.originIndex = originIndex;
        this.destinationIndex = destinationIndex;
        this.arrivalDelayIndex = arrivalDelayIndex;
        this.departureDelayIndex = departureDelayIndex;
        this.cancelledIndex = cancelledIndex;
    }

    static HeaderInfo fromLine(String[] line)
    {
        List<String> searchableLine = Arrays.asList(line);

        return new HeaderInfo(searchableLine.indexOf(ORIGIN),
                searchableLine.indexOf(DESTINATION),
                searchableLine.indexOf(ARRIVAL_DELAY),
                searchableLine.indexOf(DEPARTURE_DELAY),
                searchableLine.indexOf(CANCELLED));
    }

    int getOriginIndex()
    {
        return originIndex;
    }

    int getDestinationIndex()
    {
        return destinationIndex;
    }

    int getArrivalDelayIndex()
    {
        return arrivalDelayIndex;
    }

    int getDepartureDelayIndex()
    {
        return departureDelayIndex;
    }

    int getCancelledIndex()
    {
        return cancelledIndex;
    }

    int getMinimumLineLength()
    {
        if (minimumLength == null)
            // The +1 is here because indices start at 0
            minimumLength = Stream.of(originIndex, destinationIndex, arrivalDelayIndex, departureDelayIndex, cancelledIndex).reduce(Integer::max).get() + 1;

        return minimumLength;
    }
}
