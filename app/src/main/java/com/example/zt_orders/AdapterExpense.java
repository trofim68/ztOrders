package com.example.zt_orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterExpense extends RecyclerView.Adapter<AdapterExpense.MyViewHolder> {

    Context context;
    ArrayList<HistoryExpenses> list;

    public AdapterExpense(Context context, ArrayList<HistoryExpenses> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.expense_history,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HistoryExpenses historyExpenses = list.get(position);
        holder.expense_date.setText(historyExpenses.getExpense_date());
        holder.expense_name.setText(historyExpenses.getExpense_name());
        holder.expense_summ.setText(historyExpenses.getExpense_summ());
        holder.expense_fio.setText(historyExpenses.getExpense_fio());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView expense_date, expense_name, expense_summ, expense_fio;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            expense_date = itemView.findViewById(R.id.expense_date);
            expense_name = itemView.findViewById(R.id.expense_name_lay);
            expense_summ = itemView.findViewById(R.id.expense_summ);
            expense_fio = itemView.findViewById(R.id.expense_fio);
        }
    }

}
