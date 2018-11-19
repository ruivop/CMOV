package org.feup.cmov.customerapp.model;

import android.provider.BaseColumns;

public final class DataBaseContract {
    private DataBaseContract(){}

    public static class Order implements BaseColumns {
        public static final String TABLE_NAME = "cafeteria_order";
        public static final String PRICE = "price";
        public static final String CONTENTS = "contents";
        public static final String CREATED_DATE = "created_date";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Order.TABLE_NAME + " (" +
                        Order._ID + " INTEGER PRIMARY KEY," +
                        Order.CONTENTS + " TEXT," +
                        Order.CREATED_DATE + " TEXT," +
                        Order.PRICE + " DOUBLE);";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Order.TABLE_NAME;
    }

    public static class Ticket implements BaseColumns {
        public static final String TABLE_NAME = "ticket";
        public static final String PERFORMANCE = "performance";
        public static final String DATE = "edate";
        public static final String VALIDATED = "validated";
        public static final String CREATED_DATE = "created_date";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Ticket.TABLE_NAME + " (" +
                        Ticket._ID + " TEXT PRIMARY KEY," +
                        Ticket.PERFORMANCE + " TEXT," +
                        Ticket.DATE + " TEXT," +
                        Ticket.CREATED_DATE + " TEXT," +
                        Ticket.VALIDATED + " COLUMN_NAME_VALIDATED);";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Ticket.TABLE_NAME;
    }

    public static class Voucher implements BaseColumns {
        public static final String TABLE_NAME = "voucher";
        public static final String CREATED_DATE = "created_date";
        public static final String PRODUCT = "product";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Voucher.TABLE_NAME + " (" +
                        Voucher._ID + " TEXT PRIMARY KEY," +
                        Voucher.PRODUCT + " TEXT," +
                        Voucher.CREATED_DATE + " TEXT);";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Voucher.TABLE_NAME;
    }

}