package com.example.databaseexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter extends ArrayAdapter<Contact> {

    Context c;
    DatabaseHandler db;
    ArrayList<Contact> contactModelArrayList;

    public CustomAdapter(@NonNull Context context, ArrayList<Contact> contactModelArrayList) {
        super(context, 0, contactModelArrayList);
        this.c = context;
        this.contactModelArrayList = contactModelArrayList;
        db = new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_item, parent, false);
        }
        Contact contactModel = getItem(position);
        TextView empName = listitemView.findViewById(R.id.txtName);
        TextView empNumber = listitemView.findViewById(R.id.txtNumber);
        TextView empEmail = listitemView.findViewById(R.id.txtEmail);
        TextView empAddress = listitemView.findViewById(R.id.txtAddress);
        CircleImageView propic = listitemView.findViewById(R.id.profile_image);
        Button btnEdit = listitemView.findViewById(R.id.btnEdit);
        Button btnDelete = listitemView.findViewById(R.id.btnDelete);
        empName.setText(contactModel.get_name());
        empNumber.setText(contactModel.get_phone_number());
        empEmail.setText(contactModel.getEmail());
        empAddress.setText(contactModel.getAddress());
        Log.v("DS","111"+ Arrays.toString(contactModel.getImage()));
        if(contactModel.getImage()!=null){
        propic.setImageBitmap(Utils.getImage(contactModel.getImage()));
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //updateDialog();
                Intent i = new Intent(c, MainActivity.class);
               // Log.v("!!!!1", "" + contactModelArrayList.get(position).get_id());
                i.putExtra("flag", "edit");
                i.putExtra("id", contactModelArrayList.get(position).get_id());
                i.putExtra("name", "" + contactModelArrayList.get(position).get_name());
                i.putExtra("number", "" + contactModelArrayList.get(position).get_phone_number());
                i.putExtra("email", "" + contactModelArrayList.get(position).getEmail());
                i.putExtra("address", "" + contactModelArrayList.get(position).getAddress());
                i.putExtra("image", contactModelArrayList.get(position).getImage());
                c.startActivity(i);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog(position);
            }
        });
        return listitemView;
    }



    private void createDialog(final int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        db.deleteContact(new Contact
                                (contactModelArrayList.get(position).get_id(),
                                contactModelArrayList.get(position).get_name(), contactModelArrayList.get(position)._phone_number,
                                        contactModelArrayList.get(position).getEmail(), contactModelArrayList.get(position).getAddress()));
                        ((Activity) getContext()).finish();
                        Intent i = new Intent(c, ContactList.class);
                        getContext().startActivity(i);

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
