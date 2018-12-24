package com.example.karmarkarsourabh.surat_job_expo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CollegeProfile extends AppCompatActivity {

    private TextView cname,location,Email,contact,website;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        new getCollegeProfiledetails().execute();
    }

    private class getCollegeProfiledetails extends AsyncTask<Void, JSONObject, JSONObject> {
        private int id;

        @Override
        protected JSONObject doInBackground(Void... voids) {
            if (id == 0) {
                Log.e("CLG-ID", "doInBackground: id 0");
            } else {
                try {
                    Log.e("CLG-ID", "doInBackground: id 0");
                    HttpUrl.Builder builder = HttpUrl.parse(getResources().getString(R.string.su_uri) + "API.php")
                            .newBuilder();
                    builder.addQueryParameter("action", "getclgprofiledata");
                    builder.addQueryParameter("clg_id", String.valueOf(id));
                    Request request = new Request.Builder()
                            .url(builder.build())
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    Response response = client.newCall(request).execute();
                    JSONObject jobj = new JSONObject(response.body().string());
                    Log.e("DATA", "doInBackground: " + jobj);
                    return jobj;
                } catch (Exception e) {
                    Log.e("clgprofileData Error", "doInBackground: " + e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Intent intent = getIntent();
            id = intent.getIntExtra("cid", 0);
            Log.e("CLG-ID", "onPreexecution: id 0");
            if (id == 0) {
                Snackbar.make((CollegeProfile.this).findViewById(R.id.clg_view), "College Not Found!", Snackbar.LENGTH_INDEFINITE);
            }
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);
            try {

                cname = (TextView) findViewById(R.id.collegeName_tv);
                location = (TextView) findViewById(R.id.collegeLocation_tv);
                Email = (TextView) findViewById(R.id.collegeEmailid_tv);
                contact = (TextView) findViewById(R.id.collegeContact_tv);
                website = (TextView) findViewById(R.id.collegeWeb_tv);

                JSONArray jsonArray = object.getJSONArray("data");
                JSONObject jobj = jsonArray.getJSONObject(0);
                if (jobj.has("College_name")){
                    cname.setText(jobj.getString("College_name"));
                    location.setText(jobj.getString("College_location"));
                    Email.setText(jobj.getString("College_email"));
                    contact.setText(jobj.getString("College_contact"));
                    website.setText(jobj.getString("College_webURL"));
                }else {
                    Snackbar.make((CollegeProfile.this).findViewById(R.id.clg_view), "Json data not found!", Snackbar.LENGTH_INDEFINITE);
                }
            } catch (JSONException e) {
                Log.e("JSON Error", "onPostExecute: "+e.getMessage());
            }


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Snackbar.make((CollegeProfile.this).findViewById(R.id.clg_view), "Something went Wrong!", Snackbar.LENGTH_INDEFINITE);
        }
    }
}
