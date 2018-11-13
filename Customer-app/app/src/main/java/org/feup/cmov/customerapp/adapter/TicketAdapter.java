package org.feup.cmov.customerapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.model.Ticket;

import java.util.List;

public class TicketAdapter  extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    List<Ticket> ticketList;
    LayoutInflater layoutInflater;

    public TicketAdapter(Context context, List<Ticket> ticketList) {
        this.ticketList = ticketList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = layoutInflater.inflate(R.layout.ticket_item,viewGroup,false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Ticket ticket = ticketList.get(i);
        myViewHolder.setData(ticket,i);
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.ticketTitle);
            date = itemView.findViewById(R.id.ticketDate);

        }

        public void setData(Ticket ticket, int i){
            title.setText(ticket.getTitle());
            date.setText(ticket.getDate());
        }
    }
}
