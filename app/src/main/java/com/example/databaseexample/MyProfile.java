package com.example.databaseexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends AppCompatActivity {

    TextView txtName, txtEmail, txtMobile, txtPswd;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    CircleImageView profilPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        txtName = findViewById(R.id.txtpName);
        txtEmail = findViewById(R.id.txtpEmail);
        txtMobile = findViewById(R.id.txtpMobile);
        txtPswd = findViewById(R.id.txtpPassword);
        profilPic = findViewById(R.id.profile_image);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        txtName.setText("Name: "+pref.getString("name",""));
        txtEmail.setText("Email Id: "+pref.getString("email",""));
        txtMobile.setText("Number: "+pref.getString("number",""));

        String previouslyEncodedImage = pref.getString("image", "");

        if( !previouslyEncodedImage.equalsIgnoreCase("") ){
            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            profilPic.setImageBitmap(bitmap);
        }
    }
}