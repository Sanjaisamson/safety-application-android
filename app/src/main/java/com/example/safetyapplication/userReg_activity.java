package com.example.safetyapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class userReg_activity extends AppCompatActivity {

    EditText username, password;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);

        username = findViewById(R.id.Username);
        password = findViewById((R.id.Password));
        register =findViewById(R.id.button3);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String Username = username.getText().toString();
               String Password = password.getText().toString();
               registerUser(Username,Password);
               Toast.makeText(userReg_activity.this, Username+"registered successfully", Toast.LENGTH_SHORT).show();
               finish();
            }
        });
    }
    private void registerUser(String username,String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }
}