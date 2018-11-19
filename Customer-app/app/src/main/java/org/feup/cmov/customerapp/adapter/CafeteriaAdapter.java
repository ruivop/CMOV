package org.feup.cmov.customerapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.app.CafeteriaActivity;
import org.feup.cmov.customerapp.app.TicketActivity;
import org.feup.cmov.customerapp.model.CafeteriaItem;
import org.feup.cmov.customerapp.model.OrderItem;
import org.feup.cmov.customerapp.model.Performance;
import org.feup.cmov.customerapp.model.Ticket;

import java.util.List;

public class CafeteriaAdapter extends RecyclerView.Adapter<CafeteriaAdapter.MyViewHolder> {


    List<CafeteriaItem> cafeteriaList;
    LayoutInflater layoutInflater;

    public CafeteriaAdapter(Context context, List<CafeteriaItem> cafeteriaList) {
        this.cafeteriaList = cafeteriaList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.cafeteria_item, viewGroup, false);
        CafeteriaAdapter.MyViewHolder holder = new CafeteriaAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return cafeteriaList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull CafeteriaAdapter.MyViewHolder myViewHolder, int i) {

        final CafeteriaItem cafeteria = cafeteriaList.get(i);
        myViewHolder.setData(cafeteria, i);


        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CafeteriaActivity caf = (CafeteriaActivity) v.getContext();

                caf.addOrder(cafeteria);
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView price;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cafitemname);
            price = itemView.findViewById(R.id.cafitemprice);
            image = itemView.findViewById(R.id.cafeitemimage);
        }

        public void setData(CafeteriaItem cafeteria, int i){
            title.setText(cafeteria.getTitle());
            price.setText(String.valueOf(cafeteria.getPrice()));
            image.setImageResource(cafeteria.getImage());
        }
    }
}
