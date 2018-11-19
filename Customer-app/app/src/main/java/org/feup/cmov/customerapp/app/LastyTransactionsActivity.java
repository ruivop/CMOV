package org.feup.cmov.customerapp.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.TicketResponser;
import org.feup.cmov.customerapp.adapter.LastTransactionsAdapter;
import org.feup.cmov.customerapp.adapter.TicketAdapter;
import org.feup.cmov.customerapp.model.LastTransactions;
import org.feup.cmov.customerapp.model.Ticket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LastyTransactionsActivity extends AppCompatActivity implements TicketResponser {

    private LastTransactionsAdapter lastTransactionsAdapter;
    ArrayList<Ticket> tickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lasty_transactions);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        Ticket.getData(this.getSharedPreferences("Register", MODE_PRIVATE), this, this);

    }

    @Override
    public void onResponseReceived(ArrayList<Ticket> tickets) {

        RecyclerView recyclerView = findViewById(R.id.last_transactions_list);
        this.tickets = tickets;
        List<LastTransactions> lastTransactions = new ArrayList<LastTransactions>();

        for(Ticket t : tickets) {
            String isUsed = t.isUsed() ? "Used" : "Unused";
            String date = t.getCreated_date().replace("Z", "");
            date = date.replace("T", " ");
            lastTransactions.add(new LastTransactions(LastTransactions.TransctionType.Ticket,
                    isUsed + "\n" + t.getTitle() + " (" + t.getDate() + ")", date));
        }
        lastTransactionsAdapter = new LastTransactionsAdapter(this, lastTransactions);
        recyclerView.setAdapter(lastTransactionsAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
