package com.example.karmarkarsourabh.surat_job_expo.Adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lenovo on 10-26-2018.
 */

public class College_Course_Manage_Detail_Adapter_Fragment_Company extends RecyclerView.Adapter<College_Course_Manage_Detail_Adapter_Fragment_Company.ViewHolder> {

    private ArrayList<HashMap> listitem;
    Context context;
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_Company_Name = "Company_name";
    private static final String TAG_Company_Adress = "Company_address";
    private static final String TAG_Company_webURL = "Company_webURL";

    private static final String TAG_SUCCESS = "success";

    public College_Course_Manage_Detail_Adapter_Fragment_Company(ArrayList<HashMap> listitems, Context context) {
        this.listitem = listitems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.college_course_manage_detail_for_company_cardview_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {



        HashMap<String,String> o = (HashMap<String, String>) listitem.get(position);
        if(o.containsKey(TAG_MESSAGE))
        {
            holder.College_Name.setText(o.get(TAG_MESSAGE));
            holder.imgvwcolnm.setVisibility(View.INVISIBLE);
            holder.imgvwweburl.setVisibility(View.INVISIBLE);
            holder.imgvwlocation.setVisibility(View.INVISIBLE);
            holder.address.setVisibility(View.INVISIBLE);
            holder.weburl.setVisibility(View.INVISIBLE);



        }
        else {


            holder.College_Name.setText(o.get(TAG_Company_Name));
            holder.address.setText(o.get(TAG_Company_Adress));

            holder.weburl.setText(o.get(TAG_Company_webURL));
        }

    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView College_Name;
        public TextView address;
        public TextView weburl;
        public ImageView imgvwcolnm;
        public ImageView imgvwweburl;
        public ImageView imgvwlocation;


        public ViewHolder(View itemView) {
            super(itemView);
            College_Name = (TextView)itemView.findViewById(R.id.textview_college_course_manage_detail_for_company_and_college_cardview_layout_name);
            address = (TextView)itemView.findViewById(R.id.textview_college_course_manage_detail_for_company_and_college_cardview_layout_location);

            weburl = (TextView)itemView.findViewById(R.id.textview_college_course_manage_detail_for_company_and_college_cardview_layout_weburl);

            imgvwcolnm=(ImageView) itemView.findViewById(R.id.imageview_college_course_manage_detail_for_company_and_college_cardview_layout_name);
            imgvwlocation=(ImageView) itemView.findViewById(R.id.imageview_college_course_manage_detail_for_company_and_college_cardview_layout_location);
            imgvwweburl=(ImageView) itemView.findViewById(R.id.imageview_college_course_manage_detail_for_company_and_college_cardview_layout_weburl);
        }

    }



}
