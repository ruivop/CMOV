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
import org.feup.cmov.customerapp.model.LastTransactions;
import org.feup.cmov.customerapp.model.Ticket;

import java.util.List;

public class LastTransactionsAdapter  extends RecyclerView.Adapter<LastTransactionsAdapter.MyViewHolder> {

    List<LastTransactions> transactionsList;
    LayoutInflater layoutInflater;

    public LastTransactionsAdapter(Context context, List<LastTransactions> transactionsList) {
        this.transactionsList = transactionsList;

        this.layoutInflater = LayoutInflater.from(context);
    }

    public List<LastTransactions> getTicketList() {
        return transactionsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = layoutInflater.inflate(R.layout.last_transction_item, viewGroup,false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        final LastTransactions transactions = transactionsList.get(i);
        myViewHolder.setData(transactions,i);
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView type;
        TextView date;
        TextView content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.textview_transction_type);
            date = itemView.findViewById(R.id.textview_transction_date);
            content = itemView.findViewById(R.id.textview_transction_content);
        }

        public void setData(LastTransactions lastTransactions, int i){
            type.setText(lastTransactions.getType().name());
            date.setText(lastTransactions.getDate());
            content.setText(lastTransactions.getContent());
        }
    }
}
