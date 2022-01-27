package com.example.zt_orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfitActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference profitDB;
    private AdapterProfit adapterProfit;
    private ArrayList<HistoryProfit> list;
//    private BankClass bankClass;
//    private int bank_private, plus_private;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);

        recyclerView = findViewById(R.id.historyList);
        profitDB = FirebaseDatabase.getInstance().getReference("ProfitDB");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ProfitActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapterProfit = new AdapterProfit(this,list);
        recyclerView.setAdapter(adapterProfit);
//        bank_private = bankClass.bank;
//        plus_private = 0;

        profitDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    HistoryProfit historyProfit = dataSnapshot.getValue(HistoryProfit.class);
                    list.add(historyProfit);
                }
                adapterProfit.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}