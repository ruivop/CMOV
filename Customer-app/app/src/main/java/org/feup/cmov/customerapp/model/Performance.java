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
    private double  price;

    public Performance(String title, int image, String description, String date, double price) {
        this.title = title;
        this.image = image;
        this.description = description;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        this.price = price;

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

    public double getPrice() {
        return price;
    }

    public void setDescription(double price) {
        this.price = price;
    }



    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");
        return sdf.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static ArrayList<Performance> getData(){
        ArrayList<Performance> performances = new ArrayList<>();
        performances.add(new Performance("Classics of Praga",
                R.drawable.concertimage1,
                "this is the best concert ever made in history and you sould not missit",
                "2018-11-12",12.30));
        performances.add(new Performance("Jazz of Matosinhos",
                R.drawable.concertimage2,
                "this is the best concert ever made in history and you sould not missit no you dont",
                "2018-11-13",15.30));
        performances.add(new Performance("Sinfonia de residência",
                R.drawable.concertimage3,
                "This really isn't the best concert ever made in history and you shouldn't really go, it's terrible.",
                "2018-11-14", 5.25));

        performances.add(new Performance("Música para o Natal",
                R.drawable.concertimage4,
                "this is the best concert ever",
                "2018-12-03",34.20));
        performances.add(new Performance("Visit to Oceanáio of Lisbon",
                R.drawable.concertimage5,
                "this is the best",
                "2018-12-11",2.00));
        performances.add(new Performance("Orquestra barroca",
                R.drawable.concertimage6,
                "this is the best concert ever  Concert end",
                "2018-12-18",2.00));
        performances.add(new Performance("Queima 2019",
                R.drawable.concertimage7,
                "this is the best concert ever Conce yes",
                "2018-12-20",2.00));
        performances.add(new Performance("Coro infantil",
                R.drawable.concertimage8,
                "this is the best concert ever Concert end",
                "2018-12-24",2.00));
        performances.add(new Performance("Arraial de Engenharia 2019",
                R.drawable.concertimage9,
                "this is the best concert ever",
                "2018-12-26",2.00));
        performances.add(new Performance("Concert of Jorge palma",
                R.drawable.concertimage10,
                "this is the best concert ever",
                "2018-12-28",2.00));

        System.out.println("performance.getDate:" + performances.get(0).getDate());
        return performances;
    }
}
