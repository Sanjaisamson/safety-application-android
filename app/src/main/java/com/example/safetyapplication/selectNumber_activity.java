package com.example.safetyapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class selectNumber_activity extends AppCompatActivity implements EmergencyContactAdapter.ButtonClickListener  {
    Button guardian1,guardian2,guardian3;
    String phoneNumber,contact_index;
    private EmergencyContactAdapter emergencyContactAdapter;
    private List<EmergencyContact> contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_number);
        ListView listView = findViewById(R.id.listView);

        // Retrieve and display numbers and names
        retrieveData();

        // Set up the custom adapter
        emergencyContactAdapter = new EmergencyContactAdapter(this, contactList, this);
        listView.setAdapter(emergencyContactAdapter);

    }
    public void onButtonClick(int position) {
        // Handle button click here with the position value
        String phoneNumber = contactList.get(position).getPhoneNumber();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("phoneNumber", phoneNumber);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
    private void retrieveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("EmergencyContacts", Context.MODE_PRIVATE);
        int registrationNumber = sharedPreferences.getInt("registrationNumber", 0);

        contactList = new ArrayList<>();

        for (int i = 1; i <= registrationNumber; i++) {
            String phoneNumber = sharedPreferences.getString("number_" + i, "");
            String userName = sharedPreferences.getString("name_" + i, "");

            // Create an EmergencyContact object and add it to the contactList
            EmergencyContact contact = new EmergencyContact(userName, phoneNumber);
            contactList.add(contact);
        }
    }
    private String getEmergencyContact(String contact_index) {
        SharedPreferences sharedPreferences = getSharedPreferences("EmergencyContacts", Context.MODE_PRIVATE);
        String getContact = sharedPreferences.getString("number_"+contact_index, "");
        return getContact;
    }

}