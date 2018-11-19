package org.feup.cmov.customerapp.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.adapter.CafeteriaAdapter;
import org.feup.cmov.customerapp.adapter.OrderAdapter;
import org.feup.cmov.customerapp.adapter.TicketAdapter;
import org.feup.cmov.customerapp.model.CafeteriaItem;
import org.feup.cmov.customerapp.model.DataBaseContract;
import org.feup.cmov.customerapp.model.OrderItem;
import org.feup.cmov.customerapp.model.Ticket;
import org.feup.cmov.customerapp.model.Vouchers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CafeteriaActivity extends AppCompatActivity {

    ArrayList<OrderItem> orderItemList = new ArrayList<>();
    List<Vouchers> vouchers;
    OrderAdapter oa;
    Context context;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_cafeteria);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cafeteria");

        if (savedInstanceState != null) {
            orderItemList = savedInstanceState.getParcelableArrayList("list");
        }
        setupRecyclerView();
        vouchers = CafeteriaItem.getVouchers(context);

        Button but_qr = findViewById(R.id.order_button);
        but_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderItemList.size() == 0) {
                    Toast.makeText(context, "Must select at least one product", Toast.LENGTH_SHORT).show();
                    return;
                }
                String order = getText();
                if (getText().equals("Invalid")) {
                    Toast.makeText(context, "You can't have more than 2 vouchers.", Toast.LENGTH_LONG).show();

                } else {
                    Intent intent = new Intent(context, OrderQrActivity.class);
                    intent.putExtra("text", order);
                    startActivity(intent);
                }
            }
        });
        setupDrawer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("list", orderItemList);
    }

    private void setupDrawer() {
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drawer_frag);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerFragment.setupDrawer(R.id.nav_drawer_frag, drawerLayout, toolbar);
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.cafeteria_items);
        RecyclerView recyclerView2 = findViewById(R.id.order_items);
        CafeteriaAdapter cafeteriaAdapter = new CafeteriaAdapter(this, CafeteriaItem.getData(context));
        OrderAdapter orderAdapter = new OrderAdapter(this, orderItemList);
        oa = orderAdapter;
        recyclerView.setAdapter(cafeteriaAdapter);
        recyclerView2.setAdapter(orderAdapter);

        GridLayoutManager grid = new GridLayoutManager(this, 2);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(grid);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
    }

    public String getText() {
        String finaltext = "";
        double totalcost = 0;
        int numVouchers = 0;
        for (int i = 0; i < orderItemList.size(); i++) {
            if (orderItemList.get(i).getTitle().contains("Voucher")) {
                numVouchers = orderItemList.get(i).getNumber();
                if (numVouchers > 2) {
                    return "Invalid";
                }
            }
            finaltext += orderItemList.get(i).getTitle() + ":" + orderItemList.get(i).getNumber() + ";";
            totalcost += (orderItemList.get(i).getPrice() * orderItemList.get(i).getNumber());
        }
        finaltext += "Cost:" + String.valueOf(totalcost);


        SharedPreferences sp1 = this.getSharedPreferences("Register", MODE_PRIVATE);
        String testRegister = sp1.getString("Id", null);
        finaltext = testRegister + ";" + finaltext;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS");
        String dateString = formatter.format(new java.util.Date());

        CafeteriaItem.addOrder(context, finaltext, totalcost, dateString);

        CafeteriaItem.deleteVouchers(context, vouchers, numVouchers);
        return finaltext;
    }

    public void addOrder(CafeteriaItem cafeteriaItem) {

        double cost = cafeteriaItem.getPrice();
        double totalcost = 0;
        Boolean verifier = true;
        for (int i = 0; i < orderItemList.size(); i++) {
            if (orderItemList.get(i).getTitle().equals(cafeteriaItem.getTitle())) {
                if (orderItemList.get(i).getNumber() >= cafeteriaItem.getQuantity()) {
                    Toast.makeText(context, "Max Available", Toast.LENGTH_SHORT).show();
                    return;
                }
                orderItemList.get(i).setNumber(orderItemList.get(i).getNumber() + 1);
                verifier = false;
            }
            totalcost += (orderItemList.get(i).getPrice() * orderItemList.get(i).getNumber());
        }
        if (verifier) {
            orderItemList.add(new OrderItem(cafeteriaItem.getTitle(), 1, cost));
            totalcost += cost;
        }

        TextView text = findViewById(R.id.order_cost);
        text.setText(totalcost + "$");

        oa.notifyDataSetChanged();
    }
}
