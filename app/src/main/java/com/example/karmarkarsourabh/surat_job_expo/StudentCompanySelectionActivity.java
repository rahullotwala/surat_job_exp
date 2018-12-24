package com.example.karmarkarsourabh.surat_job_expo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.Adaptor.StudentCompanySelectionAdapter;
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

public class StudentCompanySelectionActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    Session session;
    ArrayList<HashMap> companyList;
    String URL="",BASE_URL;
    OkHttpClient client;
    Intent intent;
    ProgressDialog pd;
    StudentCompanySelectionAdapter adapter;
    TextView drivename;

    String job_fair_id;
    //Data Passed From Intent

    private String TAG_JOB_FAIR_ID="job_fair_id";
    private String TAG_COLLEGE_NAME="college_name";

    //Json node names

    private String ARRAY_NAME="companyDetail";
    private String TAG_SUCCESS="success";
    private String TAG_MESSAGE="message";
    private String TAG_COMPANY_ID="company_id";
    private String TAG_COMPANY_NAME="company_name";
    private String TAG_COMPANY_ADDRESS="company_address";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_company_selection);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View view = (View) findViewById(R.id.viewGrp);
        Snackbar snackbar = Snackbar.make(view,"Note: Once applied you can not revert",Snackbar.LENGTH_LONG);
        View sview=snackbar.getView();
        sview.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();

        drivename=findViewById(R.id.textview_studentCompanySelected_drivename);
        recyclerView=findViewById(R.id.recyclerview_student_company_selection);
        intent=getIntent();
        drivename.setText(intent.getStringExtra(TAG_COLLEGE_NAME));
        session=new Session(this);
        BASE_URL=getResources().getString(R.string.BASE_URL);
        job_fair_id = intent.getStringExtra(TAG_JOB_FAIR_ID);
        client = new OkHttpClient();


        pd = new ProgressDialog(this);
        pd.setMessage("Loading Data please wait....");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.show();

            new AsycDataloadforCompanyList().execute();

    }

    class AsycDataloadforCompanyList extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            companyList=new ArrayList<HashMap>();
            URL = BASE_URL+"company_participated_in_drive_by_driveId.php";

        }

        @Override
        protected String doInBackground(final String... strings) {

            RequestBody formbody = new FormBody.Builder()
                    .add("job_fair_id",intent.getStringExtra(TAG_JOB_FAIR_ID) )
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
                        Log.d("ResForCompaniesInCampus",rs);
                        JSONObject jsonObject = new JSONObject(rs);
                        int success = jsonObject.getInt(TAG_SUCCESS);

                        if(success == 1)
                        {
                            JSONArray jsonArray = jsonObject.getJSONArray(ARRAY_NAME);

                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                HashMap<String,String> common = new HashMap<>();
                                common.put(TAG_JOB_FAIR_ID,intent.getStringExtra(TAG_JOB_FAIR_ID));
                                common.put(TAG_COMPANY_ID,jsonObject1.getString(TAG_COMPANY_ID));
                                common.put(TAG_COMPANY_NAME,jsonObject1.getString(TAG_COMPANY_NAME));
                                common.put(TAG_COMPANY_ADDRESS,jsonObject1.getString(TAG_COMPANY_ADDRESS));

                                Log.d("ResParticiCompanyList",common.toString());
                               companyList.add(common);

                            }


                        }
                        else if(success==2)
                        {
                            String msg=jsonObject.getString(TAG_MESSAGE);
                            HashMap<String,String> common = new HashMap<>();
                            common.put(TAG_MESSAGE,msg);
                            companyList.add(common);

                        }
                        pd.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new StudentCompanySelectionAdapter(StudentCompanySelectionActivity.this,companyList);
                                recyclerView.setLayoutManager(new GridLayoutManager(StudentCompanySelectionActivity.this,1));
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

    public void RedirectToCompanyPriority(View view) {
        Intent intent = new Intent(this,StudentSelectedCompanyPriorityActivity.class);
        intent.putExtra("job_fair_id",job_fair_id);
        startActivity(intent);
    }
}
