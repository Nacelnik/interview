package com.closeit.interview.dataobject;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class HeaderInfo
{
    // Kind of hard coded, but we expect one correct format of the data file anyway
    static final String ORIGIN = "Origin";
    static final String DESTINATION = "Dest";
    static final String ARRIVAL_DELAY = "ArrDelay";
    static final String DEPARTURE_DELAY = "DepDelay";
    static final String CANCELLED = "Cancelled";
    public static final String MALFORMED_HEADER_LINE_MESSAGE = "The line doesn't contain all necessary information";

    private final int originIndex, destinationIndex, arrivalDelayIndex, departureDelayIndex, cancelledIndex;

    private Integer minimumLength = null;

    public HeaderInfo(int originIndex, int destinationIndex, int arrivalDelayIndex, int departureDelayIndex, int cancelledIndex)
    {

        if (Stream.of(originIndex, destinationIndex, arrivalDelayIndex, departureDelayIndex, cancelledIndex).anyMatch(x -> x == -1))
            throw new IllegalArgumentException(MALFORMED_HEADER_LINE_MESSAGE);

        this.originIndex = originIndex;
        this.destinationIndex = destinationIndex;
        this.arrivalDelayIndex = arrivalDelayIndex;
        this.departureDelayIndex = departureDelayIndex;
        this.cancelledIndex = cancelledIndex;
    }

    public static HeaderInfo fromLine(String[] line)
    {
        List<String> searchableLine = Arrays.asList(line);

        return new HeaderInfo(searchableLine.indexOf(ORIGIN),
                searchableLine.indexOf(DESTINATION),
                searchableLine.indexOf(ARRIVAL_DELAY),
                searchableLine.indexOf(DEPARTURE_DELAY),
                searchableLine.indexOf(CANCELLED));
    }

    public int getOriginIndex()
    {
        return originIndex;
    }

    public int getDestinationIndex()
    {
        return destinationIndex;
    }

    public int getArrivalDelayIndex()
    {
        return arrivalDelayIndex;
    }

    public int getDepartureDelayIndex()
    {
        return departureDelayIndex;
    }

    public int getCancelledIndex()
    {
        return cancelledIndex;
    }

    public int getMinimumLineLength()
    {
        if (minimumLength == null)
            // The +1 is here because indices start at 0
            minimumLength = Stream.of(originIndex, destinationIndex, arrivalDelayIndex, departureDelayIndex, cancelledIndex).reduce(Integer::max).get() + 1;

        return minimumLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeaderInfo that = (HeaderInfo) o;
        return originIndex == that.originIndex &&
                destinationIndex == that.destinationIndex &&
                arrivalDelayIndex == that.arrivalDelayIndex &&
                departureDelayIndex == that.departureDelayIndex &&
                cancelledIndex == that.cancelledIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(originIndex, destinationIndex, arrivalDelayIndex, departureDelayIndex, cancelledIndex);
    }
}
