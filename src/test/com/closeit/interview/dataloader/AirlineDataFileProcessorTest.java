package com.closeit.interview.dataloader;

import com.closeit.interview.dataobject.Airport;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AirlineDataFileProcessorTest {

    @Test
    public void testProcessCorrectLine()
    {
        AirlineDataFileProcessor processor = new AirlineDataFileProcessor();
        HeaderInfo headerInfo = new HeaderInfo(0, 1, 2, 3, 4);

        String[] flightFromLAXtoSFO = {"LAX", "SFO", "5", "10", "0"};
        String[] flightFromSFOtoLAX = {"SFO", "LAX", "25378", "10", "0"};

        Map<String, Airport> result = new HashMap<>();

        processor.processLine(headerInfo, result, flightFromLAXtoSFO);

        Assert.assertEquals("The map should contain two airports", 2, result.size());
        Airport lax = result.get("LAX");
        Assert.assertNotNull("Data for LAX should be present", lax);
        Assert.assertEquals("There should be one departure for LAX", 1, lax.departureCount);
        Assert.assertEquals("The departure delay for LAX should be as expected", 10.0, lax.departuresDelay, 0.001);

        Airport sfo = result.get("SFO");
        Assert.assertNotNull("Data for SFO should be present", sfo);
        Assert.assertEquals("There should be no departures from SFO", 0, sfo.departureCount);
        processor.processLine(headerInfo, result, flightFromSFOtoLAX);


    }


}
