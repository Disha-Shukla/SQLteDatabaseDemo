package com.example.databaseexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    EditText edtName, edtNumber, edtEmail, edtAddress;
    Button btnAddEmployee;
    DatabaseHandler db;
    ArrayList<Contact> ContacList, co;
    Bundle bundle;
    CircleImageView profileImg;
    Uri selectedImageURI;
    byte[] inputData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        // get action bar
        /*ActionBar actionBar = getActionBar();
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add Employee");*/

        db = new DatabaseHandler(this);
        ContacList = new ArrayList<>();

        if (getIntent() != null) {
            bundle = getIntent().getExtras();
            if (getIntent().hasExtra("name")) {
                edtName.setText(bundle.getString("name"));
                edtNumber.setText(bundle.getString("number"));
                edtEmail.setText(bundle.getString("email"));
                edtAddress.setText(bundle.getString("address"));
                profileImg.setImageBitmap(Utils.getImage(bundle.getByteArray("image")));
                //actionBar.setTitle("Update Employee");
                btnAddEmployee.setText("Update Employee");
            }
        }

        profileImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (checkAndRequestPermissions(MainActivity.this)) {
                    chooseImage(MainActivity.this);
                }
            }
        });


        Log.d("Insert: ", "Inserting ..");
        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getIntent() != null) {
                    if (!validateUsername() | !validateAddress() | !validatePhoneNo() | !validateEmail()) {
                        return;
                    } else {
                        if (getIntent().hasExtra("flag")) {
                            bundle = getIntent().getExtras();
                            if (bundle.getString("flag").
                                    equalsIgnoreCase("edit")) {
                                db.updateContact(new Contact
                                        (bundle.getInt("id", 0),
                                                edtName.getText().toString(),
                                                edtNumber.getText().toString(),
                                                edtEmail.getText().toString(),
                                                edtAddress.getText().toString()
                                                ));
                            }
                        } else {
                            InputStream iStream = null;
                            try {
                                iStream = getContentResolver().openInputStream(selectedImageURI);
                                inputData = Utils.getBytes(iStream);
                                Log.v("DS","222   "+inputData);
                                db.addContact
                                        (new Contact(edtName.getText().toString(),
                                                edtNumber.getText().toString(),
                                                edtEmail.getText().toString(),
                                                edtAddress.getText().toString(),
                                                inputData));
                            } catch (FileNotFoundException r){
                                r.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            /*db.addContact
                                    (new Contact(edtName.getText().toString(),
                                            edtNumber.getText().toString(),
                                            edtEmail.getText().toString(),
                                            edtAddress.getText().toString(),
                                            inputData));*/
                        }
                    }
                }
                Intent i = new Intent(MainActivity.this, ContactList.class);
                startActivity(i);
                finish();
            }
        });
    }

    private Boolean validateUsername() {
        String val = edtName.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            edtName.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
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

    private Boolean validateAddress() {
        String val = edtAddress.getText().toString();

        if (val.isEmpty()) {
            edtAddress.setError("Field cannot be empty");
            return false;
        } else {
            edtAddress.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = edtNumber.getText().toString();

        if (val.isEmpty()) {
            edtNumber.setError("Field cannot be empty");
            return false;
        } else if (val.length() < 10 || val.length() > 10) {
            edtNumber.setError("Mobile Number should be 10 digits long");
            return false;
        } else {
            edtNumber.setError(null);
            return true;
        }
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(MainActivity.this);
                }
                break;
        }
    }

    // function to let's the user to choose image from camera or gallery
    private void chooseImage(Context context) {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (optionsMenu[i].equals("Take Photo")) {
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (optionsMenu[i].equals("Choose from Gallery")) {
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else if (optionsMenu[i].equals("Exit")) {
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
                        profileImg.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                         selectedImageURI = data.getData();
                         Log.v("DS","uri"+selectedImageURI);
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImageURI != null) {
                            Cursor cursor = getContentResolver().query(selectedImageURI, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                profileImg.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void initialize() {
        edtName = findViewById(R.id.edtName);
        edtNumber = findViewById(R.id.edtNumber);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        btnAddEmployee = findViewById(R.id.btnAdd);
        profileImg = findViewById(R.id.profile_pic);
    }


}