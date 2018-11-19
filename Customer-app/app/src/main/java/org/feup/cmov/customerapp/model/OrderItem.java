package org.feup.cmov.customerapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderItem implements Parcelable {

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

    protected OrderItem(Parcel in) {
        title = in.readString();
        number = in.readInt();
        price = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(number);
        dest.writeDouble(price);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OrderItem> CREATOR = new Parcelable.Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel in) {
            return new OrderItem(in);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };
}
