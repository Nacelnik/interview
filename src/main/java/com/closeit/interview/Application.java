package com.closeit.interview;

import com.closeit.interview.dataloader.USDTDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class Application
{

   @Autowired
   private USDTDataLoader dataLoader;

   public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

   }

   @PostConstruct
   public void init()
   {
      try
      {
         dataLoader.prepareData();
      }
      catch (IOException ioe)
      {
         throw new RuntimeException("Cannot get data", ioe);
      }
   }

}