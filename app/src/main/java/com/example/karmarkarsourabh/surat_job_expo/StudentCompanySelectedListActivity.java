package com.example.karmarkarsourabh.surat_job_expo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.karmarkarsourabh.surat_job_expo.Adaptor.StudentCompanySlectedListAdapter;
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

public class StudentCompanySelectedListActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    Session session;
    ArrayList companyArraylist;
    String URL="",BASE_URL;
    OkHttpClient client;
    ProgressDialog pd;
    StudentCompanySlectedListAdapter adapter;



    private String ARRAY_NAME="companydetail";
    private String TAG_Company_name="Company_name";
    private String TAG_Job_fair_id="Job_fair_id";
    private String TAG_Job_seeker_id="Job_seeker_id";
    private String TAG_SUCCESS="success";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_company_selected_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BASE_URL=getResources().getString(R.string.BASE_URL);

        URL=BASE_URL+"student_company_selected.php";

        recyclerView=(RecyclerView)findViewById(R.id.recycleview_student_company_selected);

        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setHasFixedSize(true);
        client=new OkHttpClient();
        companyArraylist = new ArrayList<>();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading Data please wait....");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.show();
        new AsycCompanyDetail().execute();



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }


    class AsycCompanyDetail extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            URL = BASE_URL+"student_company_selected.php";

            companyArraylist=new ArrayList<>();


        }

        @Override
        protected String doInBackground(final String... strings) {
            RequestBody formbody = new FormBody.Builder()
                    .add("Job_seeker_id",1+"")
                    .add("Job_fair_id",1+"")
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
                        JSONObject jsonObject = new JSONObject(rs);
                        int success = jsonObject.getInt(TAG_SUCCESS);
                        if(success == 1)
                        {

                            JSONArray jsonArray = jsonObject.getJSONArray(ARRAY_NAME);


                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                HashMap<String,String> common = new HashMap<>();
                                common.put(TAG_Company_name,jsonObject2.getString(TAG_Company_name));

                                companyArraylist.add(common);
                            }


                            pd.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //company details in recycler view
                                    adapter = new StudentCompanySlectedListAdapter(companyArraylist,StudentCompanySelectedListActivity.this);
                                    recyclerView.setLayoutManager(new GridLayoutManager(StudentCompanySelectedListActivity.this,1));
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
