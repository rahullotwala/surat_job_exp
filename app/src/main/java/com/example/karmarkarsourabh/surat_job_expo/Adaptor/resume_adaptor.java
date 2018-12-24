package com.example.karmarkarsourabh.surat_job_expo.Adaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.Modal.resume.resume_md;
import com.example.karmarkarsourabh.surat_job_expo.R;
import com.squareup.picasso.Picasso;

public class resume_adaptor extends RecyclerView.Adapter<resume_adaptor.ViewHolder> {
    resume_md rm_modal;
    Context context;
    public  resume_adaptor(resume_md rm_modal, Context context) {
        this.rm_modal = rm_modal;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_resume, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.thm.setText(Html.fromHtml(rm_modal.getData().get(i).getTheme_name()));
        viewHolder.cat.setText(Html.fromHtml(rm_modal.getData().get(i).getTheme_category()));
        Picasso.get().load(rm_modal.getData().get(i).getTheme_prev()).into(viewHolder.cv);

    }

    @Override
    public int getItemCount() {
        return rm_modal.getData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView thm, cat;
        ImageView arrow, cv;
        public ViewHolder(View itemView) {
            super(itemView);
            thm = (TextView) itemView.findViewById(R.id.theme_tv);
            cat = (TextView) itemView.findViewById(R.id.cat_tv);
            arrow = (ImageView) itemView.findViewById(R.id.arrow);
            cv = (ImageView) itemView.findViewById(R.id.resume_previwe);
        }
    }
}

//https://drive.google.com/viewerng/viewer?url=