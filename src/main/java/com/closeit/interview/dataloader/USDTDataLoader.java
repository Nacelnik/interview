package com.closeit.interview.dataloader;

import com.closeit.interview.dataobject.Airport;
import com.closeit.interview.repository.AirportRepository;
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
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * USDT stands for US Department of Transportation
 */

@Component
public class USDTDataLoader {

    @Autowired
    private AirportRepository repository;

    @Value("${application.temp}")
    private String tempFolder;

    @Value("${application.data.source}")
    private String url;

    @Value("${application.data.year}")
    private String year;

    private static final String FILE_SUFFIX = ".csv.bz2";

    public void prepareData() throws IOException
    {
        getAndExtractFile();
        processFile();
    }

    private void processFile() throws IOException
    {
        repository.deleteAll();

        AirlineDataFileProcessor processor = new AirlineDataFileProcessor(getUnzippedOutputFileName());

        Collection<Airport> airports = processor.processFile();

        repository.saveAll(airports);
    }


    public void getAndExtractFile() throws IOException
    {
        debug("Start download", getSourceUrl());
        downloadFile();
        debug("Start extracting", getZippedOutputFileName());
        extractFile();
        debug("File prepared", getUnzippedOutputFileName());
    }

    private void debug(String message, String file)
    {
        System.out.println(message + ":" + file + " at " + LocalDateTime.now());
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
}
