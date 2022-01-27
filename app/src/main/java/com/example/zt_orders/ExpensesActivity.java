package com.example.zt_orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExpensesActivity extends AppCompatActivity {

    private AdapterExpense adapterExpense;
    private ArrayList<HistoryExpenses> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        RecyclerView recyclerView = findViewById(R.id.historyList);
        DatabaseReference expenseDB = FirebaseDatabase.getInstance().getReference("ExpenseDB");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ExpensesActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapterExpense = new AdapterExpense(this,list);
        recyclerView.setAdapter(adapterExpense);

        expenseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                int countFields = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    countFields = (int) dataSnapshot.getChildrenCount();
                    HistoryExpenses historyExpense = dataSnapshot.getValue(HistoryExpenses.class);
                    list.add(historyExpense);

                }
                adapterExpense.notifyDataSetChanged();
//                Intent intent = new Intent(ExpensesActivity.this, MainActivity.class);
//                intent.putExtra("countFields", countFields);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}