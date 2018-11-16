package org.feup.cmov.customerapp.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.adapter.CafeteriaAdapter;
import org.feup.cmov.customerapp.adapter.OrderAdapter;
import org.feup.cmov.customerapp.adapter.TicketAdapter;
import org.feup.cmov.customerapp.model.CafeteriaItem;
import org.feup.cmov.customerapp.model.OrderItem;
import org.feup.cmov.customerapp.model.Ticket;

import java.util.ArrayList;
import java.util.List;

public class CafeteriaActivity extends AppCompatActivity {

    List<OrderItem> orderItemList = new ArrayList<>();
    OrderAdapter oa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cafeteria);

        setupRecyclerView();


    }

    private void setupRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.cafeteria_items);
        RecyclerView recyclerView2 = findViewById(R.id.order_items);
        CafeteriaAdapter cafeteriaAdapter = new CafeteriaAdapter(this, CafeteriaItem.getData());
        OrderAdapter  orderAdapter = new OrderAdapter(this,orderItemList);
        oa = orderAdapter;
        recyclerView.setAdapter(cafeteriaAdapter);
        recyclerView2.setAdapter(orderAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
    }

    public void addOrder(String title, String price){

        double cost = Double.valueOf(price);
        double totalcost = 0;
        Boolean verifier = true;
        for(int i = 0; i< orderItemList.size(); i++){
            if(orderItemList.get(i).getTitle().equals(title)){
                orderItemList.get(i).setNumber(orderItemList.get(i).getNumber() + 1);
                verifier = false;
            }
            totalcost += (orderItemList.get(i).getPrice() * orderItemList.get(i).getNumber());
        }
        if(verifier){
            orderItemList.add(new OrderItem(title, 1, cost));
            totalcost +=  cost;
        }

        TextView text = findViewById(R.id.order_cost);
        text.setText(totalcost + "$");

        oa.notifyDataSetChanged();
    }
}
