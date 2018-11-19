package org.feup.cmov.customerapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CafeteriaItem {

    private String title;
    private int image;
    private double  price;
    private int quantity;

    public CafeteriaItem(String title, int image, double price, int quantity) {
        this.title = title;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public static ArrayList<CafeteriaItem> getData(Context context){


        ArrayList<CafeteriaItem> cafItems = new ArrayList<CafeteriaItem>();
        cafItems.add(new CafeteriaItem("Coffee", R.drawable.coffeeimage, 0.5, 1000));
        cafItems.add(new CafeteriaItem("Soup", R.drawable.soupimage, 1.5, 1000));
        cafItems.add(new CafeteriaItem("Burger", R.drawable.burguerimage, 3.5, 1000));
        cafItems.add(new CafeteriaItem("Soda", R.drawable.sodaimage, 1.0, 1000));
        cafItems.add(new CafeteriaItem("Popcorn", R.drawable.popcornimage, 2.5, 1000));


        List<Vouchers> vouchers = getVouchers(context);
        ArrayList<CafeteriaItem> vouchersItems = new ArrayList<CafeteriaItem>();

        for(Vouchers v: vouchers) {
            String capitalized = v.prduct.substring(0, 1).toUpperCase() + v.prduct.substring(1);

            //increase thevalue if it it already there
            boolean invouchers = false;
            for (CafeteriaItem cafeteriaItem : vouchersItems) {

                if (cafeteriaItem.title.contains(capitalized)) {
                    cafeteriaItem.setQuantity(cafeteriaItem.getQuantity() + 1);
                    invouchers = true;
                    break;
                }
            }

            //add it, if not
            if(!invouchers) {
                for(CafeteriaItem cafeteriaItem: cafItems) {
                    if(cafeteriaItem.title.compareTo(capitalized) == 0) {
                        vouchersItems.add(new CafeteriaItem(capitalized + " Voucher",cafeteriaItem.image, 0, 1));
                        break;
                    }
                }
            }
        }
        ArrayList<CafeteriaItem> newList = new ArrayList<CafeteriaItem>(cafItems);
        newList.addAll(vouchersItems);
        return newList;
    }

    public static List<Vouchers> getVouchers(Context context) {
        ArrayList<Vouchers> vouchers = new ArrayList<Vouchers>();

        final DatabaseHelper mDbHelper = new DatabaseHelper(context);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String[] projection = {
                DataBaseContract.Voucher._ID,
                DataBaseContract.Voucher.PRODUCT,
                DataBaseContract.Voucher.CREATED_DATE
        };
        String sortOrder = DataBaseContract.Voucher.PRODUCT + " ASC";

        Cursor cursor = db.query(
                DataBaseContract.Voucher.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while(cursor.moveToNext()) {
            String voucherId = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContract.Voucher._ID));
            String voucherProduct = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContract.Voucher.PRODUCT));
            String voucherCreatedDate = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContract.Voucher.CREATED_DATE));
            vouchers.add(new Vouchers(voucherId, voucherProduct, voucherCreatedDate));
        }
        cursor.close();
        return vouchers;
    }

    public static void deleteVouchers(Context context, List<Vouchers> vouchers, int num) {

        final DatabaseHelper mDbHelper = new DatabaseHelper(context);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int deletedRows = 0;
        String selection = DataBaseContract.Voucher._ID + " LIKE ?";
        for(int i = 0; i < num; i++) {
            String[] selec = { vouchers.get(i).getId() };
            deletedRows += db.delete(DataBaseContract.Voucher.TABLE_NAME, selection, selec);
        }
    }

    public static void addOrder(Context context, String content, double price, String creationDate) {

        final DatabaseHelper mDbHelper = new DatabaseHelper(context);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DataBaseContract.Order.CONTENTS, content);
        values.put(DataBaseContract.Order.CREATED_DATE, creationDate);
        values.put(DataBaseContract.Order.PRICE, price);

        long newRowId = db.insert(DataBaseContract.Order.TABLE_NAME, null, values);
    }
}
