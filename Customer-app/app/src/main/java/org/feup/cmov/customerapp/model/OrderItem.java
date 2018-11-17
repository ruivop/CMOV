package org.feup.cmov.customerapp.model;

public class OrderItem {

    String title;
    int number;
    double price;

    public OrderItem(String title, int number, double price) {
        this.title = title;
        this.number = number;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
