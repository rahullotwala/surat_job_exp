package com.example.karmarkarsourabh.surat_job_expo;

import android.os.AsyncTask;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.Adaptor.resume_adaptor;
import com.example.karmarkarsourabh.surat_job_expo.Modal.resume.resume_md;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Resume_theme extends AppCompatActivity  {
    FragmentPagerAdapter adapterViewPager;
    private resume_md reume_modal;
    private RecyclerView rem_rc;
    private TextView hTv;
    private ImageView warningIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_theme);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        warningIC = (ImageView) findViewById(R.id.warningIV);
        hTv = (TextView) findViewById(R.id.hidden_TV);
        rem_rc = (RecyclerView) findViewById(R.id.resume_list);
        new getResume().execute();
        
    }

    class getResume extends AsyncTask<String,String,String>{

        private String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("status", "onPreExecute: ");
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONObject object = new JSONObject(aVoid);
                JSONArray array = object.getJSONArray("data");
                Log.e("ARRAY", "onPostExecute: "+array);
                if(array.length()>0) {
                    Log.e("status", "onPostExecute: " + aVoid);
                    reume_modal = new Gson().fromJson(aVoid, resume_md.class);
                    resume_adaptor adaptor = new resume_adaptor(reume_modal, Resume_theme.this);
                    rem_rc.setLayoutManager(new GridLayoutManager(Resume_theme.this, 2, GridLayoutManager.VERTICAL, false));
                    rem_rc.setItemAnimator(new DefaultItemAnimator());
                    rem_rc.setAdapter(adaptor);
                }
                else {
                    rem_rc.setVisibility(View.INVISIBLE);
                    hTv.setVisibility(View.VISIBLE);
                    warningIC.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                Log.e("json error resume list", "onPostExecute: "+e.getMessage());
            }

        }

        @Override
        protected String doInBackground(String... voids) {

            try {
                HttpUrl.Builder builder = HttpUrl.parse(getResources().getString(R.string.su_uri) + "API.php")
                        .newBuilder();
                builder.addQueryParameter("action", "resume_theme");
                Request request = new Request.Builder()
                        .url(builder.build())
                        .build();
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                res = response.body().string();
                Log.e("Data", "doInBackground: "+res);
                return res;
            }
            catch (Exception e){
                Log.e("resume_list_err", "msg: "+e.getMessage());
            }
            return null;
        }
    }
}
