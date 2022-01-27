package com.example.zt_orders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edSurname, edPassword;
    private Button btn_SignIn, btn_SignOut, btn_Start;
    private TextView txt_authorization, hello;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        startService(new Intent(this, MyService.class));
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        edSurname = findViewById(R.id.edSurname);
        edPassword = findViewById(R.id.edPassword);
        btn_SignIn = findViewById(R.id.btn_SignIn);
        btn_SignOut = findViewById(R.id.btn_SignOut);
        btn_Start = findViewById(R.id.btn_Start);
        txt_authorization = findViewById(R.id.txt_authorization);
        hello = findViewById(R.id.hello);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String value = currentUser.getEmail();
            switch (value) {
                case  "trofimov@zt.ru":
                    hello.setText("Здравствуй, Виктор");
                    break;
                case "zarapin@zt.ru":
                    hello.setText("Здравствуй, Алексей");
                    break;
                case "trofimova@zt.ru":
                    hello.setText("Здравствуй, Анастасия");
                    break;
                case "zarapina@zt.ru":
                    hello.setText("Здравствуй, Анастасия");
                    break;
            }
            showStartScreen();
        }
        else {
            showAutorization();
        }
    }

    public void onClickSignIn(View view) {
        if (!TextUtils.isEmpty(edSurname.getText().toString()) &&
                !TextUtils.isEmpty(edPassword.getText().toString())) {
                    mAuth.signInWithEmailAndPassword(edSurname.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Вы успешно вошли", Toast.LENGTH_SHORT).show();
                                showStartScreen();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Нет такого пользователя", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            Toast.makeText(LoginActivity.this, "Введите данные для входа", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSignOut(View view) {
        FirebaseAuth.getInstance().signOut();
        showAutorization();
    }

    public void onClickStart(View view) {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

    private void showAutorization() {
       txt_authorization.setVisibility(View.VISIBLE);
       edSurname.setVisibility(View.VISIBLE);
       edPassword.setVisibility(View.VISIBLE);
       btn_SignIn.setVisibility(View.VISIBLE);
       hello.setVisibility(View.GONE);
       btn_Start.setVisibility(View.GONE);
       btn_SignOut.setVisibility(View.GONE);
    }
    private void showStartScreen() {
        txt_authorization.setVisibility(View.GONE);
        edSurname.setVisibility(View.GONE);
        edPassword.setVisibility(View.GONE);
        btn_SignIn.setVisibility(View.GONE);
        hello.setVisibility(View.VISIBLE);
        btn_Start.setVisibility(View.VISIBLE);
        btn_SignOut.setVisibility(View.VISIBLE);
    }
}
