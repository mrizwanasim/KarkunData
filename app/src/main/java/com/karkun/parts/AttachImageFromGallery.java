package com.karkun.parts;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karkun.R;

import java.io.File;

public class AttachImageFromGallery extends AppCompatActivity {

    private CopyFile copyFile;
    public UniqueDateAndTimePicker uniqueDateAndTimePicker;
    private static final int SELECT_PICTURE = 100;
    public String mySDcardFilePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uniqueDateAndTimePicker = new UniqueDateAndTimePicker(this);
    }

    /* Choose an image from Gallery */
    public void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
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
                    Glide
                            .with(getBaseContext())
                            .load(selectedImageUri)
                            .apply(RequestOptions.circleCropTransform())
                            .into((ImageView) findViewById(R.id.aa_image_view));
//                    ((ImageView) findViewById(R.id.aa_image_view)).setImageURI(selectedImageUri);
                    CopyFileFromSourseToDestination(data);
                }
            }
        }
    }

    private void CopyFileFromSourseToDestination(Intent data) {

        File destFile = new File(Environment.getExternalStorageDirectory().toString() + "/karkunAppData/" + uniqueDateAndTimePicker.uniqueDateAndTimePicker() + ".png");
        mySDcardFilePath = destFile.toString();
        Log.i("Testing", "CopyFileFromSourseToDestination: " + mySDcardFilePath);
        try {
            copyFile = new CopyFile(this,new File(getPathFromURI(data.getData())), destFile);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Testing", "onActivityResult: " + e);
            Toast.makeText(this, "Error in Coping", Toast.LENGTH_SHORT).show();
        }
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

}
