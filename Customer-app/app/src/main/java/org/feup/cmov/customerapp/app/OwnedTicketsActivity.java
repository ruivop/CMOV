package org.feup.cmov.customerapp.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.adapter.PerformanceAdapter;
import org.feup.cmov.customerapp.adapter.TicketAdapter;
import org.feup.cmov.customerapp.model.Performance;
import org.feup.cmov.customerapp.model.Ticket;

public class OwnedTicketsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_tickets);

        setupRecyclerView();

    }


    private void setupRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.owned_tickets);
        TicketAdapter ticketAdapter = new TicketAdapter(this, Ticket.getData());
        recyclerView.setAdapter(ticketAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
