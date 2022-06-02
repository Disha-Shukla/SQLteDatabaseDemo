package com.example.databaseexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactList extends AppCompatActivity {


    ListView lv_ContactList;
    DatabaseHandler db;
    ArrayList<Contact> ContacList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        lv_ContactList = findViewById(R.id.lv_ContacList);
        ContacList = new ArrayList<>();
        db = new DatabaseHandler(this);

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllContacts();

        for (Contact cn : contacts) {
            String log = "Id: " + cn.get_id() + " ,Name: " + cn.get_name() + " ,Phone: " +
                    cn.get_phone_number()+ " image "+cn.getImage();
            ContacList.add(new Contact(cn.get_id(), cn.get_name(), cn.get_phone_number(), cn.getEmail(), cn.getAddress(), cn.getImage()));
            // Writing Contacts to log
            Log.v("Name: ", log);
        }

        CustomAdapter customAdapter = new CustomAdapter
                (ContactList.this, ContacList);
        lv_ContactList.setAdapter(customAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.insert:
                Intent i = new Intent(ContactList.this, MainActivity.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}