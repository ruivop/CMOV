package org.feup.cmov.customerapp.app;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.model.DataBaseContract;
import org.feup.cmov.customerapp.utils.DatabaseHelper;
import org.feup.cmov.customerapp.utils.HttpUtils;
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
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class TicketActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    int ticketval;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ticket);
        context = this;
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Next Performance");

        Button but_qr = findViewById(R.id.pur_button);

        but_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!HttpUtils.isNetworkAvailable(context)) {
                    Toast.makeText(context, "Must have internet connection", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = getIntent();
                double price = intent.getDoubleExtra("price", 0.0);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle("Confirm Purchase");
                setBarTicketval();
                final View v2 = v;
                int tval = getTicketval();

                alertDialogBuilder.setMessage("Are you sure you want to buy " + tval + " tickets for " + String.valueOf(price * tval) + "$")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                purchaseTicket(getTicketval());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

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

    private void purchaseTicket(int number) {
        Intent intent = getIntent();
        String performanceDate = intent.getStringExtra("date");
        String performanceTitle = intent.getStringExtra("title");
        SharedPreferences sp1 = this.getSharedPreferences("Register", MODE_PRIVATE);
        String idReg = sp1.getString("Id", null);

        try {
            String request = "";
            if (number == 1) {
                request ="tickets";
            } else {
                request = "tickets/" + number;
            }

            RequestParams requestParams = new RequestParams();
            requestParams.put("edate", performanceDate);
            requestParams.put("edate", performanceDate);
            requestParams.put("customer", idReg);
            requestParams.put("performance", performanceTitle);

            HttpUtils.postByUrl(HttpUtils.BASE_URL+ request, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    System.out.println("Body: " + new String(responseBody));

                    final DatabaseHelper mDbHelper = new DatabaseHelper(context);
                    final SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    int i = -1;
                    JSONArray jsonobj = null;
                    try {
                        jsonobj = new JSONArray(new  String(responseBody));
                        JSONArray array = jsonobj.getJSONArray(1);
                        for (i = 0; i < array.length(); i++) {
                            JSONObject voucher = array.getJSONObject(i);

                            ContentValues values = new ContentValues();
                            values.put(DataBaseContract.Voucher._ID, voucher.getString("_id"));
                            values.put(DataBaseContract.Voucher.PRODUCT, voucher.getString("product"));
                            values.put(DataBaseContract.Voucher.CREATED_DATE, voucher.getString("Created_date"));
                            db.insert(DataBaseContract.Voucher.TABLE_NAME, null, values);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(context, "Purchase Made. You gained " + i + " vouchers!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, OwnedTicketsActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    System.out.println("Error on purchasing the tickets " + statusCode + ": " + error.getMessage());

                    Toast.makeText(context, "Error on purchasing the tickets", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, OwnedTicketsActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
