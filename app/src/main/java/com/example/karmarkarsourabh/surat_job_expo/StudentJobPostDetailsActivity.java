package com.example.karmarkarsourabh.surat_job_expo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StudentJobPostDetailsActivity extends AppCompatActivity {

    Intent intent;
    TextView company_name;
    TextView company_address;
    TextView experience;
    TextView package_provided;
    TextView post_date;
    TextView description;
    TextView aggrement;

    String name,address,experience_required,packageProvided,date,desc,aggrementDetails;
    String job_post_id;
    String BASE_URL,URL;
    OkHttpClient client;
    ArrayList studentJobpostDdetailArraylist;
    ProgressDialog pd;

    //jobpostdetails
    private static final String TAG_COMPANY_NAME = "Company_name";
    private static final String TAG_COMPANY_ADDRESS = "Company_address";
    private static final String TAG_JOB_EXPERIENCE = "Experience_required";
    private static final String TAG_JOB_PACKAGE = "Package_provided";
    private static final String TAG_JOB_POST_DATE = "Post_date";
    private static final String TAG_JOB_DESCRIPTION = "Post_description";
    private static final String TAG_JOB_AGGREMENT = "Agreement_details";
    private static final String TAG_SUCCESS = "success";
    private static final String ARRAY_NAME = "companyJobPostDetails";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_job_post_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        job_post_id = intent.getStringExtra("company_job_post_id");

        BASE_URL=getResources().getString(R.string.BASE_URL);
        client = new OkHttpClient();
        studentJobpostDdetailArraylist = new ArrayList<>();


        company_name = (TextView)findViewById(R.id.textview_studentJobPostDetail_companyname);
        company_address = (TextView)findViewById(R.id.textview_studentJobPostDetail_joblocation);
        experience = (TextView)findViewById(R.id.textview_studentJobPostDetail_jobexperience);
        package_provided = (TextView)findViewById(R.id.textview_studentJobPostDetail_salary);
        post_date = (TextView)findViewById(R.id.textview_studentJobPostDetail_postedate);
        description = (TextView)findViewById(R.id.textview_studentJobPostDetail_job_description);
        aggrement = (TextView)findViewById(R.id.textview_studentJobPostDetail_aggrementDetails);

        pd = new ProgressDialog(StudentJobPostDetailsActivity.this);
        pd.setMessage("Loading Data please wait....");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.show();

        new AsycDataloadforStudentJobPostDetails().execute();

    }
    class AsycDataloadforStudentJobPostDetails extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            studentJobpostDdetailArraylist = new ArrayList<>();
            URL = BASE_URL+"student_job_post_details.php";

        }

        @Override
        protected String doInBackground(final String... strings) {

            RequestBody formbody = new FormBody.Builder()
                    .add("company_job_post_id",job_post_id)
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
                        if(success == 1) {
                            JSONArray jsonArray = jsonObject.getJSONArray(ARRAY_NAME);
                            Log.d("Array Length", String.valueOf(jsonArray.length()));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                name = jsonObject1.getString(TAG_COMPANY_NAME);
                                address = jsonObject1.getString(TAG_COMPANY_ADDRESS);
                                experience_required = jsonObject1.getString(TAG_JOB_EXPERIENCE);
                                packageProvided = jsonObject1.getString(TAG_JOB_PACKAGE);
                                date = jsonObject1.getString(TAG_JOB_POST_DATE);
                                desc = jsonObject1.getString(TAG_JOB_DESCRIPTION);
                                aggrementDetails =  jsonObject1.getString(TAG_JOB_AGGREMENT);

                            }
                        }

                        pd.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            company_name.setText(name);
                            company_address.setText(address);
                            experience.setText(experience_required);
                            package_provided.setText(packageProvided);
                            post_date.setText(date);
                            description.setText(desc);
                            aggrement.setText(aggrementDetails);
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
