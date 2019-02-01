package com.example.karmarkarsourabh.surat_job_expo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.Adaptor.JobListAdaptor;
import com.example.karmarkarsourabh.surat_job_expo.Modal.JobFair.jobfair;
import com.example.karmarkarsourabh.surat_job_expo.Modal.JobList.joblist;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JobFairSingle extends AppCompatActivity {

    private TextView tv;
    private jobfair mJobfair;
    private TextView st_tv;
    private TextView en_tv;
    private TextView location_tv;
    private TextView email_tv;
    private TextView contact_tv;
    private CollapsingToolbarLayout toolbarLayout;
    private AppBarLayout barLayout;
    private Toolbar app_toolbar;
    private FloatingActionButton Participate_FloatB;
    private RecyclerView job_list_RV;
    private LinearLayout FourZeroFour, participated_card;
    private int jid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_fair_single);

        //initailization
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collap_toolbar_job_details);
        barLayout = (AppBarLayout) findViewById(R.id.app_bar_job_details);
        app_toolbar = (Toolbar) findViewById(R.id.job_detai_toolbar);
        app_toolbar = (Toolbar) findViewById(R.id.job_detai_toolbar);
        setSupportActionBar(app_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        job_list_RV = (RecyclerView) findViewById(R.id.job_list);
        FourZeroFour = (LinearLayout) findViewById(R.id.dataNotFound);
        Participate_FloatB = (FloatingActionButton) findViewById(R.id.participate_FB);
        tv = (TextView) findViewById(R.id.Drive_name);
        st_tv = (TextView) findViewById(R.id.JFstart_datetv);
        en_tv = (TextView) findViewById(R.id.JFend_datetv);
        location_tv = (TextView) findViewById(R.id.location_tv);
        email_tv = (TextView) findViewById(R.id.ema_tv);
        contact_tv = (TextView) findViewById(R.id.cont_tv);
        participated_card = (LinearLayout) findViewById(R.id.participated_card);


        Participate_FloatB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v,"You have already participated",Snackbar.LENGTH_LONG).show();
                if (FourZeroFour.getVisibility() == View.VISIBLE) {
                    Snackbar.make(v, "Sorry there is no job is posted yet!", Snackbar.LENGTH_LONG).show();
                } else {
                    new participateInJobFair().execute();
                }
            }
        });

        Intent mintent = getIntent();
        mJobfair = (jobfair) mintent.getExtras().getSerializable("obj");
        final String CollegeName = mJobfair.getData().get(mintent.getIntExtra("pos", 0)).getClg_name();
        tv.setText(CollegeName);

        st_tv.setText(mJobfair.getData().get(mintent.getIntExtra("pos", 0)).getStart_date());
        en_tv.setText(mJobfair.getData().get(mintent.getIntExtra("pos", 0)).getStud_end_date());
        location_tv.setText(mJobfair.getData().get(mintent.getIntExtra("pos", 0)).getClg_loc());
        email_tv.setText(mJobfair.getData().get(mintent.getIntExtra("pos", 0)).getClg_email());
        contact_tv.setText(mJobfair.getData().get(mintent.getIntExtra("pos", 0)).getClg_contat());
        jid = mJobfair.getData().get(mintent.getIntExtra("pos", 0)).getJFid();

        barLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbarLayout.setTitle(CollegeName);
                    isShow = true;
                } else if (isShow) {
                    toolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
        new checkApplyForJobFair().execute();
    }


    private class GetJobList extends AsyncTask<String, String, String> {

        private String res;
        private joblist JobListModal;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Snackbar.make((JobFairSingle.this).findViewById(R.id.singleJobFairView), "Loading Data..!", Snackbar.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.e("JSon data", "onPostExecute: " + s);
                JSONObject object = new JSONObject(s);
                JSONObject dataObj = object.getJSONObject("data");
                JSONArray array = dataObj.getJSONArray("data");

                if (array.length() > 0) {
                    //Extraction Json data

                    JobListModal = new Gson().fromJson(dataObj.toString(), joblist.class);

                    //adaptor configuration
                    JobListAdaptor adaptor = new JobListAdaptor(JobFairSingle.this, JobListModal);
                    job_list_RV.setLayoutManager(new LinearLayoutManager(JobFairSingle.this, LinearLayoutManager.VERTICAL, false));
                    job_list_RV.setItemAnimator(new DefaultItemAnimator());
                    job_list_RV.setAdapter(adaptor);

                } else {

                    //the json have null data [ ]
                    FourZeroFour.setVisibility(View.VISIBLE);
                    job_list_RV.setVisibility(View.INVISIBLE);
                    Snackbar.make((JobFairSingle.this).findViewById(R.id.singleJobFairView), "There is no Data..!", Snackbar.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Log.e("JSonError", "onPostExecute: " + e.getMessage());
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Log.e("id", "doInBackground: " + String.valueOf(jid));
                HttpUrl.Builder builder = HttpUrl.parse(getResources().getString(R.string.su_uri) + "API.php")
                        .newBuilder();
                builder.addQueryParameter("action", "JobList");
                builder.addQueryParameter("Job_fair_id", String.valueOf(jid));
                Request request = new Request.Builder()
                        .url(builder.build())
                        .build();
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                res = response.body().string();
                Log.e("Data", "doInBackground: " + res);
                return res;
            } catch (IOException e) {
                Log.e("IOError", "doInBackground: " + e.getMessage());
                return null;
            }

        }
    }

    private class participateInJobFair extends AsyncTask<String, String, String> {
        private String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.e("Json Data", "onPostExecute: " + s);
                JSONObject object = new JSONObject(s);

                if (object.getInt("status") == 2) {
                    Snackbar.make((JobFairSingle.this).findViewById(R.id.singleJobFairView), object.getString("data") + " " + object.getString("message"), Snackbar.LENGTH_LONG).show();
                }
                else if (object.getInt("status") == 1){
                    job_list_RV.setVisibility(View.INVISIBLE);
                    participated_card.setVisibility(View.VISIBLE);
                    Snackbar.make((JobFairSingle.this).findViewById(R.id.singleJobFairView), object.getString("message"), Snackbar.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make((JobFairSingle.this).findViewById(R.id.singleJobFairView), object.getString("message"), Snackbar.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Log.e("JSon Error", "onPostExecute: " + e.getMessage());
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                SharedPreferences Stud_sharepref = getSharedPreferences("stud", 0);

                HttpUrl.Builder builder = HttpUrl.parse(getResources().getString(R.string.su_uri) + "API.php")
                        .newBuilder();
                builder.addQueryParameter("action", "participate_jobFair");
                builder.addQueryParameter("JS_ID", Stud_sharepref.getString("stud_id", "0"));
                builder.addQueryParameter("JF_ID", String.valueOf(jid));
                Request request = new Request.Builder()
                        .url(builder.build())
                        .build();
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                res = response.body().string();
                Log.e("Data", "doInBackground: " + res);
                return res;
            } catch (IOException e) {
                Log.e("IOError", "doInBackground: " + e.getMessage());
                return null;
            }
        }
    }

    private class checkApplyForJobFair extends AsyncTask<String,String,String>{
        private String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                JSONObject object = new JSONObject(s);
                if (object.getInt("status")==1){
                    job_list_RV.setVisibility(View.INVISIBLE);
                    participated_card.setVisibility(View.VISIBLE);
                }else {
                    new GetJobList().execute();
                }
            }
            catch (JSONException e){
                Log.e("JSon error", "onPostExecute: "+e.getMessage());
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                SharedPreferences Stud_sharepref = getSharedPreferences("stud", 0);

                HttpUrl.Builder builder = HttpUrl.parse(getResources().getString(R.string.su_uri) + "API.php")
                        .newBuilder();
                builder.addQueryParameter("action", "CheckApplication");
                builder.addQueryParameter("JS_ID", Stud_sharepref.getString("stud_id", "0"));
                builder.addQueryParameter("JF_ID", String.valueOf(jid));
                Request request = new Request.Builder()
                        .url(builder.build())
                        .build();
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                res = response.body().string();
                Log.e("Data", "doInBackground: " + res);
                return res;
            } catch (IOException e) {
                Log.e("IOError", "doInBackground: " + e.getMessage());
                return null;
            }

        }
    }
}
