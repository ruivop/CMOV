package org.feup.cmov.customerapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.feup.cmov.customerapp.TicketResponser;
import org.feup.cmov.customerapp.utils.DatabaseHelper;
import org.feup.cmov.customerapp.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Ticket {

    public static final String serverIp = "192.168.0.101";

    private String id;
    private String date;
    private String title;
    private boolean used;

    private boolean isSelected;

    public Ticket(String id, String date, String title, boolean used) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.used = used;
        isSelected= false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public static void getData(SharedPreferences sharedPreferences, final TicketResponser responser, Context context) {
        final DatabaseHelper mDbHelper = new DatabaseHelper(context);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //final String testRegister = sharedPreferences.getString("Id", null);
        final String testRegister = "123123123"; //TODO: por o sharedPreferences no meu pc a funcioinar
        try {
            String request = "http://" + serverIp + ":3000/tickets";
            HttpUtils.get("tickets", new RequestParams(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {
                        JSONArray jsonArray = new JSONArray(new String(responseBody));

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobj = null;

                            jsonobj = jsonArray.getJSONObject(i);

                            String performanceCustomer = jsonobj.getString("customer");

                            if (testRegister.equals(performanceCustomer)) {
                                ContentValues values = new ContentValues();
                                values.put(DataBaseContract.Ticket._ID, jsonobj.getString("_id"));
                                values.put(DataBaseContract.Ticket.DATE, jsonobj.getString("edate"));
                                values.put(DataBaseContract.Ticket.PERFORMANCE, jsonobj.getString("performance"));
                                values.put(DataBaseContract.Ticket.VALIDATED, jsonobj.getString("validated"));

                                db.insert(DataBaseContract.Ticket.TABLE_NAME, null, values);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayList<Ticket> tickets = new ArrayList<>();

                    String[] projection = {
                            DataBaseContract.Ticket._ID,
                            DataBaseContract.Ticket.DATE,
                            DataBaseContract.Ticket.PERFORMANCE,
                            DataBaseContract.Ticket.VALIDATED
                    };
                    String sortOrder = DataBaseContract.Ticket.DATE + " ASC";

                    Cursor cursor = db.query(
                            DataBaseContract.Ticket.TABLE_NAME,   // The table to query
                            projection,             // The array of columns to return (pass null to get all)
                            null,              // The columns for the WHERE clause
                            null,          // The values for the WHERE clause
                            null,                   // don't group the rows
                            null,                   // don't filter by row groups
                            sortOrder               // The sort order
                    );

                    while(cursor.moveToNext()) {
                        String performanceId = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContract.Ticket._ID));
                        String performanceDate = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContract.Ticket.DATE));
                        String performanceTitle = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContract.Ticket.PERFORMANCE));
                        boolean performanceUsed = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContract.Ticket.VALIDATED)) > 0;
                        tickets.add(new Ticket(performanceId, performanceDate, performanceTitle, performanceUsed));
                    }
                    cursor.close();


                    responser.onResponseReceived(tickets);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    System.out.println("onFailure - statusCode: " + statusCode);
                    responser.onResponseReceived(null);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
