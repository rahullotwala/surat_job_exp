package com.example.karmarkarsourabh.surat_job_expo.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lenovo on 10-23-2018.
 */

public class College_Home_Adapter extends RecyclerView.Adapter<College_Home_Adapter.ViewHolder>{


    private ArrayList<HashMap> listitem;
    Context context;

    private static final String TAG_Host_College_Name = "Colgnm";
    private static final String TAG_job_fair_id = "job_fair_id";
    private static final String TAG_Total_Company_Count = "companycount";
    private static final String TAG_Total_Student_Count = "studentcount";
    private static final String TAG_Total_College_Count = "collegecount";
    private static final String TAG_SUCCESS = "success";

    public College_Home_Adapter(ArrayList<HashMap> listitems, Context context) {
        this.listitem = listitems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.college_home_cardview_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        HashMap<String,String> o = (HashMap<String, String>) listitem.get(position);

       holder.Host_College_Name.setText(o.get(TAG_Host_College_Name));
       holder.Total_Company.setText(o.get(TAG_Total_Company_Count));
       holder.Total_College.setText(o.get(TAG_Total_College_Count));
       holder.Total_Student.setText(o.get(TAG_Total_Student_Count));

holder.buttonDetail.setOnClickListener(new View.OnClickListener() {


   // courselist.get(position);
    @Override
    public void onClick(View v) {

        HashMap<String,String> o = (HashMap<String, String>) listitem.get(position);
//        Intent i=new Intent(context,College_Course_Manage_Detail_TabBar_Activity.class);
//        i.putExtra("Job_fair_id",o.get(TAG_job_fair_id));

//        context.startActivity(i);

    }
});
    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView Host_College_Name;
        public TextView Total_Student;
        public TextView Total_College;
        public TextView Total_Company;
        public Button buttonDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            Host_College_Name = (TextView)itemView.findViewById(R.id.textView_collegeHome_driveName);
            Total_Student = (TextView)itemView.findViewById(R.id.textView_collegeHome_totalStudents);

            Total_College = (TextView)itemView.findViewById(R.id.textView_collegeHome_totalCollege);
            Total_Company = (TextView)itemView.findViewById(R.id.textView_collegeHome_totalCompanies);

            buttonDetail=(Button) itemView.findViewById(R.id.button_collegeHome_details);
        }

    }
}
