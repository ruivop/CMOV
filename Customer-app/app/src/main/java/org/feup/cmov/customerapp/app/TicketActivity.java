package org.feup.cmov.customerapp.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import org.feup.cmov.customerapp.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class TicketActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    int ticketval;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_ticket);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Next Performance");

        Button but_qr = findViewById(R.id.pur_button);

        but_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                double price =intent.getDoubleExtra("price",0.0);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle("Confirm Purchase");
                setBarTicketval();
                final View v2 = v;
                int tval = getTicketval();

                alertDialogBuilder.setMessage("Are you sure you want to buy " + tval + " tickets for " + String.valueOf(price*tval) + "$")
                        .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        purchaseTicket(getTicketval());
                        Context context = v2.getContext();
                        Intent intent = new Intent(context, PerformancesActivity.class);
                        startActivity(intent);
                    }
                })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();





            }
        });


    }

    public int getTicketval() {
        return ticketval;
    }

    private void setBarTicketval() {
        TextView tval = findViewById(R.id.numberText);
        ticketval = Integer.valueOf(String.valueOf(tval.getText()));
    }

    private void purchaseTicket(int number){
        Intent intent = getIntent();
        String performanceDate = intent.getStringExtra("date");
        String performanceTitle = intent.getStringExtra("title");
        SharedPreferences sp1 = this.getSharedPreferences("Register", MODE_PRIVATE);
        String idReg = sp1.getString("Id",null);

        try {

            String urlParameters  = "edate=" + performanceDate + "&customer=" + idReg + "&performance=" + performanceTitle;
            byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int postDataLength = postData.length;
            String request = "";
            if(number == 1){
            request = "http://cmovrestapi.localtunnel.me:3000/tickets";}
            else{
                request = "http://cmovrestapi.localtunnel.me:3000/tickets/" + number;
            }
            URL url = new URL( request );
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
            conn.setUseCaches(false);
            byte[] outputBytes = urlParameters.getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(outputBytes);
            os.flush();
            os.close();

            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            String ResponseData = convertStreamToString(inputStream);
            System.out.println(ResponseData);
            inputStream.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return;



    }

    public String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
