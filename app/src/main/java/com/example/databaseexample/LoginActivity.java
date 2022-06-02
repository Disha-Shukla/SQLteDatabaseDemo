package com.example.databaseexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    TextView txtRegister;
    Button btnLogin;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    Log.v("DS",""+pref.getString("email",""));
                    if(pref.getString("email","").equalsIgnoreCase(edtEmail.getText().toString())) {
                        Intent i = new Intent(LoginActivity.this, HomeScreen.class);
                        editor.putBoolean("login", true);
                        editor.apply();
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Email Id not registered", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initialize() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        txtRegister = findViewById(R.id.txtRegister);
        btnLogin = findViewById(R.id.btnLogin);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

    }

    public boolean validate() {
        boolean valid = true;

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("enter a valid email address");
            valid = false;
        } else {
            edtEmail.setError(null);
        }
        if (password.isEmpty()) {
            edtPassword.setError("password cannot be empty");
            valid = false;
        } else {
            edtPassword.setError(null);
        }
        return valid;
    }
}