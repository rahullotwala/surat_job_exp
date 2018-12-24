package com.example.karmarkarsourabh.surat_job_expo.Adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.R;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;

/**
 * Created by Keyuri on 10-29-2018.
 */

public class StudentSelectedCompanyPriorityAdapter extends RecyclerView.Adapter<StudentSelectedCompanyPriorityAdapter.ViewHolder>{


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_STUDENT_PRIORITY_ID= "Student_priority_id";
    private static final String TAG_COMPANY_NAME= "Company_name";
    private static final String TAG_COMPANY_ADDRESS= "Company_address";
    private static final String TAG_STUDENT_PRIORITY= "Priority";
    OkHttpClient client;
    String URL,BASE_URL;
    private ArrayList<HashMap> listitem;
    HashMap<String,String> o;
    Context context;
    public StudentSelectedCompanyPriorityAdapter(ArrayList<HashMap> listitems, Context context) {
        this.listitem = listitems;
        this.context = context;
    }
    @Override
    public StudentSelectedCompanyPriorityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.student_company_selection_priority_cardview_layout,null);
        return new StudentSelectedCompanyPriorityAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final StudentSelectedCompanyPriorityAdapter.ViewHolder holder, int position) {
        o = (HashMap<String, String>) listitem.get(position);
        holder.CompanyName.setText(o.get(TAG_COMPANY_NAME));
        holder.CompanyLocation.setText(o.get(TAG_COMPANY_ADDRESS));

    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView CompanyName, CompanyLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            CompanyName = (TextView) itemView.findViewById(R.id.textview_studentCompanySelectionPriorityCardview_companyName);
            CompanyLocation = (TextView) itemView.findViewById(R.id.textview_studentCompanySelectionPriorityCardview_companyLocation);
        }


    }

}
