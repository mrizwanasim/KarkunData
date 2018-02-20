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

import com.karkun.R;
import com.karkun.recyclerViewAdapterAndRealmDatabase.KarkunData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Calendar;

import io.realm.Realm;

/**
 * Created by Windows on 1/02/2018.
 */

public class ActivityAdd extends AppCompatActivity {

    private Realm realmObject;
    private KarkunData karkunData;
    private ImageButton aImageButton;
    private Button addImageButton, addKarkunButton;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private String mySDcardFilePath, nameString, ageString, addressString, departmentString, phoneString;
    private long idLong;
    private EditText id, name, age, address, department, phone;
    private static final int SELECT_PICTURE = 100;


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
                            karkunData = new KarkunData(idLong, nameString, ageString, addressString, departmentString, phoneString, mySDcardFilePath);
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

    }


    /* Choose an image from Gallery */
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri

                    String path = getPathFromURI(selectedImageUri);
                    Log.i("Testing", "Image Path : " + path);
                    // Set the image in ImageView
                    ((ImageView) findViewById(R.id.aa_image_view)).setImageURI(selectedImageUri);
                    CopyFileFromSourseToDestination(data);
                }
            }
        }
    }

    private void CopyFileFromSourseToDestination(Intent data) {

        File destFile = new File(Environment.getExternalStorageDirectory().toString() + "/karkunAppData/" + uniqueDateAndTimePicker() + ".png");
        mySDcardFilePath = destFile.toString();
        Log.i("Testing", "CopyFileFromSourseToDestination: " + mySDcardFilePath);
        try {
            copyFile(new File(getPathFromURI(data.getData())), destFile);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Testing", "onActivityResult: " + e);
            Toast.makeText(this, "Error in Coping", Toast.LENGTH_SHORT).show();
        }
    }

    private long uniqueDateAndTimePicker() {
        datePicker = new DatePicker(this);
        timePicker = new TimePicker(this);

        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
        long startTime = calendar.getTimeInMillis();
        return startTime;
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        for (int i = 0; i == proj.length; i++) {
        }

        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        Log.i("Testing", "copyFile : " + sourceFile.toString());
        Log.i("Testing", "copyFile : " + destFile.toString());
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }
}
