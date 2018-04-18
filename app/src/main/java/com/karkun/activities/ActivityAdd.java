package com.karkun.activities;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karkun.R;
import com.karkun.parts.AttachImageFromGallery;
import com.karkun.parts.CopyFile;
import com.karkun.parts.UniqueDateAndTimePicker;
import com.karkun.recyclerViewAdapterAndRealmDatabase.KarkunData;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import io.realm.Realm;

/**
 * Created by Windows on 1/02/2018.
 */

public class ActivityAdd extends AttachImageFromGallery {
    private static final String TAG = "Testing";
    private Realm realmObject;
    private KarkunData karkunData;
    private ImageButton aImageButton;
    private Button addImageButton, addKarkunButton;
    private String nameString, ageString, addressString, departmentString, phoneString;
    private long idLong;
    private EditText id, name, age, address, department, phone;
    private long date;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        aImageButton = (ImageButton) findViewById(R.id.aa_close_btn);
        addImageButton = (Button) findViewById(R.id.aa_add_image_btn);
        addKarkunButton = (Button) findViewById(R.id.aa_add_karkkun_btn);
        id = (EditText) findViewById(R.id.aa_id);
        name = (EditText) findViewById(R.id.aa_name);
        age = (EditText) findViewById(R.id.aa_age);
        address = (EditText) findViewById(R.id.aa_address);
        department = (EditText) findViewById(R.id.aa_department);
        phone = (EditText) findViewById(R.id.aa_phone);


        aImageButton.setOnClickListener(pressingOnClickListener);
        addImageButton.setOnClickListener(pressingOnClickListener);
        addKarkunButton.setOnClickListener(pressingOnClickListener);

        date = uniqueDateAndTimePicker.uniqueDateAndTimePicker();
    }


    private View.OnClickListener pressingOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.aa_close_btn: {
                    finish();
                    break;
                }

                case R.id.aa_add_karkkun_btn: {
                    try {

                        idLong = Long.parseLong(id.getText().toString());
                        nameString = name.getText().toString();
                        ageString = age.getText().toString();
                        addressString = address.getText().toString();
                        departmentString = department.getText().toString();
                        phoneString = phone.getText().toString();

                        if (nameString.trim().isEmpty() | ageString.trim().isEmpty() | addressString.trim().isEmpty()
                                | departmentString.trim().isEmpty() | phoneString.trim().isEmpty()) {
                            throw new IOException();
                        }

                        try {
                            realmObject = Realm.getDefaultInstance();
                            karkunData = new KarkunData(idLong, nameString, ageString, addressString, departmentString, phoneString, mySDcardFilePath, date);
                            realmObject.beginTransaction();
                            realmObject.copyToRealm(karkunData);
                            realmObject.commitTransaction();
                            realmObject.close();
                            Toast.makeText(ActivityAdd.this, "Data Successfully Entered!", Toast.LENGTH_SHORT).show();
                        }catch (Exception exception){
                            Toast.makeText(ActivityAdd.this, "Error: Duplicate ID", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception exception) {
                        Toast.makeText(ActivityAdd.this, "Error: Missing or Wrong Input", Toast.LENGTH_SHORT).show();
                    }finally {
                        finish();
                    }
                    break;
                }

                case R.id.aa_add_image_btn: {
                    openImageChooser();
                    break;
                }
            }
        }
    };

}
