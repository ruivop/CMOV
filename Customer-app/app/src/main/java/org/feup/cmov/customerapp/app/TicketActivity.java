package org.feup.cmov.customerapp.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import org.feup.cmov.customerapp.R;

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
                int tval = getTicketval();

                alertDialogBuilder.setMessage("Are you sure you want to buy " + tval + " for " + String.valueOf(price*tval) + "$")
                        .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        dialog.cancel();
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

    private void purchaseTicket(){



    }


}
