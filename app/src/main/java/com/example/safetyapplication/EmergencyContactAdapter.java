package com.example.safetyapplication;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

// EmergencyContactAdapter.java
public class EmergencyContactAdapter extends ArrayAdapter<EmergencyContact> {

    private List<EmergencyContact> contactList;
    String phoneNumber,contact_index;
    private ButtonClickListener buttonClickListener;
    public interface ButtonClickListener {
        void onButtonClick(int position);
    }
    public EmergencyContactAdapter(Context context, List<EmergencyContact> contactList, ButtonClickListener buttonClickListener) {
        super(context, R.layout.list_layout, contactList);
        this.contactList = contactList;
        this.buttonClickListener = buttonClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_layout, null);
        }

        TextView textView = view.findViewById(R.id.textView);
        TextView textView2 = view.findViewById(R.id.textView2);
        Button button = view.findViewById(R.id.button);

        EmergencyContact contact = contactList.get(position);
        textView2.setText("hii hello");
        textView.setText(contact.getName());
        textView2.setText(contact.getPhoneNumber());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the onButtonClick method in the callback interface
                if (buttonClickListener != null) {
                    buttonClickListener.onButtonClick(position);
                }
            }
        });
        return view;
    }
}
