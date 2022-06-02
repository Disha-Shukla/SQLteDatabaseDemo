package com.example.databaseexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {

    Button btnAddEmployee, btnViewEmployee;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        initialize();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(HomeScreen.this, MainActivity.class);
                startActivity(addIntent);
            }
        });

        btnViewEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(HomeScreen.this, ContactList.class);
                startActivity(addIntent);
            }
        });
    }

    private void initialize() {
        btnAddEmployee = findViewById(R.id.btnAddEmployee);
        btnViewEmployee = findViewById(R.id.btnViewEmployee);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    { switch(item.getItemId()) {
        case R.id.profile:
            //add the function to perform here
            Intent intent = new Intent(HomeScreen.this, MyProfile.class);
            startActivity(intent);

            return(true);

        case R.id.logout:
            //add the function to perform here
            Intent intent1 = new Intent(HomeScreen.this, LoginActivity.class);
            editor.putBoolean("login", false);

            editor.apply();
            startActivity(intent1);
            finish();
            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }
}