package com.example.zt_orders;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ShowOrderActivity extends AppCompatActivity {

    private TextView id_order, summInBank;
    private EditText product_name;
    private EditText material_name;
    private EditText money;
    private EditText customer_fio;
    private EditText date_accepted;
    private EditText date_executed;
    private EditText worker_fio;
    private EditText readi;
    private EditText com, prepayShowText;
    private DatabaseReference mDataBase, secDataBase, profitDB, bankDB;
    String plus_public, minus_public, bank_public, userName, userUpdateSwitch,
            userAddsSwitch;
    BankClass bankClass;
    private FirebaseUser fbUser;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_order);
        init();
        getIntentOrder();
        getDataBank();

        worker_fio.setShowSoftInputOnFocus(false);
        switch (fbUser.getEmail()) {
            case  "trofimov@zt.ru":
                userName = "Трофимов Виктор";
                userUpdateSwitch = "Изменил";
                userAddsSwitch = "Добавил";

                worker_fio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            showUsersPopup(v);
                        }
                    }
                });

                break;
            case "zarapin@zt.ru":
                userName = "Зарапин Алексей";
                userUpdateSwitch = "Изменил";
                userAddsSwitch = "Добавил";

                worker_fio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            Toast.makeText(ShowOrderActivity.this, "Ты не можешь изменить значение этого поля", Toast.LENGTH_SHORT).show();
                            readi.requestFocus();
                        }
                    }
                });

                break;
            case "trofimova@zt.ru":
                userName = "Трофимова Анастасия";
                userUpdateSwitch = "Изменила";
                userAddsSwitch = "Добавила";

                worker_fio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            Toast.makeText(ShowOrderActivity.this, "Ты не можешь изменить значение этого поля", Toast.LENGTH_SHORT).show();
                            readi.requestFocus();
                        }
                    }
                });

                break;
            case "zarapina@zt.ru":
                userName = "Зарапина Анастасия";
                userUpdateSwitch = "Изменила";
                userAddsSwitch = "Добавила";

                worker_fio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            Toast.makeText(ShowOrderActivity.this, "Ты не можешь изменить значение этого поля", Toast.LENGTH_SHORT).show();
                            readi.requestFocus();
                        }
                    }
                });

                break;
        }


        prepayShowText.setShowSoftInputOnFocus(false);
        readi.setShowSoftInputOnFocus(false);

        prepayShowText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
