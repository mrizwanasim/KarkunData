package com.karkun.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karkun.R;
import com.karkun.recyclerViewAdapterAndRealmDatabase.KarkunData;
import com.karkun.recyclerViewAdapterAndRealmDatabase.RecyclerViewAdapter;

public class ActivityShowItem extends AppCompatActivity {

    private static final String TAG = "Testing";
    EditText asi_txt_name, asi_txt_age, asi_txt_department, asi_txt_address, asi_txt_phone;
    TextView asi_txt_id;
    String name, department, age, phone, address, id, picture;
    ImageView asi_imgage_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);
        asi_txt_name = findViewById(R.id.asi_editText_name);
        asi_txt_id = findViewById(R.id.asi_editText_id);
        asi_txt_age = findViewById(R.id.asi_editText_age);
        asi_txt_department = findViewById(R.id.asi_editText_department);
        asi_txt_phone = findViewById(R.id.asi_editText_phonenumber);
        asi_txt_address = findViewById(R.id.asi_editText_address);
        asi_imgage_view = findViewById(R.id.asi_image_view);


        name = getIntent().getStringExtra("name");
        department = getIntent().getStringExtra("department");
        age = getIntent().getStringExtra("age");
        phone = getIntent().getStringExtra("phone");
        address = getIntent().getStringExtra("address");
        id = getIntent().getStringExtra("id");
        picture = getIntent().getStringExtra("picture");

        asi_txt_name.setText(name);
        asi_txt_id.setText(id);
        asi_txt_age.setText(age);
        asi_txt_department.setText(department);
        asi_txt_phone.setText(phone);
        asi_txt_address.setText(address);

        if(picture == null)
        {
            Glide
                    .with(this)
                    .load(R.drawable.application_icon)
                    .apply(RequestOptions.circleCropTransform())
                    .into(asi_imgage_view);
        }else {
            try {
                Glide
                        .with(this)
                        .load(picture)
                        .apply(RequestOptions.circleCropTransform())
                        .into(asi_imgage_view);
            } catch (Exception e) {
                Toast.makeText(this, "Error loading Image!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
