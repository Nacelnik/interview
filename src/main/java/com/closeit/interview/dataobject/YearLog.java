package com.closeit.interview.dataobject;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class YearLog {


    @Id
    public String id;

    public String year;
    public LocalDate dateSaved;
    public String info;

    public YearLog()
    { }

    public YearLog(String year, LocalDate dateSaved, String info)
    {
        this.year = year;
        this.dateSaved = dateSaved;
        this.info = info;
    }

    @Override
    public String toString()
    {
        return "YearLog{" +
                "id='" + id + '\'' +
                ", year=" + year +
                ", dateSaved=" + dateSaved +
                ", info='" + info + '\'' +
                '}';
    }
}
