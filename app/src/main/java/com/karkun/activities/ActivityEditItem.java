package com.karkun.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karkun.R;
import com.karkun.parts.AttachImageFromGallery;
import com.karkun.parts.UniqueDateAndTimePicker;
import com.karkun.recyclerViewAdapterAndRealmDatabase.KarkunData;

import io.realm.Realm;

public class ActivityEditItem extends AttachImageFromGallery {

    private static final String TAG = "Testing";
    EditText aei_txt_name, aei_txt_age, aei_txt_department, aei_txt_address, aei_txt_phone;
    TextView aei_txt_id;
    String name, department, age, phone, address, idPrimaryKey, picture, inCommingPicturePath;
    ImageView aei_imgage_view;

    Button aei_btn_change, aei_btn_cancel;
    Realm realmObject;
    private long date;
    KarkunData karkunData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        aei_txt_name = findViewById(R.id.aei_editText_name);
        aei_txt_id = findViewById(R.id.aei_editText_id);
        aei_txt_age = findViewById(R.id.aei_editText_age);
        aei_txt_department = findViewById(R.id.aei_editText_department);
        aei_txt_phone = findViewById(R.id.aei_editText_phonenumber);
        aei_txt_address = findViewById(R.id.aei_editText_address);
        aei_imgage_view = findViewById(R.id.aei_image_view);

        aei_imgage_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser(R.id.aei_image_view);
                Toast.makeText(ActivityEditItem.this, "Hello", Toast.LENGTH_SHORT).show();
            }
        });

        aei_btn_change = findViewById(R.id.aei_btn_change);
        aei_btn_cancel = findViewById(R.id.aei_btn_cancel);

        uniqueDateAndTimePicker = new UniqueDateAndTimePicker(this);
        date = uniqueDateAndTimePicker.uniqueDateAndTimePicker();

        name = getIntent().getStringExtra("name");
        department = getIntent().getStringExtra("department");
        age = getIntent().getStringExtra("age");
        phone = getIntent().getStringExtra("phone");
        address = getIntent().getStringExtra("address");
        idPrimaryKey = getIntent().getStringExtra("id");
        picture = getIntent().getStringExtra("picture");

        aei_btn_change.setOnClickListener(btnOnClickListener);
        aei_btn_cancel.setOnClickListener(btnOnClickListener);

        aei_txt_name.setText(name);
        aei_txt_id.setText(idPrimaryKey);
        aei_txt_age.setText(age);
        aei_txt_department.setText(department);
        aei_txt_phone.setText(phone);
        aei_txt_address.setText(address);

        if (picture == null) {
            Glide
                    .with(this)
                    .load(R.drawable.application_icon)
                    .apply(RequestOptions.circleCropTransform())
                    .into(aei_imgage_view);
        } else {
            try {
                Glide
                        .with(this)
                        .load(picture)
                        .apply(RequestOptions.circleCropTransform())
                        .into(aei_imgage_view);
            } catch (Exception e) {
                Toast.makeText(this, "Error loading Image!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private View.OnClickListener btnOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (mySDcardFilePath == null) {
                inCommingPicturePath = picture;
            } else {
                inCommingPicturePath = mySDcardFilePath;
            }

            switch (v.getId()) {
                case R.id.aei_btn_change:
                    try {
                        realmObject = Realm.getDefaultInstance();

                        karkunData = new KarkunData(Long.parseLong(aei_txt_id.getText().toString()),
                                aei_txt_name.getText().toString(),
                                aei_txt_age.getText().toString(),
                                aei_txt_address.getText().toString(),
                                aei_txt_department.getText().toString(),
                                aei_txt_phone.getText().toString(),
                                inCommingPicturePath,
                                date);

                        realmObject.beginTransaction();
                        realmObject.copyToRealmOrUpdate(karkunData);
                        realmObject.commitTransaction();
                        realmObject.close();
                        Toast.makeText(ActivityEditItem.this, "Change Successfully!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(ActivityEditItem.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    break;

                case R.id.aei_btn_cancel:
                    Toast.makeText(ActivityEditItem.this, "Cancel", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };

}
