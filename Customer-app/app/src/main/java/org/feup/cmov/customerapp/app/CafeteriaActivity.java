package org.feup.cmov.customerapp.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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
        final Context context = this;

        setContentView(R.layout.activity_cafeteria);

        setupRecyclerView();

        Button but_qr = findViewById(R.id.order_button);


        but_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderQrActivity.class);
                intent.putExtra("text",getText());
                startActivity(intent);
            }
        });


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

    public String getText(){
        String finaltext = "";
        double totalcost = 0;
        for(int i = 0; i<orderItemList.size(); i++){
            finaltext += orderItemList.get(i).getTitle() + ":" + orderItemList.get(i).getNumber() + ";";
            totalcost += (orderItemList.get(i).getPrice() * orderItemList.get(i).getNumber());
        }
        finaltext += "Cost:" + String.valueOf(totalcost);
        return finaltext;
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
