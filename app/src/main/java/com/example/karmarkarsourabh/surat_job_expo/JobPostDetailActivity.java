package com.example.karmarkarsourabh.surat_job_expo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.Adaptor.JobFairDetailsAdapter;

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

public class JobPostDetailActivity extends AppCompatActivity {

    Intent intent;
    ArrayList jobpostDdetailArraylist,courseArraylist;
    ListView courseList;
    String URL="",BASE_URL;
    OkHttpClient client;
    private RecyclerView recyclerView;
    JobFairDetailsAdapter adapter;
    ProgressDialog pd;


    //job_fair_details
    int job_fair_id;
    String job_fair_name,job_fair_end_date,student_reg_start_date,student_reg_end_date,totalStudents;
    public TextView Campus_name;
    public TextView Job_fair_end_date;
    public TextView Total_student;
    public TextView Student_reg_start_date;
    public TextView Student_reg_end_date;
    private static final String TAG_SUCCESS = "success";

    private static final String TAG_COMPANY_JOB_POST_ID = "Company_job_post_id";
    private static final String TAG_JOB_FAIR_END_DATE = "Job_fair_end_date";
    private static final String TAG_STUDENT_REG_START_DATE = "Student_registration_start_date";
    private static final String TAG_STUDENT_REG_END_DATE = "Student_registration_end_date";
    private static final String TAG_TOTAL_STUDENT = "Total_student";

    //jobpost details
    private static final String TAG_COMPANY_NAME = "Company_name";
    private static final String TAG_POST_NAME = "Post_name";
    private static final String TAG_COMPANY_WEBURL = "Company_webURL";
    private static final String TAG_COMPANY_ADDRESS = "Company_address";
    private static final String TAG_POST_DESC = "Post_description";
    private static final String TAG_EXPERIENCE_REQUIRED = "Experience_required";
    private static final String TAG_TOTAL_PEOPLE_APPLIED = "Total_people";

    //course name
    private static final String TAG_COURSE_NAME = "Course_name";

    private static final String ARRAY_NAME = "jobfairdetails";
    private static final String ARRAY_NAME1 = "jobpostdetails";
    private static final String ARRAY_NAME2 = "coursesname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Campus_name = (TextView)findViewById(R.id.textView_job_post_detail_campusName);
        Job_fair_end_date = (TextView)findViewById(R.id.textView_job_post_detail_jobfair_end_date);
        Total_student = (TextView)findViewById(R.id.textView_job_post_detail_totalStudents);
        Student_reg_start_date = (TextView)findViewById(R.id.textView_job_post_detail_studentRegStartDate);
        Student_reg_end_date = (TextView)findViewById(R.id.textView_job_post_detail_studentRegEndDate);

        recyclerView = findViewById(R.id.recylerview_jobPostDetails);


        BASE_URL=getResources().getString(R.string.BASE_URL);
        client = new OkHttpClient();
        jobpostDdetailArraylist = new ArrayList<>();
        URL = BASE_URL+"jobpost_display_all.php";

        intent = getIntent();
        job_fair_id = intent.getIntExtra("jobfairid",0);
        job_fair_name = intent.getStringExtra("jobfairname");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        pd = new ProgressDialog(JobPostDetailActivity.this);
        pd.setMessage("Loading Data please wait....");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.show();
        new AsycDataloadforJobfairDetail().execute();

    }

    class AsycDataloadforJobfairDetail extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            URL = BASE_URL+"jobpost_display_all.php";
            jobpostDdetailArraylist = new ArrayList<>();
            courseArraylist = new ArrayList<>();
            courseList = (ListView)findViewById(R.id.listView_jobPostDetails);
        }

        @Override
        protected String doInBackground(final String... strings) {
            RequestBody formbody = new FormBody.Builder()
                    .add("job_fair_id",job_fair_id + "")
                    .build();

            Request req = new Request.Builder()
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
                        Log.d("responseJobPosts",rs);
                        JSONObject jsonObject = new JSONObject(rs);
                        int success = jsonObject.getInt(TAG_SUCCESS);
                        if(success == 1)
                        {
                            JSONArray jsonArray = jsonObject.getJSONArray(ARRAY_NAME);
                            JSONArray jsonArray1 = jsonObject.getJSONArray(ARRAY_NAME1);
                            JSONArray jsonArray2 = jsonObject.getJSONArray(ARRAY_NAME2);
                            for(int i=0;i<jsonArray.length();i++)
                            {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                job_fair_end_date = jsonObject1.getString(TAG_JOB_FAIR_END_DATE);
                                totalStudents = jsonObject1.getString(TAG_TOTAL_STUDENT);
                                student_reg_start_date = jsonObject1.getString(TAG_STUDENT_REG_START_DATE);
                                student_reg_end_date = jsonObject1.getString(TAG_STUDENT_REG_END_DATE);
                            }
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                                HashMap<String,String> common = new HashMap<>();
                                common.put(TAG_COMPANY_JOB_POST_ID,jsonObject2.getString(TAG_COMPANY_JOB_POST_ID));
                                common.put(TAG_COMPANY_NAME,jsonObject2.getString(TAG_COMPANY_NAME));
                                common.put(TAG_POST_NAME,jsonObject2.getString(TAG_POST_NAME));
                                common.put(TAG_COMPANY_WEBURL,jsonObject2.getString(TAG_COMPANY_WEBURL));
                                common.put(TAG_COMPANY_ADDRESS,jsonObject2.getString(TAG_COMPANY_ADDRESS));
                                common.put(TAG_POST_DESC,jsonObject2.getString(TAG_POST_DESC));
                                common.put(TAG_EXPERIENCE_REQUIRED,jsonObject2.getString(TAG_EXPERIENCE_REQUIRED));
                                common.put(TAG_TOTAL_PEOPLE_APPLIED,jsonObject2.getString(TAG_TOTAL_PEOPLE_APPLIED));
                                jobpostDdetailArraylist.add(common);
                            }
                            for(int i=0;i<jsonArray2.length();i++)
                            {
                                JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                                ArrayList<String> common = new ArrayList<>();
                                common.add(jsonObject2.getString(TAG_COURSE_NAME));
                                courseArraylist.add(common);
                            }

                            pd.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //set the value of job_fair details
                                    Campus_name.setText(job_fair_name);
                                    Job_fair_end_date.setText(job_fair_end_date);
                                    Student_reg_start_date.setText(student_reg_start_date);
                                    Student_reg_end_date.setText(student_reg_end_date);
                                    Total_student.setText(totalStudents);

                                    //course listview
                                    ArrayAdapter<CharSequence> Listadapter = new ArrayAdapter<CharSequence>(JobPostDetailActivity.this,R.layout.custom_textview_for_listview,courseArraylist);
                                    courseList.setAdapter(Listadapter);

                                    //company details in recycler view
                                    adapter = new JobFairDetailsAdapter(jobpostDdetailArraylist,JobPostDetailActivity.this);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(JobPostDetailActivity.this,LinearLayoutManager.VERTICAL,false));
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setAdapter(adapter);


                                }
                            });


                        }
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

        }
    }


}
