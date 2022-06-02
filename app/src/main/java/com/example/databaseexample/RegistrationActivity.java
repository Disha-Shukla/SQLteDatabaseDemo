package com.example.databaseexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    EditText edtName, edtMobile, edtEmail, edtPswd, edtCPswd;
    Button btnRegister;
    TextView txtLogin;
    CircleImageView profilePic;
   // int CAMERA_PIC_REQUEST = 1;
   SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initialize();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateUsername() | !validatePassword() | !validatePhoneNo() | !validateEmail()){
                   return;
                }
                else{
                    Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                    editor.putString("name", edtName.getText().toString()); // Storing boolean - true/false
                    editor.putString("email", edtEmail.getText().toString()); // Storing string
                    editor.putString("number", edtMobile.getText().toString()); // Storing string
                    editor.putBoolean("registered",true);
                    editor.apply();
                    startActivity(i);
                    finish();
                }
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkAndRequestPermissions(RegistrationActivity.this)){
                    chooseImage(RegistrationActivity.this);
                }
            }
        });

    }

    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    1);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (ContextCompat.checkSelfPermission(RegistrationActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(RegistrationActivity   .this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(RegistrationActivity.this);
                }
                break;
        }
    }

    // function to let's the user to choose image from camera or gallery
    private void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        profilePic.setImageBitmap(selectedImage);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();

                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        editor.putString("image",encodedImage);
                        editor.apply();
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                profilePic.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                                Bitmap realImage = BitmapFactory.decodeFile(picturePath);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                realImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] b = baos.toByteArray();

                                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                                editor.putString("image",encodedImage);
                                editor.apply();
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void initialize() {
        edtName = findViewById(R.id.edtRName);
        edtMobile = findViewById(R.id.edtRMobile);
        edtEmail = findViewById(R.id.edtREmail);
        edtPswd = findViewById(R.id.edtRPassword);
        edtCPswd = findViewById(R.id.edtRConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);
        profilePic = findViewById(R.id.profile_image);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
    }

    private Boolean validateUsername() {
        String val = edtName.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            edtName.setError("Field cannot be empty");
            return false;
        }  else if (!val.matches(noWhiteSpace)) {
            edtName.setError("White Spaces are not allowed");
            return false;
        } else {
            edtName.setError(null);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = edtEmail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            edtEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            edtEmail.setError("Invalid email address");
            return false;
        } else {
            edtEmail.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = edtMobile.getText().toString();

        if (val.isEmpty()) {
            edtMobile.setError("Field cannot be empty");
            return false;
        } else if (val.length()<10 || val.length()>10) {
            edtMobile.setError("Mobile Number should be 10 digits long");
            return false;
        }

        else {
            edtMobile.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = edtPswd.getText().toString();
        String val2 = edtCPswd.getText().toString();
        String passwordVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            edtPswd.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            edtPswd.setError("Password is too weak");
            return false;
        }
        else if (!val.equals(val2)) {
            edtPswd.setError("Password and Confirm password doesnot match");
            return false;
        }else {
            edtPswd.setError(null);
            return true;
        }
    }
}