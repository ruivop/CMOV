package org.feup.cmov.customerapp.model;

import org.feup.cmov.customerapp.R;

import java.util.ArrayList;
import java.util.List;

public class Drawer {
    private String title;
    private int imageId;

    public Drawer(String title, int imageId) {
        this.title = title;
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public static List<Drawer> getData(){
        List<Drawer> drawers = new ArrayList<>();

        drawers.add(new Drawer("Next Performances", R.drawable.ic_theaters_black_24dp));
        drawers.add(new Drawer("Present Tickets", R.drawable.ic_payment_black_24dp));
        drawers.add(new Drawer("Cafeteria", R.drawable.ic_store_black_24dp));
        drawers.add(new Drawer("Last Transactions", R.drawable.ic_history_black_24dp));

        return drawers;
    }
}