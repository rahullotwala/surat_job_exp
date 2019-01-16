package com.example.karmarkarsourabh.surat_job_expo.Adaptor;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karmarkarsourabh.surat_job_expo.JobFairSingle;
import com.example.karmarkarsourabh.surat_job_expo.Modal.JobFair.jobfair;
import com.example.karmarkarsourabh.surat_job_expo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Archana on 10-10-2018.
 */

public class StudentJobFairAdapter extends RecyclerView.Adapter<StudentJobFairAdapter.ViewHolder>{

    jobfair mJobfair;
    transient Context context;
    public StudentJobFairAdapter(jobfair jfModal, Context context) {
        this.mJobfair = jfModal;
        this.context = context;
    }
    @Override
    public StudentJobFairAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.student_home_cardview_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final StudentJobFairAdapter.ViewHolder holder, final int position) {
        holder.dName.setText(Html.fromHtml(mJobfair.getData().get(position).getClg_name()));
        holder.sDate.setText(Html.fromHtml(mJobfair.getData().get(position).getStart_date()));
        holder.eDate.setText(Html.fromHtml(mJobfair.getData().get(position).getEnd_date()));

        if (mJobfair.getData().get(position).getIsOn()==1){
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.transperent_card));
        }else {
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.splash_back));

            holder.layout.setEnabled(false);

            holder.arrow.setEnabled(false);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intTentCall(position);
            }
        });
        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intTentCall(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mJobfair.getData().size();
    }

   private void intTentCall(int position){

        Intent mintent = new Intent(context,JobFairSingle.class);
        mintent.putExtra("obj", (Serializable) mJobfair);
        mintent.putExtra("pos",position);
        context.startActivity(mintent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView sDate, eDate, dName;
        ConstraintLayout layout;
        ImageView arrow;
        public ViewHolder(View iV) {
            super(iV);
            sDate = (TextView) iV.findViewById(R.id.JFstart_date);
            eDate = (TextView) iV.findViewById(R.id.JFend_date);
            dName = (TextView) iV.findViewById(R.id.textView_studentHome_Drivename);
            layout = (ConstraintLayout) iV.findViewById(R.id.constrain_bottom_bar);
            arrow = (ImageView) iV.findViewById(R.id.imageView_studentHome_imageMenu);
        }

    }

}
