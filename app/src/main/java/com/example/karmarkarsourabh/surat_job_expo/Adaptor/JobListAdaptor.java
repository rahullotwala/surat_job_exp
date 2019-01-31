package com.example.karmarkarsourabh.surat_job_expo.Adaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.Modal.JobList.joblist;
import com.example.karmarkarsourabh.surat_job_expo.R;


public class JobListAdaptor extends RecyclerView.Adapter<JobListAdaptor.ViewHolder> {
    private Context context;
    private joblist joblist;

    public JobListAdaptor(Context context, joblist joblist) {
        this.context = context;
        this.joblist = joblist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.joblist_item,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.cmp_name.setText(joblist.getData().get(i).getCompany_name());
        viewHolder.post.setText(joblist.getData().get(i).getPost_name());
        viewHolder.job_pkg.setText(joblist.getData().get(i).getPackage_provided());
        viewHolder.decs.setText(joblist.getData().get(i).getPost_description());
    }

    @Override
    public int getItemCount() {
        return joblist.getData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cmp_name,post,job_pkg,decs;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cmp_name = (TextView) itemView.findViewById(R.id.JI_company_name);
            post = (TextView) itemView.findViewById(R.id.JI_post);
            job_pkg = (TextView) itemView.findViewById(R.id.JI_salary);
            decs = (TextView) itemView.findViewById(R.id.JI_jobdesc);
        }
    }
}
