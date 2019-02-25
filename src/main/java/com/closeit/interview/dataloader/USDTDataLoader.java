package com.closeit.interview.dataloader;

import com.closeit.interview.dataobject.Airport;
import com.closeit.interview.dataobject.YearLog;
import com.closeit.interview.repository.AirportRepository;
import com.closeit.interview.repository.YearLogRepository;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * USDT stands for US Department of Transportation
 */

@Component
public class USDTDataLoader {

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private YearLogRepository yearLogRepository;

    @Autowired
    private AirlineDataFileProcessor dataFileProcessor;

    @Value("${application.temp}")
    private String tempFolder;

    @Value("${application.data.source}")
    private String url;

    @Value("${application.data.year}")
    private String year;

    private static final String FILE_SUFFIX = ".csv.bz2";

    public void prepareData() throws IOException
    {

        YearLog yearLog = yearLogRepository.findByYear(year);

        if (yearLog == null)
        {

            getAndExtractFile();
            processFile();
            cleanUpTempFiles();
            logSuccessfullDownload();
        }
        else
        {
            debug("Data already found in DB, exiting set up process: ", yearLog.info);
        }
    }



    private void cleanUpTempFiles()
    {
        String zippedOutputFileName = getZippedOutputFileName();
        if (new File(zippedOutputFileName).delete())
            debug("Deleted downloaded file: ", zippedOutputFileName);


        String unzippedOutputFileName = getUnzippedOutputFileName();
        if (new File(unzippedOutputFileName).delete())
            debug("Deleted extracted file: ", unzippedOutputFileName);

    }

    private void processFile() throws IOException
    {
        airportRepository.deleteAll();

        dataFileProcessor.setInputFileName(getUnzippedOutputFileName());

        Collection<Airport> airports = dataFileProcessor.processFile();

        airportRepository.saveAll(airports);
    }


    private void getAndExtractFile() throws IOException
    {
        debug("Start download", getSourceUrl());
        downloadFile();
        debug("Start extracting", getZippedOutputFileName());
        extractFile();
        debug("File prepared", getUnzippedOutputFileName());
    }

    private void extractFile() throws IOException
    {
        ReadableByteChannel inputChannel = Channels.newChannel(new BZip2CompressorInputStream(Files.newInputStream(new File(getZippedOutputFileName()).toPath())));

        FileOutputStream fileOutputStream = new FileOutputStream(getUnzippedOutputFileName());

        fileOutputStream.getChannel().transferFrom(inputChannel, 0, Long.MAX_VALUE);
    }

    private void downloadFile() throws IOException
    {
        URL source = new URL(getSourceUrl());

        ReadableByteChannel readableByteChannel = Channels.newChannel(source.openStream());

        FileOutputStream fileOutputStream = new FileOutputStream(getZippedOutputFileName());

        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
    }

    private void logSuccessfullDownload() {
        YearLog yearLog = new YearLog(year, LocalDate.now(), "Successfully downloaded from " + getSourceUrl());
    }

    private String getUnzippedOutputFileName()
    {
        return tempFolder + "/" + year + ".csv";
    }

    private String getZippedOutputFileName() {
        return tempFolder + "/" + year + FILE_SUFFIX;
    }


    private String getSourceUrl()
    {
        return url + year + FILE_SUFFIX;
    }

    private void debug(String message, String additionalInfo)
    {
        System.out.println(message + ":" + additionalInfo + " at " + LocalDateTime.now());
    }
}
