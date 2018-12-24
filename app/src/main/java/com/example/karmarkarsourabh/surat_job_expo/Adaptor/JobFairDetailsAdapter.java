package com.example.karmarkarsourabh.surat_job_expo.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.R;
import com.example.karmarkarsourabh.surat_job_expo.StudentJobPostDetailsActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Archana on 10-15-2018.
 */

public class JobFairDetailsAdapter extends RecyclerView.Adapter<JobFairDetailsAdapter.ViewHolder>{


    private ArrayList<HashMap> listitem;
    Context context;
    private static final String TAG_COMPANY_JOB_POST_ID = "Company_job_post_id";
    private static final String TAG_COMPANY_NAME = "Company_name";
    private static final String TAG_POST_NAME = "Post_name";
    private static final String TAG_COMPANY_WEBURL = "Company_webURL";
    private static final String TAG_COMPANY_ADDRESS = "Company_address";
    private static final String TAG_POST_DESC = "Post_description";
    private static final String TAG_EXPERIENCE_REQUIRED = "Experience_required";
    private static final String TAG_TOTAL_PEOPLE_APPLIED = "Total_people";

    public JobFairDetailsAdapter(ArrayList<HashMap> listitems, Context context) {
        this.listitem = listitems;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.job_post_details_cardview_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String,String> o = (HashMap<String, String>) listitem.get(position);
        holder.company_name.setText(o.get(TAG_COMPANY_NAME));
        holder.company_weburl.setText(o.get(TAG_COMPANY_WEBURL));

        holder.company_location.setText(o.get(TAG_COMPANY_ADDRESS));
        holder.experiance_wanted.setText(o.get(TAG_EXPERIENCE_REQUIRED));
        holder.company_post.setText(o.get(TAG_POST_NAME));
        holder.total_people.setText(o.get(TAG_TOTAL_PEOPLE_APPLIED));
        final String company_job_post_id = o.get(TAG_COMPANY_JOB_POST_ID);
        holder.job_post_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,StudentJobPostDetailsActivity.class);
                intent.putExtra("company_job_post_id",company_job_post_id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView company_name;
        public TextView company_weburl;
        public TextView company_location;
        public TextView experiance_wanted;
        public TextView company_post;
        public TextView total_people;
        public RelativeLayout job_post_details;

        public ViewHolder(View itemView) {
            super(itemView);
            company_name = (TextView) itemView.findViewById(R.id.textView_job_post_detail_companyName);
            company_weburl = (TextView) itemView.findViewById(R.id.textView_job_post_detail_companyWeburl);
            company_location = (TextView) itemView.findViewById(R.id.textView_job_post_detail_companyLocation);
            experiance_wanted = (TextView) itemView.findViewById(R.id.textView_job_post_detail_ExperianceWanted);
            company_post = (TextView) itemView.findViewById(R.id.textView_job_post_detail_companyPostname);
            total_people = (TextView) itemView.findViewById(R.id.textView_job_post_detail_peopleApplied);
            job_post_details = (RelativeLayout)itemView.findViewById(R.id.cardView_job_fair_details);
        }
    }
}
