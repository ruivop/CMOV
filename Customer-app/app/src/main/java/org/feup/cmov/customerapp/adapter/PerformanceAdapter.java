package org.feup.cmov.customerapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.app.PerformancesActivity;
import org.feup.cmov.customerapp.app.RegisterActivity;
import org.feup.cmov.customerapp.app.TicketActivity;
import org.feup.cmov.customerapp.model.Performance;

import java.util.List;

import static android.content.ContentValues.TAG;

public class PerformanceAdapter extends RecyclerView.Adapter<PerformanceAdapter.MyViewHolder> {

    List<Performance> performanceList;
    LayoutInflater layoutInflater;

    public PerformanceAdapter(Context context, List<Performance> performanceList) {
        this.performanceList = performanceList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.performance_item, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Performance performance = performanceList.get(i);
        myViewHolder.setData(performance, i);
    }

    @Override
    public int getItemCount() {
        return performanceList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView image;
        TextView description;
        TextView date;
        TextView price;
        int position;
        Performance current;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.performance_title_item);
            image = itemView.findViewById(R.id.performance_image_item);
            description = itemView.findViewById(R.id.performance_description_item);
            date = itemView.findViewById(R.id.performance_date_item);
            price = itemView.findViewById(R.id.performance_price_item);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context con = v.getContext();
                    Intent intent = new Intent(con, TicketActivity.class);
                    intent.putExtra("price",current.getPrice());
                    intent.putExtra("date",current.getDate());
                    intent.putExtra("title",current.getTitle());
                    con.startActivity(intent);


                }
            });
        }

        public void setData(Performance performance, int i) {
            title.setText(performance.getTitle());
            description.setText(performance.getDescription());
            date.setText(performance.getDate());
            image.setImageResource(performance.getImage());
            current = performance;
            price.setText(String.valueOf(performance.getPrice()) + "$");
        }
    }
}
