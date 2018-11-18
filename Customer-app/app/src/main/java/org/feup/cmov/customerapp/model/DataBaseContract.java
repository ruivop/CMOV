package org.feup.cmov.customerapp.model;

import android.provider.BaseColumns;

public final class DataBaseContract {
    private DataBaseContract(){}

    public static class Order implements BaseColumns {
        public static final String TABLE_NAME = "cafeteria_order";
        public static final String PRICE = "price";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Order.TABLE_NAME + " (" +
                        Order._ID + " INTEGER PRIMARY KEY," +
                        Order.PRICE + " DOUBLE);";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Order.TABLE_NAME;
    }

    public static class Product implements BaseColumns {
        public static final String TABLE_NAME = "product";
        public static final String ORDER = "order_id";
        public static final String PRODUCT_NAME = "body";
        public static final String QUANTITY = "quantity";


        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Product.TABLE_NAME + " (" +
                        Product._ID + " INTEGER PRIMARY KEY," +
                        Product.ORDER + " INTEGER," +
                        Product.PRODUCT_NAME + " TEXT," +
                        Product.QUANTITY + " INTEGER," +
                        "FOREIGN KEY (" + Product.ORDER + ") REFERENCES " + Order.TABLE_NAME + "(" + Order._ID +"));";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Product.TABLE_NAME;
    }

    public static class Ticket implements BaseColumns {
        public static final String TABLE_NAME = "ticket";
        public static final String PERFORMANCE = "performance";
        public static final String DATE = "edate";
        public static final String VALIDATED = "validated";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Ticket.TABLE_NAME + " (" +
                        Ticket._ID + " TEXT PRIMARY KEY," +
                        Ticket.PERFORMANCE + " TEXT," +
                        Ticket.DATE + " TEXT," +
                        Ticket.VALIDATED + " COLUMN_NAME_VALIDATED);";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Ticket.TABLE_NAME;
    }

}