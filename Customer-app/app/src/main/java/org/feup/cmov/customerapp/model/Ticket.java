package org.feup.cmov.customerapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.widget.Toast;

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

public class Ticket implements Parcelable {

    public static final String serverIp = "192.168.0.101";

    private String id;
    private String date;
    private String title;
    private boolean used;
    private String created_date;

    private boolean isSelected;

    public Ticket(String id, String date, String title, boolean used, String created_date) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.used = used;
        isSelected = false;
        this.created_date = created_date;
    }

    public Ticket(Parcel in) {
        String[] data = new String[3];

        in.readStringArray(data);

        this.id = data[0];
        this.date = data[1];
        this.title = data[2];
        this.used = new Boolean(data[3]);
        this.created_date = data[4];
        this.isSelected = new Boolean(data[5]);
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
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

    public static void getData(SharedPreferences sharedPreferences, final TicketResponser responser, final Context context) {
        final DatabaseHelper mDbHelper = new DatabaseHelper(context);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        if (!HttpUtils.isNetworkAvailable(context)) {
            sendData(db, responser);
            return;
        }

        final String testRegister = sharedPreferences.getString("Id", null);
        try {
            HttpUtils.get("tickets", new RequestParams(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    db.execSQL(DataBaseContract.Ticket.SQL_DELETE_ENTRIES);
                    db.execSQL(DataBaseContract.Ticket.SQL_CREATE_ENTRIES);
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
                                values.put(DataBaseContract.Ticket.CREATED_DATE, jsonobj.getString("Created_date"));

                                db.insert(DataBaseContract.Ticket.TABLE_NAME, null, values);
                            }
                        }
                        sendData(db, responser);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    public static void sendData(final SQLiteDatabase db, final TicketResponser responser) {
        ArrayList<Ticket> tickets = new ArrayList<>();

        String[] projection = {
                DataBaseContract.Ticket._ID,
                DataBaseContract.Ticket.DATE,
                DataBaseContract.Ticket.PERFORMANCE,
                DataBaseContract.Ticket.VALIDATED,
                DataBaseContract.Ticket.CREATED_DATE
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

        while (cursor.moveToNext()) {
            String performanceId = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContract.Ticket._ID));
            String performanceDate = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContract.Ticket.DATE));
            String performanceTitle = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContract.Ticket.PERFORMANCE));
            String performanceCreatedDate = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContract.Ticket.CREATED_DATE));
            Boolean performanceUsed = Boolean.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContract.Ticket.VALIDATED)));
            tickets.add(new Ticket(performanceId, performanceDate, performanceTitle, performanceUsed, performanceCreatedDate));
        }
        cursor.close();

        responser.onResponseReceived(tickets);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.id,
                this.date,
                this.title,
                String.valueOf(this.used),
                this.created_date,
                String.valueOf(this.isSelected)
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Ticket createFromParcel(Parcel in) {
            return new Ticket(in);
        }

        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };

}
