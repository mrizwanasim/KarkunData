package com.karkun.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karkun.R;
import com.karkun.parts.CopyFile;
import com.karkun.parts.GetPermissions;
import com.karkun.recyclerViewAdapterAndRealmDatabase.KarkunData;
import com.karkun.recyclerViewAdapterAndRealmDatabase.MyRecyclerView;
import com.karkun.recyclerViewAdapterAndRealmDatabase.RecyclerViewAdapter;
import com.karkun.recyclerViewAdapterAndRealmDatabase.SimpleTouchCallback;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

import javax.security.auth.login.LoginException;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class ActivityMain extends AppCompatActivity implements RecyclerViewAdapter.RecyclerViewItemSelectInterface {

    private static final String TAG = "Testing";

    private Realm realmObjcet;
    private File pathBackup;
    private Toolbar mToolbar;
    private ImageView mImageView;
    private MyRecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RealmResults<KarkunData> karkunResults;
    private Button el_add_button, el_btn_restore;
    private View mEmptyView;
    private GetPermissions getPermissions;
    private CopyFile copyFile;
    AlertDialog alertDialog;


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
        getPermissions = new GetPermissions(this);
        getPermissions.myAppPermissions();

        mImageView = (ImageView) findViewById(R.id.mbackground);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(R.drawable.background).into(mImageView);
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        el_add_button = (Button) findViewById(R.id.el_add_karkun);
        el_btn_restore = findViewById(R.id.el_btn_restore);
        mEmptyView = findViewById(R.id.empty_layout);
        mRecyclerView = (MyRecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.hideIfEmpty(mToolbar);
        mRecyclerView.showIfEmpty(mEmptyView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        realmObjcet = Realm.getDefaultInstance();
        karkunResults = realmObjcet.where(KarkunData.class).sort("date", Sort.ASCENDING).findAllAsync();
        mAdapter = new RecyclerViewAdapter(karkunResults, this, realmObjcet, this);
        mRecyclerView.setAdapter(mAdapter);
        el_btn_restore.setOnClickListener(onClickListener);
        el_add_button.setOnClickListener(onClickListener);

        SimpleTouchCallback callback = new SimpleTouchCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.el_add_karkun:
                    getPermissions.myAppPermissions();
                    if (getPermissions.checkPermission()) {

                        File newFile = new File(Environment.getExternalStorageDirectory().toString() + "/karkunAppData");
                        if (!newFile.exists()) {
                            newFile.mkdir();
                        }

                        Toast.makeText(getApplicationContext(), "Good", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ActivityAdd.class);
                        startActivity(intent);
                    }
                    break;

                case R.id.el_btn_restore:
                    getPermissions.myAppPermissions();
                    pathBackup = new File(realmObjcet.getPath());
                    Log.i(TAG, "PathBackup: " + pathBackup);
                    File newFile2 = new File(Environment.getExternalStorageDirectory().toString() + "/karkunAppData/BackupAndRestore/" + "karkunData.realm");
                    Log.i(TAG, "newFile: " + newFile2);
                    try {
                        copyFile = new CopyFile(ActivityMain.this);
                        copyFile.copyFile(newFile2, pathBackup);
                        alertDialog = new AlertDialog.Builder(ActivityMain.this).create();
                        alertDialog.setTitle("RESTART!");
                        alertDialog.setMessage("Please, Atart App Manually, Thanks");
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        System.exit(0);
                                    }
                                });
                    } catch (IOException e) {
                        alertDialog = new AlertDialog.Builder(ActivityMain.this).create();
                        alertDialog.setTitle("BACKUP PROBLEM!");
                        alertDialog.setMessage("BACKUP FILE NOT EXIST");
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                    }

                    alertDialog.show();
                    break;
            }
        }
    };