//                    showPopup(v);
                    Toast.makeText(ShowOrderActivity.this, "Ты не можешь изменить значение этого поля", Toast.LENGTH_SHORT).show();
                    customer_fio.requestFocus();
                }
            }
        });

        money.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Toast.makeText(ShowOrderActivity.this, "Ты не можешь изменить значение этого поля", Toast.LENGTH_SHORT).show();
                    customer_fio.requestFocus();
                }
            }
        });

        Dialog calendar = new Dialog(ShowOrderActivity.this);
        calendar.setTitle("Выбери дату и нажми на нее");
        calendar.setContentView(R.layout.calendar_dialog);
        calendar.setCancelable(false);
        CalendarView calendarView = calendar.findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month;
                int mDay = dayOfMonth;
                String selectedDate = new StringBuilder().append(mDay).append(".")
                        .append(mMonth + 1).append(".").append(mYear)
                        .append(" ").toString();
                View focus = ShowOrderActivity.this.getCurrentFocus();
                if (focus == date_accepted)
                    date_accepted.setText(selectedDate);
                else if (focus == date_executed)
                    date_executed.setText(selectedDate);
                calendar.dismiss();
            }
        });

        date_accepted.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                date_accepted.setShowSoftInputOnFocus(false);
                if (hasFocus)
                    calendar.show();
            }
        });

        date_executed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                date_executed.setShowSoftInputOnFocus(false);
                if (hasFocus)
                    calendar.show();
            }
        });

        com.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (view.getId() ==R.id.comment) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction()&MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        readi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showPopup(v);
                }
            }
        });

    }

    private void showUsersPopup(View v) {
        PopupMenu usersPopup = new PopupMenu(this, v);
        usersPopup.inflate(R.menu.users_popup);
        usersPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.trofimov:
                        worker_fio.setText("Трофимов Виктор");
                        return true;
                    case R.id.trofimova:
                        worker_fio.setText("Трофимова Анастасия");
                        return true;
                    case R.id.zarapin:
                        worker_fio.setText("Зарапин Алексей");
                        return true;
                    case R.id.zarapina:
                        worker_fio.setText("Зарапина Анастасия");
                        return true;
                    default: return false;
                }
            }
        });
        usersPopup.show();
    }

    private void init() {
        id_order = findViewById(R.id.id_order);
        product_name = findViewById(R.id.name_product);
        material_name = findViewById(R.id.material);
        money = findViewById(R.id.price);
        customer_fio = findViewById(R.id.fio_customer);
        date_accepted = findViewById(R.id.accepted_date);
        date_executed = findViewById(R.id.executed_date);
        worker_fio = findViewById(R.id.fio_worker);
        readi = findViewById(R.id.readiness);
        com = findViewById(R.id.comment);
        mDataBase = FirebaseDatabase.getInstance().getReference("ZTUsers");
        secDataBase = FirebaseDatabase.getInstance().getReference("ZTArchive");
        profitDB = FirebaseDatabase.getInstance().getReference("ProfitDB");
        bankDB = FirebaseDatabase.getInstance().getReference("Bank");
        bankClass = new BankClass();
        summInBank = findViewById(R.id.summInBankShowOrders);
        prepayShowText = findViewById(R.id.prepayShowText);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void getIntentOrder() {
        Intent i =getIntent();
            if (i != null) {
                id_order.setText(i.getStringExtra("id_order"));
                product_name.setText(i.getStringExtra("name_product"));
                material_name.setText(i.getStringExtra("material_product"));
                money.setText(i.getStringExtra("price"));
                prepayShowText.setText(i.getStringExtra("prepay"));
                customer_fio.setText(i.getStringExtra("fio_customer"));
                date_accepted.setText(i.getStringExtra("accepted_date"));
                date_executed.setText(i.getStringExtra("executed_date"));
                worker_fio.setText(i.getStringExtra("fio_worker"));
                readi.setText(i.getStringExtra("readiness"));
                com.setText(i.getStringExtra("comment"));
            }
    }

    public void onClickButtonAdd(View view) {
        String id = id_order.getText().toString();
        String product = product_name.getText().toString();
        String material = material_name.getText().toString();
        String priceness = money.getText().toString();
        String prepays = prepayShowText.getText().toString();
        String customer = customer_fio.getText().toString();
        String acc_date = date_accepted.getText().toString();
        String exec_date = date_executed.getText().toString();
        String worker = worker_fio.getText().toString();
        String ready = readi.getText().toString();
        String comm = com.getText().toString();
        updateData(id, product, material,priceness,prepays,customer,acc_date,exec_date,worker,ready,comm);

    }

    private void updateData(String id,
                            String product,
                            String material,
                            String priceness,
                            String prepays,
                            String customer,
                            String acc_date,
                            String exec_date,
                            String worker,
                            String ready,
                            String comm) {

        HashMap<String, Object> Order = new HashMap<>();
        Order.put("id_order", id);
        Order.put("name_product", product);
        Order.put("material_product", material);
        Order.put("price", priceness);
        Order.put("prepay", prepays);
        Order.put("fio_customer", customer);
        Order.put("accepted_date", acc_date);
        Order.put("executed_date", exec_date);
        Order.put("fio_worker", worker);
        Order.put("readiness", ready);
        Order.put("comment", comm);


        String titleText ="Изменены данные заказа!";
        String bodyText = userName + "\nВ заказе "+product_name.getText().toString()+"\n"+userUpdateSwitch+" данные заказа.";

        mDataBase.child(id).updateChildren(Order).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {

                    //notification
                    HashMap<String, String> data = new HashMap<>();
                    data.put("title", titleText);
                    data.put("body", bodyText);

                    MyFireBaseInstanceService.sendPushToSingleInstance(ShowOrderActivity.this, data);

                    Toast.makeText(ShowOrderActivity.this,"Запись изменена)))", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShowOrderActivity.this,"Не получилось изменить(((", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onClickButtonArchive(View view){
        String id = id_order.getText().toString();
        String product = product_name.getText().toString();
        String material = material_name.getText().toString();
        String priceness = money.getText().toString();
        String prepay = prepayShowText.getText().toString();
        String customer = customer_fio.getText().toString();
        String acc_date = date_accepted.getText().toString();
        String exec_date = date_executed.getText().toString();
        String worker = worker_fio.getText().toString();
        String comm = com.getText().toString();

            ArchiveFiles newFile = new ArchiveFiles(id,
                    product,
                    material,
                    priceness,
                    prepay,
                    customer,
                    acc_date,
                    exec_date,
                    worker,
                    comm);

        String titleText ="Заказ добавлен в архив!";
        String bodyText = userName + "\n"+userAddsSwitch+" в архив заказ\n"+product;

        secDataBase.child(id).setValue(newFile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        //добавление в историю доходов
                        HistoryProfit newFile = new HistoryProfit(id,
                                exec_date,
                                product,
                                priceness);
                        profitDB.child(id).setValue(newFile);

                        //финансы

                        switch (prepayShowText.getText().toString()){
                            case "Заказчик отдал половину":
                                int data = Integer.parseInt(plus_public);
                                int data2 = Integer.parseInt(money.getText().toString()) / 2;
                                int bank_data = Integer.parseInt(bank_public);
                                String b = String.valueOf(bankClass.bankCalcPlus(bank_data, data2));
                                String c = String.valueOf(bankClass.plusesCount(data, data2));

                                bankDB.child("bank").setValue(b);
                                bankDB.child("pluses").setValue(c);
                                break;
                            case "Не дали денег":
                                data = Integer.parseInt(plus_public);
                                data2 = Integer.parseInt(money.getText().toString());
                                bank_data = Integer.parseInt(bank_public);
                                b = String.valueOf(bankClass.bankCalcPlus(bank_data, data2));
                                c = String.valueOf(bankClass.plusesCount(data, data2));
                                bankDB.child("bank").setValue(b);
                                bankDB.child("pluses").setValue(c);
                                break;
                            case "Заказчик полностью оплатил заказ":

                                break;
                        }


                        //удалять из ZTUsers
                        mDataBase.child(id).removeValue();
                        Intent i = new Intent(ShowOrderActivity.this, MainActivity.class);
                        startActivity(i);

                        //notification
                        HashMap<String, String> dataset = new HashMap<>();
                        dataset.put("title", titleText);
                        dataset.put("body", bodyText);

                        MyFireBaseInstanceService.sendPushToSingleInstance(ShowOrderActivity.this, dataset);

                        Toast.makeText(ShowOrderActivity.this,"Изделие ( "+product+" ) добавлено в архив", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ShowOrderActivity.this,"Запись не добавлена(((", Toast.LENGTH_SHORT).show();

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

    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.first:
                        readi.setText("Принят в работу");
                        return true;
                    case R.id.second:
                        readi.setText("Разработан макет");
                        return true;
                    case R.id.third:
                        readi.setText("Вырезан");
                        return true;
                    case R.id.fourth:
                        readi.setText("Готов к покраске");
                        return true;
                    case R.id.fifth:
                        readi.setText("Передан курьеру");
                        return true;
                    case R.id.sixth:
                        readi.setText("Передан заказчику");
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                com.requestFocus();
            }
        });
    }


}
