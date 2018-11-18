package org.feup.cmov.customerapp.model;

import android.content.SharedPreferences;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.feup.cmov.customerapp.TicketResponser;
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

    public static void getData(SharedPreferences sharedPreferences, final TicketResponser responser) {

        //final String testRegister = sharedPreferences.getString("Id", null);
        final String testRegister = "123123123"; //TODO: por o sharedPreferences no meu pc a funcioinar
        try {
            String request = "http://" + serverIp + ":3000/tickets";
            HttpUtils.get("tickets", new RequestParams(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    ArrayList<Ticket> tickets = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(new String(responseBody));

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobj = null;

                            jsonobj = jsonArray.getJSONObject(i);


                            String performanceCustomer = jsonobj.getString("customer");
                            String performanceDate = jsonobj.getString("edate");
                            String performanceTitle = jsonobj.getString("performance");
                            String performanceId = jsonobj.getString("_id");
                            boolean performanceUsed = jsonobj.getBoolean("validated");

                            if (testRegister.equals(performanceCustomer)) {
                                tickets.add(new Ticket(performanceId, performanceDate, performanceTitle, performanceUsed));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
