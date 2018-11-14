package org.feup.cmov.customerapp.model;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Ticket {

    private String date;
    private String title;
    private SharedPreferences sharedPreferences;

    public Ticket(String date, String title) {
        this.date = date;
        this.title = title;

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

    public static ArrayList<Ticket> getData(SharedPreferences sharedPreferences){
        ArrayList<Ticket> tickets = new ArrayList<>();

        String testRegister = sharedPreferences.getString("Id",null);

        try {
        String request = "http://hello.localtunnel.me:3000/tickets";

        URL url = null;
        url = new URL( request );

        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());

            JSONArray jsonArray = new JSONArray(response.toString());

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonobj = jsonArray.getJSONObject(i);

                String performanceCustomer = jsonobj.getString("customer");


                String performanceDate = jsonobj.getString("edate");
                String performanceTitle = jsonobj.getString("performance");
                System.out.println(performanceDate + " " + performanceTitle);
                if(testRegister.equals(performanceCustomer)){
                tickets.add(new Ticket(performanceDate,performanceTitle));}
            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tickets;
    }

}
