package com.example.zt_orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zt_orders.service.MyFireBaseInstanceService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.text.TextUtils.isEmpty;

public class ArchiveActivity extends AppCompatActivity {

    private DatabaseReference secDataBase, mDataBase, bankDB;
    private TextView id_order, product, material, price, prepay, customer, acc_date, exec_date, worker, comment, summInBank;
    private String bank_public, plus_public, minus_public, userName, userResetSwitch;
    private BankClass bankClass;
    private FirebaseUser fbUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive_layout);
        init();
        getIntentArchive();
        getDataBank();

        switch (fbUser.getEmail()) {
            case  "trofimov@zt.ru":
                userName = "Трофимов Виктор";
                userResetSwitch = "Восстановил";
                break;
            case "zarapin@zt.ru":
                userName = "Зарапин Алексей";
                userResetSwitch = "Восстановил";
                break;
            case "trofimova@zt.ru":
                userName = "Трофимова Анастасия";
                userResetSwitch = "Восстановила";
                break;
            case "zarapina@zt.ru":
                userName = "Зарапина Анастасия";
                userResetSwitch = "Восстановила";
                break;
        }
    }

    private void init() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        secDataBase = db.getReference("ZTArchive");
        mDataBase = db.getReference("ZTUsers");
        bankDB = db.getReference("Bank");
        id_order = findViewById(R.id.id_order);
        product = findViewById(R.id.archive_NameProduct);
        material = findViewById(R.id.archive_materialProduct);
        price = findViewById(R.id.archive_price);
        prepay = findViewById(R.id.archive_prepay);
        customer = findViewById(R.id.archive_customer);
        acc_date = findViewById(R.id.accDate_archive);
        exec_date = findViewById(R.id.execDate_archive);
        worker = findViewById(R.id.worker_archive);
        comment = findViewById(R.id.comment_archive);
        summInBank = findViewById(R.id.summInBankArchive);
        bankClass = new BankClass();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void getIntentArchive() {
        Intent i = getIntent();
        if (i != null) {
            id_order.setText(i.getStringExtra("id_order"));
            product.setText(i.getStringExtra("name_product"));
            material.setText(i.getStringExtra("material_product"));
            price.setText(i.getStringExtra("price"));
            prepay.setText(i.getStringExtra("prepay"));
            customer.setText(i.getStringExtra("fio_customer"));
            acc_date.setText(i.getStringExtra("accepted_date"));
            exec_date.setText(i.getStringExtra("executed_date"));
            worker.setText(i.getStringExtra("fio_worker"));
            comment.setText(i.getStringExtra("comment"));
        }
    }

    public void onClickReset(View view) {
        String id = id_order.getText().toString();
        String prod = product.getText().toString();
        String mater = material.getText().toString();
        String priceness = price.getText().toString();
        String prepays = prepay.getText().toString();
        String cust = customer.getText().toString();
        String accept_date = acc_date.getText().toString();
        String execut_date = exec_date.getText().toString();
        String read = "Введите значение";
        String work = worker.getText().toString();
        String comm = comment.getText().toString();
            Orders newOrder = new Orders(id,
                    prod,
                    mater,
                    priceness,
                    prepays,
                    cust,
                    accept_date,
                    execut_date,
                    work,
                    read,
                    comm);

        String titleText ="Восстановление из архива!";
        String bodyText = userName +"\n"+ userResetSwitch + " заказ из архива.\n"+prod;

        mDataBase.child(id).setValue(newOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        //notification
                        HashMap<String, String> dataSet = new HashMap<>();
                        dataSet.put("title", titleText);
                        dataSet.put("body", bodyText);

                        MyFireBaseInstanceService.sendPushToSingleInstance(ArchiveActivity.this, dataSet);

                        //финансы
                        if (prepay.getText().toString().equals("Заказчик отдал половину")) {
                            int data = Integer.parseInt(plus_public);
                            int data2 = Integer.parseInt(price.getText().toString()) / 2;
                            int bank_data = Integer.parseInt(bank_public);
                            String b = String.valueOf(bankClass.bankCancelPlus(bank_data, data2));
                            String c = String.valueOf(bankClass.cancelPlus(data, data2));
                            bankDB.child("bank").setValue(b);
                            bankDB.child("pluses").setValue(c);
                            Toast.makeText(ArchiveActivity.this, "Из банка вычтено " + data2 + "руб.", Toast.LENGTH_SHORT).show();
                        }
                        else if (prepay.getText().toString().equals("Заказчик полностью оплатил заказ")){
                            int data = Integer.parseInt(plus_public);
                            int data2 = Integer.parseInt(price.getText().toString());
                            int bank_data = Integer.parseInt(bank_public);
                            String b = String.valueOf(bankClass.bankCancelPlus(bank_data, data2));
                            String c = String.valueOf(bankClass.cancelPlus(data, data2));
                            bankDB.child("bank").setValue(b);
                            bankDB.child("pluses").setValue(c);
                            Toast.makeText(ArchiveActivity.this, "Из банка вычтено " + data2 + "руб.", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(ArchiveActivity.this, "Изделие " + prod + " восстановлено в Насущном", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ArchiveActivity.this, MainActivity.class);
                        i.putExtra("id_order", id);
                        i.putExtra("name_product", prod);
                        i.putExtra("material_product", mater);
                        i.putExtra("price", priceness);
                        i.putExtra("prepay", prepays);
                        i.putExtra("fio_customer", cust);
                        i.putExtra("accepted_date", accept_date);
                        i.putExtra("executed_date", execut_date);
                        i.putExtra("fio_worker", work);
                        i.putExtra("readiness", read);
                        i.putExtra("comment", comm);
                        secDataBase.child(id).removeValue();
                        startActivity(i);
                    } else {
                        Toast.makeText(ArchiveActivity.this, "Запись не восстановлена(((", Toast.LENGTH_SHORT).show();

                    }
                }
            });
    }

    private void getDataBank() {
        bankDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bank_public = snapshot.child("bank").getValue().toString();
                plus_public = snapshot.child("pluses").getValue().toString();
                minus_public = snapshot.child("minuses").getValue().toString();

                summInBank.setText(bank_public);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}