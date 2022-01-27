package com.example.zt_orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterProfit extends RecyclerView.Adapter<AdapterProfit.MyViewHolder> {

    Context context;
    ArrayList<HistoryProfit> list;

    public AdapterProfit(Context context, ArrayList<HistoryProfit> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.profit_history,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HistoryProfit historyProfit = list.get(position);
        holder.id.setText(historyProfit.getHistory_id());
        holder.date.setText(historyProfit.getHistory_date());
        holder.product.setText(historyProfit.getHistory_product());
        holder.price.setText(historyProfit.getHistory_price());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date, product, price, id;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.history_id);
            date = itemView.findViewById(R.id.history_date);
            price = itemView.findViewById(R.id.history_price);
            product = itemView.findViewById(R.id.history_product);
        }
    }

}
