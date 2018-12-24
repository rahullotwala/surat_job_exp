package com.example.karmarkarsourabh.surat_job_expo.Adaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lenovo on 10-30-2018.
 */

public class StudentCompanySlectedListAdapter extends RecyclerView.Adapter<StudentCompanySlectedListAdapter.ViewHolder>{



    private ArrayList<HashMap> listitem;
    Context context;

    private static final String TAG_Company_Name = "Company_name";
    private static final String TAG_Company_logo = "Company_logo";

    private static final String TAG_SUCCESS = "success";

    public StudentCompanySlectedListAdapter(ArrayList<HashMap> listitems, Context context) {
        this.listitem = listitems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.student_company_seleted_cardview_layout,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HashMap<String,String> o = (HashMap<String, String>) listitem.get(position);
        holder.Company_Name.setText(o.get(TAG_Company_Name));



    }

//    @Override
//    public void onBindViewHolder(College_Home_Adapter.ViewHolder holder, final int position) {
//        HashMap<String,String> o = (HashMap<String, String>) listitem.get(position);
//
//        holder.Host_College_Name.setText(o.get(TAG_Host_College_Name));
//        holder.Total_Company.setText(o.get(TAG_Total_Company_Count));
//        holder.Total_College.setText(o.get(TAG_Total_College_Count));
//        holder.Total_Student.setText(o.get(TAG_Total_Student_Count));
//
//        holder.buttonDetail.setOnClickListener(new View.OnClickListener() {
//
//
//            // courselist.get(position);
//            @Override
//            public void onClick(View v) {
//
//                HashMap<String,String> o = (HashMap<String, String>) listitem.get(position);
//                Intent i=new Intent(context,College_Course_Manage_Detail_TabBar_Activity.class);
//                i.putExtra("Job_fair_id",o.get(TAG_job_fair_id));
//
//                context.startActivity(i);
//
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView Company_Name;


        public ViewHolder(View itemView) {
            super(itemView);

            Company_Name = (TextView)itemView.findViewById(R.id.textView_student_company_selected_cardview_layout_companyName);


           }

    }








    }
