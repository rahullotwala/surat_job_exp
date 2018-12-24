package com.example.karmarkarsourabh.surat_job_expo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.karmarkarsourabh.surat_job_expo.Adaptor.StudentCampusParticipationAdapter;
import com.example.karmarkarsourabh.surat_job_expo.Utill.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StudentCampusParticipationActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    Session session;
    ArrayList<HashMap> campusList;
    String URL="",URL_STUDENT="",BASE_URL,Image_uri;
    OkHttpClient client;
    ImageView studentProfile;
    TextView studentName,studentEmail;
    String sprofile="",sname="",semail="";
    View v;
    ProgressDialog pd;
    StudentCampusParticipationAdapter adapter;

    //Json node names

    private String ARRAY_NAME="campusDetail";
    private String TAG_JOB_FAIR_ID="job_fair_id";
    private String TAG_JOB_FAIR_START_DATE="job_fair_start_date";
    private String TAG_JOB_FAIR_END_DATE="job_fair_end_date";
    private String TAG_COLLEGE_NAME="college_name";
    private String TAG_SUCCESS="success";
    private String TAG_MESSAGE="message";

    private String ARRAY_NAME_FOR_STUDENT="studentdetail";
    private String TAG_STUDENT_NAME="Student_name";
    private String TAG_STUDENT_EMAIL="Student_email";
    private String TAG_STUDENT_PROFILE="Student_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_campus_participation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        recyclerView=findViewById(R.id.recyclerview_student_campus_participation_drives);

        session=new Session(this);

        BASE_URL=getResources().getString(R.string.BASE_URL);
        Image_uri = getResources().getString(R.string.Image_uri);

        client = new OkHttpClient();

        pd = new ProgressDialog(StudentCampusParticipationActivity.this);
        pd.setMessage("Loading Data please wait....");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.show();

        new StudentCampusParticipationActivity.AsycDataloadforCampusList().execute();

    }


    class AsycDataloadforCampusList extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            campusList = new ArrayList<HashMap>();
            URL = BASE_URL+"student_participated_campus_drive_by_id.php";

        }

        @Override
        protected String doInBackground(final String... strings) {

            RequestBody formbody = new FormBody.Builder()
                    .add("job_seeker_id", session.getLoginID())
                    .build();


            final Request req = new Request.Builder()
                    .url(URL)
                    .post(formbody)
                    .build();


            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("You got error",e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String rs = response.body().string();
                        Log.d("Response For Jobfair",rs);
                        JSONObject jsonObject = new JSONObject(rs);
                        int success = jsonObject.getInt(TAG_SUCCESS);

                        if(success == 1)
                        {
                            JSONArray jsonArray = jsonObject.getJSONArray(ARRAY_NAME);
                            Log.d("Array Length",String.valueOf(jsonArray.length()));
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                HashMap<String,String> common = new HashMap<>();
                                common.put(TAG_JOB_FAIR_ID,jsonObject1.getString(TAG_JOB_FAIR_ID));
                                common.put(TAG_COLLEGE_NAME,jsonObject1.getString(TAG_COLLEGE_NAME));
                                common.put(TAG_JOB_FAIR_START_DATE,jsonObject1.getString(TAG_JOB_FAIR_START_DATE));
                                common.put(TAG_JOB_FAIR_END_DATE,jsonObject1.getString(TAG_JOB_FAIR_END_DATE));
                                    Log.d("Response Participated",common.toString());
                                campusList.add(common);

                            }


                        }
                        else if(success==2)
                        {
                            String msg=jsonObject.getString(TAG_MESSAGE);
                            HashMap<String,String> common = new HashMap<>();
                            common.put(TAG_MESSAGE,msg);
                            campusList.add(common);

                        }
                        pd.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new StudentCampusParticipationAdapter(StudentCampusParticipationActivity.this,campusList);
                                recyclerView.setLayoutManager(new GridLayoutManager(StudentCampusParticipationActivity.this,1));
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(adapter);

                            }
                        });


                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }

                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

}
