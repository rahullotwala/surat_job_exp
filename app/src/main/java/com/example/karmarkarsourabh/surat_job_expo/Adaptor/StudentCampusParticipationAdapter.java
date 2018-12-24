package com.example.karmarkarsourabh.surat_job_expo.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.R;
import com.example.karmarkarsourabh.surat_job_expo.StudentCompanyNotSelectedActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Archana on 10/24/2018.
 */

public class StudentCampusParticipationAdapter extends RecyclerView.Adapter<StudentCampusParticipationAdapter.MyViewHolder> {

    Context context;
    ArrayList<HashMap> campusList;
    private String TAG_JOB_FAIR_ID="job_fair_id";
    private String TAG_JOB_FAIR_START_DATE="job_fair_start_date";
    private String TAG_JOB_FAIR_END_DATE="job_fair_end_date";
    private String TAG_COLLEGE_NAME="college_name";
    private String TAG_MESSAGE="message";

    public StudentCampusParticipationAdapter(Context context, ArrayList<HashMap> campusList) {
        this.context = context;
        this.campusList = campusList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(context).inflate(R.layout.student_campus_participation_cardview_layout,null);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

       final HashMap<String,String> obj=campusList.get(position);
       if(obj.containsKey(TAG_MESSAGE)) {
            holder.drivename.setText(obj.get(TAG_MESSAGE));
       }
       else {
           String clg = obj.get(TAG_COLLEGE_NAME);

           String year = obj.get(TAG_JOB_FAIR_START_DATE).substring(0, 4);

           holder.drivename.setText(clg + " " + year);
           holder.drivename.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent companyNotSelected = new Intent(context, StudentCompanyNotSelectedActivity.class);
                   companyNotSelected.putExtra(TAG_JOB_FAIR_ID,obj.get(TAG_JOB_FAIR_ID));
                   companyNotSelected.putExtra(TAG_COLLEGE_NAME,obj.get(TAG_COLLEGE_NAME));
                   context.startActivity(companyNotSelected);
               }
           });
       }
    }

    @Override
    public int getItemCount() {
        return campusList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView drivename;
        public MyViewHolder(View itemView) {
            super(itemView);
            drivename=itemView.findViewById(R.id.textView_studentParticipation_Drivename);
        }
    }
}
