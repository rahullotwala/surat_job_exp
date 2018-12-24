package com.example.karmarkarsourabh.surat_job_expo;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karmarkarsourabh.surat_job_expo.Adaptor.College_Course_Manage_Detail_Adapter_Fragment_Company;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class College_Course_Manage_Detail_Fragment_Company extends Fragment {

    public int jobfairid;
    ArrayList companyArraylist;
    String URL="",BASE_URL;
    OkHttpClient client;
    private RecyclerView recyclerView;
    College_Course_Manage_Detail_Adapter_Fragment_Company adapter;
    ProgressDialog pd;


    private static final String TAG_Company_Name = "Company_name";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_Company_Adress = "Company_address";
    private static final String TAG_Company_webURL = "Company_webURL";


    private static final String TAG_SUCCESS = "success";

    private static final String ARRAY_NAME = "companydetail";


    public College_Course_Manage_Detail_Fragment_Company() {

    }

    @Override
    public void onStart() {
        super.onStart();
    Log.d("jid",jobfairid+"");
       recyclerView=getView().findViewById(R.id.recycleview_fragment_college_course_manage_detail_company);


        BASE_URL=getResources().getString(R.string.BASE_URL);
        client = new OkHttpClient();
        companyArraylist = new ArrayList<>();

        URL = BASE_URL+"college_course_manage_detail_participatedCompany.php";

        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading Data please wait....");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.show();
        new AsycCompanyDetail().execute();

    }




    class AsycCompanyDetail extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            URL = BASE_URL+"college_course_manage_detail_participatedCompany.php";
            companyArraylist = new ArrayList<>();


        }

        @Override
        protected String doInBackground(final String... strings) {
            RequestBody formbody = new FormBody.Builder()
                    .add("Job_fair_id",jobfairid+"")
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
                                common.put(TAG_Company_Name,jsonObject2.getString(TAG_Company_Name));
                                common.put(TAG_Company_Adress,jsonObject2.getString(TAG_Company_Adress));
                                common.put(TAG_Company_webURL,jsonObject2.getString(TAG_Company_webURL));

                                companyArraylist.add(common);
                                          }

                        }
                        else {


                            String msg=jsonObject.getString(TAG_MESSAGE);
                            HashMap<String,String> common = new HashMap<>();
                            common.put(TAG_MESSAGE,msg);
                            companyArraylist.add(common);



                        }
                        pd.dismiss();

                        final Handler mHandler = new Handler(Looper.getMainLooper());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        //company details in recycler view

                                        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getContext());
                                        mlinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        adapter = new College_Course_Manage_Detail_Adapter_Fragment_Company(companyArraylist, getContext());
                                        recyclerView.setLayoutManager(mlinearLayoutManager);
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setAdapter(adapter);
                                    }
                                });
                            }
                        }).start();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_college__course__manage__detail__fragment__company, container, false);


    }



}
