package com.example.karmarkarsourabh.surat_job_expo.Adaptor;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
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

import com.example.karmarkarsourabh.surat_job_expo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

public class StudentHomeAdapter extends RecyclerView.Adapter<StudentHomeAdapter.ViewHolder> {

    private static final String TAG_JOB_FAIR_ID = "Job_fair_id";
    private static final String TAG_JOB_FAIR_NAME = "Job_fair_name";
    private static final String TAG_JOB_FAIR_START_DATE = "Job_fair_end_date";
    private static final String TAG_JOB_FAIR_END_DATE = "Job_fair_end_date";
    private static final String TAG_STUDENT_REG_START_DATE = "Student_registration_start_date";
    private static final String TAG_STUDENT_REG_END_DATE = "Student_registration_end_date";
    private String TAG_ISPARTICIPATED = "Isparticipated";
    private String TAG_MESSAGE="message";
    private String TAG_STUDENT_ID = "job_seeker_id";
    public static final String TAG_SUCEESS = "success";
    OkHttpClient client;
    String URL,BASE_URL;
    int student_id;

    private ArrayList<HashMap> listitem;
    HashMap<String,String> o;
    Context context;
    public StudentHomeAdapter(ArrayList<HashMap> listitems, Context context) {
        this.listitem = listitems;
        this.context = context;
    }
    @Override
    public StudentHomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.student_home_cardview_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final StudentHomeAdapter.ViewHolder holder, int position) {
o = (HashMap<String, String>) listitem.get(position);
        if(o.containsKey(TAG_MESSAGE)) {
            holder.campus_drive_name.setText(o.get(TAG_MESSAGE));
        }
        else {

            String job_fair_name = o.get(TAG_JOB_FAIR_NAME);

            String year = o.get(TAG_JOB_FAIR_START_DATE).substring(0, 4);

            student_id = Integer.parseInt(o.get(TAG_STUDENT_ID));
            holder.job_fair_end_date = o.get(TAG_JOB_FAIR_END_DATE);
            holder.student_reg_start_date = o.get(TAG_STUDENT_REG_START_DATE);
            holder.student_reg_end_date = o.get(TAG_STUDENT_REG_END_DATE);
            holder.campus_drive_name.setText(job_fair_name + " " + year);
            holder.job_fair_id = Integer.parseInt(o.get(TAG_JOB_FAIR_ID));

            holder.isParticipated=o.get(TAG_ISPARTICIPATED);

            Log.d("isParti",holder.isParticipated);

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            final String formattedDate = df.format(c);

            if (holder.job_fair_end_date.compareTo(formattedDate) < 1) {
                holder.job_fair_post_layout.setBackgroundColor(context.getResources().getColor(R.color.StudentDrawerFontColor));
            } else {
                holder.job_fair_post_layout.setBackgroundColor(context.getResources().getColor(R.color.transperent_card));
            }

            holder.jobfairdetails.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    Context wrapper = new ContextThemeWrapper(context, R.style.popupMenuStyle);
                    final PopupMenu popup = new PopupMenu(wrapper, v, Gravity.END);
                    popup.inflate(R.menu.jobfairdetailpopupmenu);
                    Log.d("Isparticipated",holder.isParticipated);
                    if (holder.student_reg_start_date.compareTo(formattedDate) > 0 || formattedDate.compareTo(holder.student_reg_end_date) > 0 || holder.isParticipated.equals("1")) {
                            popup.getMenu().removeItem(R.id.jobfairparticipate);
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.jobfairdetail:
//                                    Intent intent1 = new Intent(context, JobPostDetailActivity.class);
//                                    intent1.putExtra("jobfairid", holder.job_fair_id);
                                    //intent1.putExtra("student_id",student_id);
//                                    intent1.putExtra("jobfairname", holder.campus_drive_name.getText().toString());
//                                    context.startActivity(intent1);
                                    return true;

                                case R.id.jobfairparticipate:
                                    AlertDialog.Builder alert1 = new AlertDialog.Builder(context);
                                    alert1.setTitle("Participation");
                                    alert1.setMessage("Are you sure you want to participate?");
                                    alert1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    insertParticipatedStudent(holder.job_fair_id);
                                                }
                                            }
                                    );
                                    alert1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(context, "Okay", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    alert1.show();
                                    return true;
                            }

                            return true;
                        }
                    });
                    popup.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView campus_drive_name;
        public int job_fair_id;
        public String isParticipated;
        public String job_fair_end_date,student_reg_start_date,student_reg_end_date;
        public ConstraintLayout job_fair_post_layout;
        public ImageView jobfairdetails;
        public ViewHolder(View itemView) {
            super(itemView);
            campus_drive_name = (TextView) itemView.findViewById(R.id.textView_studentHome_Drivename);
            jobfairdetails = (ImageView)itemView.findViewById(R.id.imageView_studentHome_imageMenu);
            job_fair_post_layout = (ConstraintLayout)itemView.findViewById(R.id.constraintLayout_student_home);

        }

    }
    public void insertParticipatedStudent(int jobfairid)
    {
        BASE_URL = (context.getResources().getString(R.string.BASE_URL));
        client = new OkHttpClient();
        URL = BASE_URL+"student_participated_insert.php";
        RequestBody formbody = new FormBody.Builder()
                .add("job_seeker_id",Integer.toString(student_id))
                .add("job_fair_id",Integer.toString(jobfairid))
                .build();
        final Request req = new Request.Builder()
                .url(URL)
                .post(formbody)
                .build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e)
            {
                Log.d("Msg",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String rs = response.body().string();
                    Log.d("response",rs);
                    JSONObject jobj = new JSONObject(rs);
                    int success = jobj.getInt(TAG_SUCEESS);
                    String msg = jobj.getString("message");
                    Log.d("Status code",success+"");
                    Log.d("Status msg",msg);
                    if(success==1)
                    {
//                        Intent i = new Intent(context,StudentCampusParticipationActivity.class);
//                        context.startActivity(i);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

}
