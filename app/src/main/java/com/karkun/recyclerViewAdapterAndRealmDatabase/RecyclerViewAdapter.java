package com.karkun.recyclerViewAdapterAndRealmDatabase;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karkun.R;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Windows on 31/01/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements SwipeListener {

    int mPosition;
    private Realm mRealm;
    private RealmResults<KarkunData> mResults;
    Context context;

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch(which){
                case DialogInterface.BUTTON_POSITIVE:
                    mRealm.beginTransaction();
                    mResults.get(mPosition).deleteFromRealm();
                    mRealm.commitTransaction();
//                    notifyItemRemoved(mPosition);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    notifyDataSetChanged();
                    Toast.makeText(context, "Not Deleted!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public RecyclerViewAdapter(RealmResults<KarkunData> constractorResults, Context context, Realm realm) {
        mRealm = realm;
        update(constractorResults);
        this.context = context;
    }


    public void update(RealmResults<KarkunData> updateResults) {
        mResults = updateResults;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_item_in_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        KarkunData karkunData = mResults.get(position);
        holder.id.setText(Long.toString(karkunData.getId()));
        holder.name.setText(karkunData.getName());
        holder.department.setText(karkunData.getDepartment());
        holder.age.setText(karkunData.getAge());
        holder.address.setText(karkunData.getAddress());
        holder.phoneNumber.setText(karkunData.getPhoneNumber());
        try {
        Glide.with(context).load(karkunData.getPictures()).into(holder.pictures);
    }catch (Exception e){
            Toast.makeText(context, "Picture Load Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    @Override
    public void onSwipe(int position) {

        mPosition = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure to delete?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView id, name, age, address, department, phoneNumber;
        private ImageView pictures;
        public MyViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.riir_Karkun_id);
            name = (TextView) itemView.findViewById(R.id.riir_karkun_name);
            age = (TextView) itemView.findViewById(R.id.riir_karkun_age);
            address = (TextView) itemView.findViewById(R.id.riir_Karkun_address);
            department = (TextView) itemView.findViewById(R.id.riir_Karkun_department);
            phoneNumber = (TextView) itemView.findViewById(R.id.riir_Karkun_phonenumber);
            pictures =  (ImageView) itemView.findViewById(R.id.riir_karkun_image);
            pictures.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}