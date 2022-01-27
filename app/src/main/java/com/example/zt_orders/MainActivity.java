package com.example.zt_orders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zt_orders.service.MyFireBaseInstanceService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.text.TextUtils.*;

public class MainActivity extends AppCompatActivity {

    private ListView listView, archiveList;
    private ArrayAdapter<String> adapter;
    private List<String> listData;
    private List<Orders> listTemp;
    private ArrayAdapter<String> adapter1;
    private ArrayAdapter<String> adapter2;
    private List<String> listData1;
    private List<ArchiveFiles> listTemp1;
    private DatabaseReference mDataBase, secDataBase, expenseDB, bankDB;
    private FirebaseAuth mAuth;
    private View ordersInWork, addOrder, archive, addExpense;
    private DrawerLayout drawer;
    private View prepay_layout;
    private EditText name_product, material_product, price, fio_customer, accepted_date,
            executed_date, fio_worker, readiness, comment, expense_date, expense_name, expense_summ, expense_fio;
    private TextView id_order, summInBank, prepayTextView;
    private CheckBox prepayCheck, prepayCheckFull;
    private SearchView search;
    private int count1, count2, count_full;
    int countFields;
    String plus_public, minus_public, bank_public, userName, userAddsSwitch, urlIcon;
    BankClass bankClass;
    public boolean isVisible;
    private FirebaseUser fbUser;
    private TextView workerNavMenu;
    private ImageView imageNavMenu, photoCheka;
    private StorageReference storageReference, pathReference;
    NavigationView navigationView;
    View navHeader;
    private final int PICK_IMAGE_REQUEST = 71;
    private static final int REQUEST_TAKE_PHOTO = 1;
    Uri photoChekaPath;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getDataFromDB();
        setOnClickItem();
        getDataFromArchive();
        setOnClickItemArchive();
        getDataBank();

//        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseMessaging.getInstance().subscribeToTopic("private");

        switch (fbUser.getEmail()) {
            case  "trofimov@zt.ru":
                userName = "Трофимов Виктор";
                userAddsSwitch = "Добавил";
                urlIcon = "https://firebasestorage.googleapis.com/v0/b/zt-orders-db-a4e71.appspot.com/o/icons%2Fvitya.jpg?alt=media&token=461d1d80-acc9-4374-a17c-9afd68526ab3";
                break;
            case "zarapin@zt.ru":
                userName = "Зарапин Алексей";
                userAddsSwitch = "Добавил";
                urlIcon = "https://firebasestorage.googleapis.com/v0/b/zt-orders-db-a4e71.appspot.com/o/icons%2Flesha.jpg?alt=media&token=97e6d16e-df1f-4cd5-b822-f647f0ed1cbf";
                break;
            case "trofimova@zt.ru":
                userName = "Трофимова Анастасия";
                userAddsSwitch = "Добавила";
                urlIcon = "https://firebasestorage.googleapis.com/v0/b/zt-orders-db-a4e71.appspot.com/o/icons%2Fpuh.jpg?alt=media&token=429494ed-9b15-4abc-b672-cf99137c115a";
                break;
            case "zarapina@zt.ru":
                userName = "Зарапина Анастасия";
                userAddsSwitch = "Добавила";
                urlIcon = "https://firebasestorage.googleapis.com/v0/b/zt-orders-db-a4e71.appspot.com/o/icons%2Fnastya.jpg?alt=media&token=c0be4ffe-247d-4592-8730-f269b2acebfb";
                break;
        }

        downloadIcon(urlIcon, userName);


        price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        expense_summ.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        readiness.setShowSoftInputOnFocus(false);

