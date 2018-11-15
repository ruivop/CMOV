package org.feup.cmov.customerapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.model.OrderItem;
import org.feup.cmov.customerapp.model.Ticket;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    List<OrderItem> orderList;
    LayoutInflater layoutInflater;

    public OrderAdapter(Context context, List<OrderItem> orderList) {
        this.orderList = orderList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.order_item, viewGroup, false);
        OrderAdapter.MyViewHolder holder = new OrderAdapter.MyViewHolder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        OrderItem order = orderList.get(i);
        myViewHolder.setData(order, i);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView number;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.order_name);
            number = itemView.findViewById(R.id.number_order);
        }

        public void setData(OrderItem order, int i){
            title.setText(order.getTitle());
            number.setText(String.valueOf(order.getNumber()));
        }
    }
}
