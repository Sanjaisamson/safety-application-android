package com.example.safetyapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registration_activity extends AppCompatActivity {

    EditText label, number;
    Button addNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        label = findViewById(R.id.guardianLabel);
        number = findViewById(R.id.guardianNumber);
        addNumber = findViewById(R.id.buttonAddNumber);
        addNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String labelInput = label.getText().toString();
                String numberInput = number.getText().toString();
                saveEmergencyContact(labelInput,numberInput);
                finish();
            }
        });
    }
    private void saveEmergencyContact(String name,String phoneNumber) {
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = getSharedPreferences("EmergencyContacts",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int registrationNumber = sharedPreferences.getInt("registrationNumber", 0) + 1;
        Toast.makeText(this, "this is "+registrationNumber + " registration", Toast.LENGTH_SHORT).show();
        if((registrationNumber<=3)){
            editor.putInt("registrationNumber", registrationNumber);
            editor.putString("name_" + registrationNumber, name);
            editor.putString("number_" + registrationNumber, phoneNumber);
            editor.apply();
            Toast.makeText(registration_activity.this, "emergency contact Saved successfully", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Contacts exceeds the limits", Toast.LENGTH_SHORT).show();
        }
    }

}