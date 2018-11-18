package org.feup.cmov.customerapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.feup.cmov.customerapp.model.DataBaseContract;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "dataBase.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseContract.Order.SQL_DELETE_ENTRIES);
        db.execSQL(DataBaseContract.Product.SQL_DELETE_ENTRIES);
        db.execSQL(DataBaseContract.Ticket.SQL_DELETE_ENTRIES);

        db.execSQL(DataBaseContract.Order.SQL_CREATE_ENTRIES);
        db.execSQL(DataBaseContract.Product.SQL_CREATE_ENTRIES);
        db.execSQL(DataBaseContract.Ticket.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
