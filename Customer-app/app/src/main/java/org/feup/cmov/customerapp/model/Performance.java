package org.feup.cmov.customerapp.model;

import org.feup.cmov.customerapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Performance {
    private String title;
    private int image;
    private String description;
    private Date date;

    public Performance(String title, int image, String description, String date) {
        this.title = title;
        this.image = image;
        this.description = description;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");

        try {
            this.date = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        return sdf.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static ArrayList<Performance> getData(){
        ArrayList<Performance> performances = new ArrayList<>();
        performances.add(new Performance("Concert of the year",
                R.drawable.concertimage1,
                "this is the best concert ever made in history and you sould not missit",
                "31-08-2019"));
        performances.add(new Performance("Concert of the year 20156",
                R.drawable.concertimage2,
                "this is the best concert ever made in history and you sould not missit no you dont",
                "01-08-2019"));
        performances.add(new Performance("Concert of the year 20157",
                R.drawable.concertimage3,
                "this is the best concert ever made in history and you sould not missit no you dont",
                "01-08-2019"));

        performances.add(new Performance("Concert",
                R.drawable.concertimage4,
                "this is the best concert ever",
                "01-08-2019"));
        performances.add(new Performance("Concert Concert Concert Concert Concert Concert end",
                R.drawable.concertimage5,
                "this is the best concert ever Concert Concert Concert Concert Concert Concert end Concert Concert Concert Concert Concert Concert end",
                "01-08-2019"));
        performances.add(new Performance("Concert Concert Concert Concert Concert Concert end",
                R.drawable.concertimage6,
                "this is the best concert ever Concert Concert Concert Concert Concert Concert end Concert Concert Concert Concert Concert Concert end",
                "01-08-2019"));
        performances.add(new Performance("Concert Concert Concert Concert Concert Concert end",
                R.drawable.concertimage7,
                "this is the best concert ever Concert Concert Concert Concert Concert Concert end Concert Concert Concert Concert Concert Concert end",
                "17-08-2019"));
        performances.add(new Performance("Concert Concert Concert Concert Concert Concert end",
                R.drawable.concertimage8,
                "this is the best concert ever Concert Concert Concert Concert Concert Concert end Concert Concert Concert Concert Concert Concert end",
                "14-08-2019"));
        performances.add(new Performance("Concert",
                R.drawable.concertimage9,
                "this is the best concert ever",
                "08-08-2019"));
        performances.add(new Performance("Concert",
                R.drawable.concertimage10,
                "this is the best concert ever",
                "03-08-2019"));
        return performances;
    }
}
