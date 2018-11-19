package org.feup.cmov.customerapp.model;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.app.CafeteriaActivity;
import org.feup.cmov.customerapp.app.LastyTransactionsActivity;
import org.feup.cmov.customerapp.app.OwnedTicketsActivity;
import org.feup.cmov.customerapp.app.PerformancesActivity;

import java.util.ArrayList;
import java.util.List;

public class Drawer {

    private Class act = null;
    private String title;
    private int imageId;

    public Drawer(String title, int imageId) {
        this.title = title;
        this.imageId = imageId;
    }

    public Drawer(String title, int imageId, Class act){
        this.title = title;
        this.imageId = imageId;
        this.act = act;
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

    public Class getAct() {
        return act;
    }

    public void setAct(Class act) {
        this.act = act;
    }

    public static List<Drawer> getData(){
        List<Drawer> drawers = new ArrayList<>();

        drawers.add(new Drawer("Next Performances", R.drawable.ic_theaters_black_24dp, PerformancesActivity.class));
        drawers.add(new Drawer("Present Tickets", R.drawable.ic_payment_black_24dp, OwnedTicketsActivity.class));
        drawers.add(new Drawer("CafeteriaItem", R.drawable.ic_store_black_24dp, CafeteriaActivity.class));
        drawers.add(new Drawer("Last Transactions", R.drawable.ic_history_black_24dp, LastyTransactionsActivity.class));

        return drawers;
    }
}
