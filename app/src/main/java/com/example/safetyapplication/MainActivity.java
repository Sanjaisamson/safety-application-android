package com.example.safetyapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText userName, password ;
    Button login, register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        login = findViewById(R.id.button3);
        register = findViewById(R.id.button4);
        userName.setText("");
        password.setText("");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUserName = userName.getText().toString();
                String newPassword = password.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                String getUsername = sharedPreferences.getString("username", "");
                String getPassword = sharedPreferences.getString("password", "");
                if (newUserName.equals(getUsername) && newPassword.equals(getPassword)) {
                    Intent intent=new Intent(getApplicationContext(), SOS_activity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Login successfull", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), userReg_activity.class);
                startActivity(intent);
            }
        });
    }
}