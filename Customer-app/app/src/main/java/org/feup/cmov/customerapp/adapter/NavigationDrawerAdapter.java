package org.feup.cmov.customerapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.model.Drawer;

import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {

    List<Drawer> drawerList;
    LayoutInflater layoutInflater;
    Context context;

    public NavigationDrawerAdapter(Context context, List<Drawer> drawerList) {
        this.context = context;
        this.drawerList = drawerList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NavigationDrawerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.nav_drawer_list_item, viewGroup, false);
        NavigationDrawerAdapter.MyViewHolder holder = new NavigationDrawerAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NavigationDrawerAdapter.MyViewHolder myViewHolder, int i) {

        Drawer drawer = drawerList.get(i);
        myViewHolder.setData(drawer, i);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, myViewHolder.title.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return drawerList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView image;
        int position;
        Drawer current;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.nav_dawer_text);
            image = itemView.findViewById(R.id.nav_dawer_icon);
        }

        public void setData(Drawer drawer, int i) {
            title.setText(drawer.getTitle());
            image.setImageResource(drawer.getImageId());
            current = drawer;
        }
    }
}