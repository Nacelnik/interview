package com.closeit.interview.dataloader.helper;

import org.junit.Assert;
import org.junit.Test;

public class HeaderInfoTest
{

    @Test
    public void testFindIndicesInHeader()
    {
        String[] line = {HeaderInfo.ARRIVAL_DELAY,
                "foo", "bar",
                HeaderInfo.CANCELLED, HeaderInfo.DEPARTURE_DELAY,
                "Ondrej Pribula",
                HeaderInfo.DESTINATION,
                "$^@&!(**$$@@!!<<<,,,@@",
                HeaderInfo.ORIGIN};
        HeaderInfo expected = new HeaderInfo(8, 6, 0, 4, 3);


        HeaderInfo info = HeaderInfo.fromLine(line);

        Assert.assertEquals("The line should be correctly parsed", expected, info);

        Assert.assertEquals("The line should correctly return its expected minimal length", line.length, info.getMinimumLineLength());
    }

    @Test
    public void testFindIndicesInIncorrectHeader()
    {
        String[] incorrectHeader = {"This", "header", "is", "almost", "completely", "incorrect", HeaderInfo.DEPARTURE_DELAY};
        try
        {
            HeaderInfo.fromLine(incorrectHeader);
            Assert.fail("An exception should have been thrown");
        }
        catch (Throwable t)
        {
            Assert.assertEquals("The exception should have the correct type", t.getClass(), IllegalArgumentException.class);
            Assert.assertEquals("The exception should have the correct message", HeaderInfo.MALFORMED_HEADER_LINE_MESSAGE, t.getMessage());
        }
    }
}
