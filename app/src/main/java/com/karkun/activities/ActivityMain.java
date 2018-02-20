package com.karkun.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karkun.R;
import com.karkun.recyclerViewAdapterAndRealmDatabase.KarkunData;
import com.karkun.recyclerViewAdapterAndRealmDatabase.MyRecyclerView;
import com.karkun.recyclerViewAdapterAndRealmDatabase.RecyclerViewAdapter;
import com.karkun.recyclerViewAdapterAndRealmDatabase.SimpleTouchCallback;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ActivityMain extends AppCompatActivity {

    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]
            {Manifest.permission.WRITE_EXTERNAL_STORAGE,
             Manifest.permission.READ_EXTERNAL_STORAGE};

    private Realm realmObjcet;

    private Toolbar mToolbar;
    private ImageView mImageView;
    private MyRecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RealmResults<KarkunData> karkunResults;
    private Button el_add_button;
    private View mEmptyView;

    @Override
    protected void onStart() {
        super.onStart();
        karkunResults.addChangeListener(myChangeListener);
    }

    private RealmChangeListener myChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            mAdapter.update(karkunResults);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        karkunResults.removeChangeListener(myChangeListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyAppPermissions();

        mImageView = (ImageView) findViewById(R.id.mbackground);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(R.drawable.background).into(mImageView);
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);

        el_add_button = (Button) findViewById(R.id.el_add_karkun);
        mEmptyView = findViewById(R.id.empty_layout);

        mRecyclerView = (MyRecyclerView) findViewById(R.id.mRecyclerView);


        mRecyclerView.hideIfEmpty(mToolbar);
        mRecyclerView.showIfEmpty(mEmptyView);





        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        realmObjcet = Realm.getDefaultInstance();
        karkunResults = realmObjcet.where(KarkunData.class).findAllAsync();

        mAdapter = new RecyclerViewAdapter(karkunResults, this, realmObjcet);
        mRecyclerView.setAdapter(mAdapter);

        el_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityAdd.class);
                startActivity(intent);
            }
        });

        SimpleTouchCallback callback = new SimpleTouchCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }




//||||||||||||||||||||||||||||||||||||Showing menu on Toolbar|||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem item = menu.findItem(R.id.toolbar_search);
        SearchView searchView  = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("Testing", "onQueryTextSubmit: " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("Testing", "onQueryTextChange: " + newText );
                if(!newText.isEmpty()) {
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

                    realmObjcet = Realm.getDefaultInstance();

                    karkunResults = realmObjcet.where(KarkunData.class).equalTo("name", newText).findAll();
                    if(!karkunResults.isEmpty()){
                        mAdapter = new RecyclerViewAdapter(karkunResults, getApplicationContext(), realmObjcet);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }
                return false;
            }
        });

        return true;
    }
//||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

//|||||||||||||||||||||||||||||||||||||Showing menu Icon Listeners||||||||||||||||||||||||||||||||||||||||||||||||||||
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id){
                case R.id.toolbar_search:
                    Toast.makeText(this, "Searching", Toast.LENGTH_LONG).show();
                    break;

                case R.id.toolbar_add:
                    Intent intent = new Intent(this,ActivityAdd.class);
                    startActivity(intent);
                    break;

                case R.id.toolbar_info:
                    Toast.makeText(this, "Karkun Software House", Toast.LENGTH_LONG).show();
                    break;

                case R.id.toolbar_quit:
                    Toast.makeText(this, "Quiting", Toast.LENGTH_SHORT).show();
                    System.exit(0);
                    break;
            }
        return super.onOptionsItemSelected(item);
    }
//||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||




    //||||||||||||||||||||||||||||||||||||||||||||||||||GETTING PERMISSIONS||||||||||||||||||||||||||||||||||||||||||||||||
    private void MyAppPermissions() {

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        if(ActivityCompat.checkSelfPermission(ActivityMain.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(ActivityMain.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(ActivityMain.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(ActivityMain.this,permissionsRequired[1])){

                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);

                builder.setTitle("Need Multiple Permissions");

                builder.setMessage("This app needs Write and Read External Storage.");

                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                        ActivityCompat.requestPermissions(ActivityMain.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                builder.show();

            } else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {

                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);

                builder.setTitle("Need Multiple Permissions");

                builder.setMessage("This app needs Write and Read External Storage.");

                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                        sentToSettings = true;

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                        Uri uri = Uri.fromParts("package", getPackageName(), null);

                        intent.setData(uri);

                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);

                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Write and Read External Storage", Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                builder.show();

            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(ActivityMain.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }


            SharedPreferences.Editor editor = permissionStatus.edit();

            editor.putBoolean(permissionsRequired[0],true);

            editor.commit();
        } else {

            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        mAdapter.update(karkunResults);
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(ActivityMain.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
       Toast toast = Toast.makeText(getBaseContext(), "We got All Permissions", Toast.LENGTH_LONG);
       toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
       toast.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }
            if(allgranted){
                proceedAfterPermission();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(ActivityMain.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(ActivityMain.this,permissionsRequired[1])){

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Write and Read External Storage.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ActivityMain.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }
    }
//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
}
