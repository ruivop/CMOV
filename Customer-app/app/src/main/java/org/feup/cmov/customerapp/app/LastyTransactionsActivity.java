package org.feup.cmov.customerapp.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.TicketResponser;
import org.feup.cmov.customerapp.adapter.LastTransactionsAdapter;
import org.feup.cmov.customerapp.adapter.TicketAdapter;
import org.feup.cmov.customerapp.model.DataBaseContract;
import org.feup.cmov.customerapp.model.LastTransactions;
import org.feup.cmov.customerapp.model.Ticket;
import org.feup.cmov.customerapp.utils.DatabaseHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LastyTransactionsActivity extends AppCompatActivity implements TicketResponser {

    private LastTransactionsAdapter lastTransactionsAdapter;
    ArrayList<Ticket> tickets;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lasty_transactions);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Last Transactions");

        setupRecyclerView();
        setupDrawer();
    }

    private void setupDrawer() {
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drawer_frag);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerFragment.setupDrawer(R.id.nav_drawer_frag, drawerLayout, toolbar);
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

        final DatabaseHelper mDbHelper = new DatabaseHelper(this);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String[] projection = {
                DataBaseContract.Order._ID,
                DataBaseContract.Order.CONTENTS,
                DataBaseContract.Order.PRICE,
                DataBaseContract.Order.CREATED_DATE
        };
        String sortOrder = DataBaseContract.Order.CREATED_DATE + " ASC";

        Cursor cursor = db.query(
                DataBaseContract.Order.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while(cursor.moveToNext()) {
            String orderId = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContract.Order._ID));
            String orderContents = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContract.Order.CONTENTS));
            double orderPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(DataBaseContract.Order.PRICE));
            String orderCreatedDate = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContract.Order.CREATED_DATE));
            lastTransactions.add(new LastTransactions(LastTransactions.TransctionType.Order,
                    orderContents.replaceFirst(".*?;", "").replace(";", "\n").replace(":", " - ") + "$",
                    orderCreatedDate));
        }
        cursor.close();
        Collections.sort(lastTransactions, new Comparator<LastTransactions>() {
            @Override
            public int compare(LastTransactions lhs, LastTransactions rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return rhs.getDate().compareTo(lhs.getDate());
            }
        });

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