//||||||||||||||||||||||||||||||||||||Showing menu on Toolbar|||||||||||||||||||||||||||||||||||||||||||||||||||||||||

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.toolbar_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String search) {
                mAdapter.getFilter().filter(search);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String search) {
                mAdapter.getFilter().filter(search);
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
        switch (id) {
            case R.id.toolbar_search:
                Toast.makeText(this, "Searching", Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_add:
                Intent intent = new Intent(this, ActivityAdd.class);
                startActivity(intent);
                break;
            case R.id.ascending_order:
                realmObjcet = Realm.getDefaultInstance();
                karkunResults = realmObjcet.where(KarkunData.class).sort("name", Sort.ASCENDING).findAllAsync();
                mAdapter = new RecyclerViewAdapter(karkunResults, this, realmObjcet, this);
                mRecyclerView.setAdapter(mAdapter);
                karkunResults.addChangeListener(myChangeListener);
                Toast.makeText(this, "Ascending Order", Toast.LENGTH_SHORT).show();
                break;
            case R.id.descending_order:
                realmObjcet = Realm.getDefaultInstance();
                karkunResults = realmObjcet.where(KarkunData.class).sort("name", Sort.DESCENDING).findAllAsync();
                mAdapter = new RecyclerViewAdapter(karkunResults, this, realmObjcet, this);
                mRecyclerView.setAdapter(mAdapter);
                karkunResults.addChangeListener(myChangeListener);
                Toast.makeText(this, "Descending Order", Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_backingUp:
                boolean noErrorflag = true;
                getPermissions.myAppPermissions();
                pathBackup = new File(realmObjcet.getPath());
                Log.i(TAG, "PathBackup: " + pathBackup);
                File newFile = new File(Environment.getExternalStorageDirectory().toString() + "/karkunAppData/BackupAndRestore/" + "karkunData.realm");
                Log.i(TAG, "newFile: " + newFile);
                try {
                    copyFile = new CopyFile(this);
                    copyFile.copyFile(pathBackup, newFile);

                } catch (IOException e) {
                    noErrorflag = false;
                    Toast.makeText(this, "Error while Backing Up!", Toast.LENGTH_SHORT).show();
                }
                if (noErrorflag) {
                    Toast.makeText(this, "Backing Up....", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.toolbar_backupRestore:
                getPermissions.myAppPermissions();
                pathBackup = new File(realmObjcet.getPath());
                Log.i(TAG, "PathBackup: " + pathBackup);
                File newFile2 = new File(Environment.getExternalStorageDirectory().toString() + "/karkunAppData/BackupAndRestore/" + "karkunData.realm");
                Log.i(TAG, "newFile: " + newFile2);
                try {
                    copyFile = new CopyFile(this);
                    copyFile.copyFile(newFile2, pathBackup);
                    alertDialog = new AlertDialog.Builder(ActivityMain.this).create();
                    alertDialog.setTitle("RESTART!");
                    alertDialog.setMessage("Please, Atart App Manually, Thanks");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    System.exit(0);
                                }
                            });
                } catch (IOException e) {
                    alertDialog = new AlertDialog.Builder(ActivityMain.this).create();
                    alertDialog.setTitle("BACKUP PROBLEM!");
                    alertDialog.setMessage("BACKUP FILE NOT EXIST");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                }

                alertDialog.show();
                break;
            case R.id.toolbar_info:
                Toast.makeText(this, "Karkun Software House", Toast.LENGTH_LONG).show();
                break;
            case R.id.toolbar_quit:
                Toast.makeText(this, "Quiting", Toast.LENGTH_SHORT).show();
                System.exit(0);
                break;
            case 16908332:
                Toast.makeText(this, "<Going Back>", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(KarkunData karkunData) {
        long id;
        id = karkunData.getId();
        Intent intent = new Intent(this, ActivityShowItem.class);
        intent.putExtra("id", Long.toString(id));
        intent.putExtra("name", karkunData.getName());
        intent.putExtra("department", karkunData.getDepartment());
        intent.putExtra("age", karkunData.getAge());
        intent.putExtra("phone", karkunData.getPhoneNumber());
        intent.putExtra("address", karkunData.getAddress());

        intent.putExtra("picture", karkunData.getPictures());

        startActivity(intent);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mAdapter.update(karkunResults);
    }
}
