package org.feup.cmov.customerapp.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.TicketResponser;
import org.feup.cmov.customerapp.adapter.PerformanceAdapter;
import org.feup.cmov.customerapp.adapter.TicketAdapter;
import org.feup.cmov.customerapp.model.Performance;
import org.feup.cmov.customerapp.model.Ticket;

import java.util.ArrayList;
import java.util.List;

public class OwnedTicketsActivity extends AppCompatActivity implements TicketResponser {
    private TicketAdapter ticketAdapter;
    Button validateBtn;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_tickets);

        setupRecyclerView();
        context = this;
        validateBtn = findViewById(R.id.validate_tickets_btn);

        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Ticket> tickets = ticketAdapter.getTicketList();
                int numSelected = 0;
                //String id = getSharedPreferences("Register", MODE_PRIVATE).getString("Id", null);;
                String id = "123123123";
                String message = id + "-";
                for(Ticket ticket : tickets) {
                    if(ticket.isSelected()) {
                        numSelected++;
                        message += ticket.getId() + ":" + ticket.getDate() + ",";
                    }
                }
                if(numSelected == 0) {
                    message = "Must select at least one ticket";
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }else if(numSelected > 4) {
                    message = "Max 4 tickets at a time exceeded";
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(context, OrderQrActivity.class);
                    intent.putExtra("text", message);
                    startActivity(intent);
                }
            }
        });
    }


    private void setupRecyclerView() {
        Ticket.getData(this.getSharedPreferences("Register", MODE_PRIVATE), this, this);

    }

    @Override
    public void onResponseReceived(ArrayList<Ticket> tickets) {

        RecyclerView recyclerView = findViewById(R.id.owned_tickets);
        ticketAdapter = new TicketAdapter(this, tickets);
        recyclerView.setAdapter(ticketAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