        listView.setAdapter(adapter);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });



        Dialog calendar = new Dialog(MainActivity.this);
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
                View focus = MainActivity.this.getCurrentFocus();
                if (focus == accepted_date)
                accepted_date.setText(selectedDate);
                else if (focus == executed_date)
                    executed_date.setText(selectedDate);
                else if (focus == expense_date)
                    expense_date.setText(selectedDate);
                calendar.dismiss();
            }
        });

        accepted_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                accepted_date.setShowSoftInputOnFocus(false);
                if (hasFocus == true)
                calendar.show();
            }
        });

        executed_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                executed_date.setShowSoftInputOnFocus(false);
                if (hasFocus == true)
                    calendar.show();
            }
        });

        expense_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    calendar.show();
            }
        });

        prepayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    price.setEnabled(false);
                    prepayCheckFull.setEnabled(false);

                    if (!TextUtils.isEmpty(price.getText().toString())) {
                        prepay_layout.setBackgroundColor(Color.rgb(255, 215, 0));
                        prepayTextView.setText("Заказчик отдал половину");

                    } else {
                        price.setEnabled(true);
                        prepayCheckFull.setEnabled(true);
                        prepayCheck.setChecked(false);
                        Toast.makeText(MainActivity.this, "Сначала укажи стоимость", Toast.LENGTH_SHORT).show();
                        price.requestFocus();
                    }

                }
                else {
                    price.setEnabled(true);
                    prepayCheckFull.setEnabled(true);
                    prepay_layout.setBackgroundColor(Color.WHITE);
                    prepayTextView.setText("Не дали денег");
                }
                }
        });

        prepayCheckFull.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    price.setEnabled(false);
                    prepayCheck.setEnabled(false);

                    if (!TextUtils.isEmpty(price.getText().toString())) {
                        prepay_layout.setBackgroundColor(Color.rgb(255, 215, 0));
                        prepayTextView.setText("Заказчик полностью оплатил заказ");

                    } else {
                        price.setEnabled(true);
                        prepayCheck.setEnabled(true);
                        prepayCheckFull.setChecked(false);
                        Toast.makeText(MainActivity.this, "Сначала укажи стоимость", Toast.LENGTH_SHORT).show();
                        price.requestFocus();
                    }

                }
                else {
                    price.setEnabled(true);
                    prepayCheck.setEnabled(true);
                    prepay_layout.setBackgroundColor(Color.WHITE);
                    prepayTextView.setText("Не дали денег");
                }
            }
        });

        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count1 = (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        secDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count2 = (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fio_worker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Toast.makeText(MainActivity.this, "Ты не можешь изменить значение этого поля", Toast.LENGTH_SHORT).show();
                    readiness.requestFocus();
                }
            }
        });

        readiness.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showPopup(v);
                    comment.requestFocus();
                }
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
                        readiness.setText("Принят в работу");
                        return true;
                    case R.id.second:
                        readiness.setText("Разработан макет");
                        return true;
                    case R.id.third:
                        readiness.setText("Вырезан");
                        return true;
                    case R.id.fourth:
                        readiness.setText("Готов к покраске");
                        return true;
                    case R.id.fifth:
                        readiness.setText("Передан курьеру");
                        return true;
                    case R.id.sixth:
                        readiness.setText("Передан заказчику");
                        return true;
                    default:
                        return false;
                }

            }
        });
        popupMenu.show();
    }

    private void init(){
        listView = findViewById(R.id.orders_list);
        archiveList = findViewById(R.id.archive_list);
        listData = new ArrayList<>();
        listTemp = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listData);
        listView.setAdapter(adapter);
        listData1 = new ArrayList<>();
        listTemp1 = new ArrayList<>();
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listData1);
        archiveList.setAdapter(adapter1);
        ordersInWork = findViewById(R.id.ordersInWork);
        addExpense = findViewById(R.id.addExpense);
        addOrder = findViewById(R.id.addOrder);
        archive = findViewById(R.id.archive);
        id_order = findViewById(R.id.id_order);
        name_product = findViewById(R.id.add_NameProduct);
        material_product = findViewById(R.id.add_MaterialProduct);
        price = findViewById(R.id.add_Price);
        fio_customer = findViewById(R.id.add_FioCustomer);
        accepted_date = findViewById(R.id.add_AcceptedDate);
        executed_date = findViewById(R.id.add_ExecutedDate);
        fio_worker = findViewById(R.id.add_FioWorker);
        readiness = findViewById(R.id.add_Readiness);
        comment = findViewById(R.id.add_Comment);
        prepayCheck = findViewById(R.id.prepayCheck);
        prepayCheckFull = findViewById(R.id.prepayCheckFull);
        prepayTextView = findViewById(R.id.prepayTextView);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        mDataBase = db.getReference("ZTUsers");
        secDataBase = db.getReference("ZTArchive");
        expenseDB = db.getReference("ExpenseDB");
        bankDB = db.getReference("Bank");
        mAuth = FirebaseAuth.getInstance();
        drawer = findViewById(R.id.drawer);
        search = findViewById(R.id.search);
        prepay_layout = findViewById(R.id.prepay_layuot);
        count1 = 0; count2 = 0; count_full = 0;
        expense_date = findViewById(R.id.add_expense_date);
        expense_name = findViewById(R.id.add_expense_name);
        expense_summ = findViewById(R.id.add_expense_summ);
        expense_fio = findViewById(R.id.add_expense_FioWorker);
        countFields = 0;
        summInBank = findViewById(R.id.summInBank);
        bankClass = new BankClass();
        isVisible = true;
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        navigationView = findViewById(R.id.menu);
        navHeader = navigationView.getHeaderView(0);
        workerNavMenu = navHeader.findViewById(R.id.worker_nav_menu);
        imageNavMenu = navHeader.findViewById(R.id.navMenuImage);
        storageReference = FirebaseStorage.getInstance().getReference();
        photoCheka = findViewById(R.id.photo_cheka);
    }

    public void onClickAdd(View view) {
        String id = id_order.getText().toString();
        String product = name_product.getText().toString();
        String material = material_product.getText().toString();
        String priceness = price.getText().toString();
        String prepay = prepayTextView.getText().toString();
        String customer = fio_customer.getText().toString();
        String acc_date = accepted_date.getText().toString();
        String exec_date = executed_date.getText().toString();
        String worker = fio_worker.getText().toString();
        String ready = readiness.getText().toString();
        String comm = comment.getText().toString();
        if (!isEmpty(product) && !isEmpty(material)
                && (!isEmpty(priceness)) && (!isEmpty(prepay))
                && (!isEmpty(customer)) && (!isEmpty(acc_date))
                && (!isEmpty(exec_date)) && (!isEmpty(worker))) {
            Orders newOrder = new Orders(id,
                product,
                material,
                priceness,
                prepay,
                customer,
                acc_date,
                exec_date,
                worker,
                ready,
                comm);

            String titleText ="Добавлен новый заказ!!";
            String bodyText = userName + "\n"+userAddsSwitch+" новый заказ\n"+product;

            mDataBase.child(id).setValue(newOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this,"Изделие "+product+" добавлено в базу", Toast.LENGTH_SHORT).show();
//notification
                        HashMap<String, String> dataSet = new HashMap<>();
                        dataSet.put("title", titleText);
                        dataSet.put("body", bodyText);
                        MyFireBaseInstanceService.sendPushToSingleInstance(MainActivity.this, dataSet);

                        //финансы
                        if (prepayCheck.isChecked()) {
                            int data = Integer.parseInt(plus_public);
                            int data2 = Integer.parseInt(price.getText().toString()) / 2;
                            int bank_data = Integer.parseInt(bank_public);
                            String b = String.valueOf(bankClass.bankCalcPlus(bank_data, data2));
                            String c = String.valueOf(bankClass.plusesCount(data, data2));
                            bankDB.child("bank").setValue(b);
                            bankDB.child("pluses").setValue(c);



                            Toast.makeText(MainActivity.this, "В банк добавлено " + data2 + "руб.\nБ-дзынь!", Toast.LENGTH_SHORT).show();
                            }
                        else if (prepayCheckFull.isChecked()){
                            int data = Integer.parseInt(plus_public);
                            int data2 = Integer.parseInt(price.getText().toString());
                            int bank_data = Integer.parseInt(bank_public);
                            String b = String.valueOf(bankClass.bankCalcPlus(bank_data, data2));
                            String c = String.valueOf(bankClass.plusesCount(data, data2));
                            bankDB.child("bank").setValue(b);
                            bankDB.child("pluses").setValue(c);
                            Toast.makeText(MainActivity.this, "В банк добавлено " + data2 + "руб.\nБ-дзынь!", Toast.LENGTH_SHORT).show();
                            }

                        setTextToFields();
                    } else {
                        Toast.makeText(MainActivity.this,"Запись не добавлена(((", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        else {
            Toast.makeText(this,"Не забывай блять вводить данные!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCLickCancel(View view) {
        setTextToFields();
    }

    private void getDataFromDB() {
        ValueEventListener vListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count1 = 0;
                count1 = (int) snapshot.getChildrenCount();
                if (listData.size() > 0) listData.clear();
                if (listTemp.size() > 0) listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Orders orders = ds.getValue(Orders.class);
                    assert orders != null;
                    listData.add(orders.name_product);
                    listTemp.add(orders);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDataBase.addValueEventListener(vListener);
    }

    private void getDataFromArchive() {
        ValueEventListener vListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count2 = 0;
                count2 = (int) snapshot.getChildrenCount();
                if (listData1.size() > 0) listData1.clear();
                if (listTemp1.size() > 0) listTemp1.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ArchiveFiles archiveFiles = ds.getValue(ArchiveFiles.class);
                    assert archiveFiles != null;
                    listData1.add(archiveFiles.arch_product);
                    listTemp1.add(archiveFiles);
                }
                adapter1.notifyDataSetChanged();
//                setTextToFields();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        secDataBase.addValueEventListener(vListener);
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

    public void setOnClickItem() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Orders order = listTemp.get(position);
                Intent i = new Intent(MainActivity.this, ShowOrderActivity.class);
                i.putExtra("id_order", order.id_order);
                i.putExtra("name_product", order.name_product);
                i.putExtra("material_product", order.material_product);
                i.putExtra("price", order.price);
                i.putExtra("prepay", order.prepay);
                i.putExtra("fio_customer", order.fio_customer);
                i.putExtra("accepted_date", order.accepted_date);
                i.putExtra("executed_date", order.executed_date);
                i.putExtra("fio_worker", order.fio_worker);
                i.putExtra("readiness", order.readiness);
                i.putExtra("comment", order.comment);
                startActivity(i);
            }
        });
    }

    public void setOnClickItemArchive() {
        archiveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArchiveFiles archiveFiles = listTemp1.get(position);
                Intent i = new Intent(MainActivity.this, ArchiveActivity.class);
                i.putExtra("id_order", archiveFiles.id_archive);
                i.putExtra("name_product", archiveFiles.arch_product);
                i.putExtra("material_product", archiveFiles.material_arch);
                i.putExtra("price", archiveFiles.price_arch);
                i.putExtra("prepay", archiveFiles.prepay_archive);
                i.putExtra("fio_customer", archiveFiles.arch_customer);
                i.putExtra("accepted_date", archiveFiles.acc_date_arch);
                i.putExtra("executed_date", archiveFiles.exec_date_arch);
                i.putExtra("fio_worker", archiveFiles.arch_worker);
                i.putExtra("comment", archiveFiles.comment);
                startActivity(i);
            }
        });
    }

    public void onOrdersInWorkClick(MenuItem item) {
        archive.setVisibility(View.GONE);
        ordersInWork.setVisibility(View.VISIBLE);
        addOrder.setVisibility(View.GONE);
        search.setVisibility(View.VISIBLE);
        addExpense.setVisibility(View.GONE);
        drawer.closeDrawer(GravityCompat.END);
        addExpense.setVisibility(View.GONE);
    }

    public void onAddOrderClick(MenuItem item) {
        archive.setVisibility(View.GONE);
        ordersInWork.setVisibility(View.GONE);
        addOrder.setVisibility(View.VISIBLE);
        search.setVisibility(View.INVISIBLE);
        drawer.closeDrawer(GravityCompat.END);
        setTextToFields();
        addExpense.setVisibility(View.GONE);
    }

    public void onArchiveFilesClick(MenuItem item) {
        ordersInWork.setVisibility(View.GONE);
        addOrder.setVisibility(View.GONE);
        archive.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
        drawer.closeDrawer(GravityCompat.END);
        addExpense.setVisibility(View.GONE);
    }

    public void onAddExpenseClick(MenuItem item) {
        archive.setVisibility(View.GONE);
        ordersInWork.setVisibility(View.GONE);
        addOrder.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
        addExpense.setVisibility(View.VISIBLE);
        drawer.closeDrawer(GravityCompat.END);
        setTextToExpensesFields();
    }

    public void onCloseNavMenuClick(MenuItem item) {
        drawer.closeDrawer(GravityCompat.END);
    }

    public void onClickOpenNavMenu(View view) {
        drawer.openDrawer(GravityCompat.END);
    }

    @SuppressLint("SetTextI18n")
    private void setTextToFields() {
        prepay_layout.setBackgroundColor(Color.WHITE);
        material_product.setText("");
        fio_customer.setText("");
        accepted_date.setText("");
        executed_date.setText("");
        readiness.setText("");
        prepayTextView.setText("Не дали денег");

        fio_worker.setText(userName);

        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count1 = 0;
                count1 = (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        secDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count2 = (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        count_full = count1 + count2 + 1;
        String prodId = "Заказ № " + count_full;
        id_order.setText(prodId);
        name_product.setText(count_full + ". ");
//        String temp_bank = bank_public;
//        String temp_plus = plus_public;
//        String temp_minus = minus_public;

//        if (prepayCheck.isChecked())
        prepayCheck.setChecked(false);
//        else if (prepayCheckFull.isChecked() == true)
        prepayCheckFull.setChecked(false);
        price.setText("");

//        bankDB.child("bank").setValue(temp_bank);
//        bankDB.child("pluses").setValue(temp_plus);
//        bankDB.child("minuses").setValue(temp_minus);
    }

    public void onProfitClick(MenuItem item) {
        Intent i = new Intent(MainActivity.this, ProfitActivity.class);
        startActivity(i);
    }

    public void onClickAddExpense(View view) {
        String exp_date = expense_date.getText().toString();
        String exp_name = expense_name.getText().toString();
        String exp_summ = expense_summ.getText().toString();
        String exp_fio = expense_fio.getText().toString();

        if (!TextUtils.isEmpty(exp_date) && !TextUtils.isEmpty(exp_name) &&
                (!TextUtils.isEmpty(exp_summ)) && (!TextUtils.isEmpty(exp_fio))) {
            HistoryExpenses newExpense = new HistoryExpenses(exp_date,
                    exp_name,
                    exp_summ,
                    exp_fio);

            String titleText ="Добавлен новый расход!!";
            String bodyText = userName + "\n"+userAddsSwitch+" новый расход\n"+exp_name;

            expenseDB.child(exp_name).setValue(newExpense).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        //notification
                        HashMap<String, String> dataSet = new HashMap<>();
                        dataSet.put("title", titleText);
                        dataSet.put("body", bodyText);
                        MyFireBaseInstanceService.sendPushToSingleInstance(MainActivity.this, dataSet);

                        //финансы
                        int data = Integer.parseInt(minus_public);
                        int data2 = Integer.parseInt(expense_summ.getText().toString());
                        int bank_data = Integer.parseInt(bank_public);
                        String b = String.valueOf(bankClass.bankCalcMinus(bank_data, data2));
                        String c = String.valueOf(bankClass.minusesCount(data, data2));
                        bankDB.child("bank").setValue(b);
                        bankDB.child("minuses").setValue(c);

                        Toast.makeText(MainActivity.this,"Запись "+exp_name+" о трате от "+exp_date+" добавлена в базу", Toast.LENGTH_SHORT).show();
                        setTextToExpensesFields();
                    } else {
                        Toast.makeText(MainActivity.this,"Запись не добавлена(((", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        else {
            Toast.makeText(this,"Не забывай блять вводить данные!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCLickCancelAddExpense(View view) {
        setTextToExpensesFields();
    }

    private void setTextToExpensesFields() {
        expense_date.setText("");
        expense_name.setText("");
        expense_summ.setText("");
        expense_fio.setText("");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String value = currentUser.getEmail();
        switch (value) {
            case  "trofimov@zt.ru":
                expense_fio.setText("Трофимов Виктор");
                break;
            case "zarapin@zt.ru":
                expense_fio.setText("Зарапин Алексей");
                break;
            case "trofimova@zt.ru":
                expense_fio.setText("Трофимова Анастасия");
                break;
            case "zarapina@zt.ru":
                expense_fio.setText("Зарапина Анастасия");
                break;
        }
}

    public void onExpensesClick(MenuItem item) {
        Intent i = new Intent(MainActivity.this, ExpensesActivity.class);
        startActivity(i);
    }

    private void downloadIcon(String url, String user) {
        workerNavMenu.setText(user);
        Glide.with(this)
                .load(url)
                .into(imageNavMenu);
    }

    public void onClickAddPhotoCheka(View view) {
        try {
            //Используем стандартное системное намерение на использование камеры:
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Задаем возможность работать с полученными с камеры данными:
            startActivityForResult(captureIntent, REQUEST_TAKE_PHOTO);
        }
        catch(ActivityNotFoundException cant){
            //Показываем сообщение об ошибке:
            String errorMessage = "Ваше устройство не поддерживает работу с камерой!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Фотка сделана, извлекаем миниатюру картинки
            Bundle extras = data.getExtras();
            Bitmap thumbnailBitmap = (Bitmap) extras.get("data");
            photoCheka.setImageBitmap(thumbnailBitmap);
        }
    }

}