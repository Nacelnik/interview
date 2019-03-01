package com.closeit.interview.dataloader;

import com.closeit.interview.dataobject.Airport;
import com.closeit.interview.dataloader.helper.HeaderInfo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class AirlineDataFileProcessorTest
{

    @Test
    public void testProcessCorrectLine()
    {
        AirlineDataFileProcessor processor = new AirlineDataFileProcessor();
        HeaderInfo headerInfo = mockHeaderInfo();

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
        Assert.assertEquals("There should be one arrival to SFO", 1, sfo.arrivalsCount);
        Assert.assertEquals("The arrival delay for SFO should be as expected", 5, sfo.arrivalsDelay, 0.001);

        processor.processLine(headerInfo, result, flightFromSFOtoLAX);

        lax = result.get("LAX");
        Assert.assertNotNull("Data for LAX should be present", lax);
        Assert.assertEquals("There should be one departure for LAX", 1, lax.departureCount);
        Assert.assertEquals("The departure delay for LAX should be as expected", 10.0, lax.departuresDelay, 0.001);
        Assert.assertEquals("There should be one arrival to LAX", 1, lax.arrivalsCount);
        Assert.assertEquals("The arrival delay for LAX should be as expected", 25378, lax.arrivalsDelay, 0.001);


        sfo = result.get("SFO");
        Assert.assertNotNull("Data for SFO should be present", sfo);
        Assert.assertEquals("There should be 1 departure from SFO", 1, sfo.departureCount);
        Assert.assertEquals("The departure delay for SFO should be as expected", 10, sfo.departuresDelay, 0.001);
        Assert.assertEquals("There should be one arrival to SFO", 1, sfo.arrivalsCount);
        Assert.assertEquals("The arrival delay for SFO should be as expected", 5, sfo.arrivalsDelay, 0.001);
    }

    @Test
    public void testProcessIncorrectLine()
    {
        AirlineDataFileProcessor processor = new AirlineDataFileProcessor();
        HeaderInfo headerInfo = mockHeaderInfo();

        String[] tooShortLine = {"SFO", "LAX"};

        try
        {
            processor.processLine(headerInfo, new HashMap<>(), tooShortLine);
            Assert.fail("An exception should have been thrown");
        }
        catch (Throwable t)
        {
            Assert.assertEquals("The exception should have the correct type", t.getClass(), IllegalArgumentException.class);
            Assert.assertEquals("The exception should have the correct message", AirlineDataFileProcessor.TOO_SHORT_LINE_MESSAGE, t.getMessage());
        }
    }

    private HeaderInfo mockHeaderInfo()
    {
        HeaderInfo info = Mockito.mock(HeaderInfo.class);
        Mockito.when(info.getOriginIndex()).then(invocationOnMock -> 0);
        Mockito.when(info.getDestinationIndex()).then(invocationOnMock -> 1);
        Mockito.when(info.getArrivalDelayIndex()).then(invocationOnMock -> 2);
        Mockito.when(info.getDepartureDelayIndex()).then(invocationOnMock -> 3);
        Mockito.when(info.getCancelledIndex()).then(invocationOnMock -> 4);
        Mockito.when(info.getMinimumLineLength()).then(invocationOnMock -> 5);
        return info;
    }
}