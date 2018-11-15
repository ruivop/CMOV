package org.feup.cmov.customerapp.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.adapter.CafeteriaAdapter;
import org.feup.cmov.customerapp.adapter.TicketAdapter;
import org.feup.cmov.customerapp.model.CafeteriaItem;
import org.feup.cmov.customerapp.model.Ticket;

public class CafeteriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cafeteria);

        setupRecyclerView();


    }

    private void setupRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.cafeteria_items);
        CafeteriaAdapter cafeteriaAdapter = new CafeteriaAdapter(this, CafeteriaItem.getData());
        recyclerView.setAdapter(cafeteriaAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
