package org.feup.cmov.customerapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
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

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = layoutInflater.inflate(R.layout.ticket_item, viewGroup,false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        final Ticket ticket = ticketList.get(i);
        myViewHolder.setData(ticket,i);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewHolder.clicked(ticket);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView date;
        TextView isUsed;
        Switch switch_ticket;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.ticketTitle);
            date = itemView.findViewById(R.id.ticketDate);
            isUsed = itemView.findViewById(R.id.is_used);
            switch_ticket = itemView.findViewById(R.id.switch_ticket);
        }

        public void setData(Ticket ticket, int i){
            title.setText(ticket.getTitle());
            date.setText(ticket.getDate());
            isUsed.setText(ticket.isUsed() ? "Used" : "Unused");
            if(!ticket.isUsed()) {
                switch_ticket.setVisibility(View.VISIBLE);
                switch_ticket.setChecked(ticket.isSelected());
            }else {
                switch_ticket.setVisibility(View.INVISIBLE);
            }
        }

        public void clicked(Ticket ticket){
            if(!ticket.isUsed()) {
                ticket.setSelected(!ticket.isSelected());
                switch_ticket.setChecked(ticket.isSelected());
            }
        }
    }
}
