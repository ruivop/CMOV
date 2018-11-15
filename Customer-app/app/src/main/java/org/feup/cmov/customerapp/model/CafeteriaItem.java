package org.feup.cmov.customerapp.model;

import org.feup.cmov.customerapp.R;

import java.util.ArrayList;

public class CafeteriaItem {

    private String title;
    private int image;
    private double  price;

    public CafeteriaItem(String title, int image, double price) {
        this.title = title;
        this.image = image;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static ArrayList<CafeteriaItem> getData(){

        ArrayList<CafeteriaItem> cafItems = new ArrayList<>();

        cafItems.add(new CafeteriaItem("Coffee", R.drawable.coffeeimage, 0.5));
        cafItems.add(new CafeteriaItem("Soup", R.drawable.soupimage, 1.5));
        cafItems.add(new CafeteriaItem("Burger", R.drawable.burguerimage, 3.5));

        return cafItems;


    }
}
