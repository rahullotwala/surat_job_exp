package com.example.karmarkarsourabh.surat_job_expo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.example.karmarkarsourabh.surat_job_expo.Adaptor.StudentSelectedCompanyPriorityAdapter;
import com.example.karmarkarsourabh.surat_job_expo.Utill.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StudentSelectedCompanyPriorityActivity extends AppCompatActivity {

    ArrayList studentCompnayPriorityArraylist;
    String URL = "", BASE_URL, Priority_url;
    OkHttpClient client;
    private RecyclerView recyclerView;
    StudentSelectedCompanyPriorityAdapter adapter;
    Intent intent1;
    ProgressDialog pd;
    Session session;
    String selctedPostString = "";
    private String ARRAY_NAME = "companyDetailByPriority";
    private static final String TAG_SUCCESS = "success";

    private static final String TAG_STUDENT_PRIORITY_ID = "Student_priority_id";
    private static final String TAG_COMPANY_NAME = "Company_name";
    private static final String TAG_COMPANY_ADDRESS = "Company_address";
    private static final String TAG_STUDENT_PRIORITY = "Priority";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_selected_company_priority);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new Session(this);
        client = new OkHttpClient();
        BASE_URL = getResources().getString(R.string.BASE_URL);
        intent1 = getIntent();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_student_company_selection_priority);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_studentSelectedCompanyPriority);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(StudentSelectedCompanyPriorityActivity.this,StudentSelectedCompanyPriorityActivity.class);
//                startActivity(intent);
//            }
//        });

        pd = new ProgressDialog(this);
        pd.setMessage("Loading Data please wait....");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.show();


        new AsycDataloadforStudentCompanyPriority().execute();


    }


    class AsycDataloadforStudentCompanyPriority extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            studentCompnayPriorityArraylist = new ArrayList<>();
            URL = BASE_URL + "student_participated_selected_company_display_by_priority.php";
        }

        @Override
        protected String doInBackground(final String... strings) {
            RequestBody formbody = new FormBody.Builder()
                    .add("job_seeker_id", session.getLoginID())
                    .add("job_fair_id", intent1.getStringExtra("job_fair_id"))
                    .build();


            final Request req = new Request.Builder()
                    .url(URL)
                    .post(formbody)
                    .build();


            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("You got error", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String rs = response.body().string();
                        JSONObject jsonObject = new JSONObject(rs);
                        int success = jsonObject.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            JSONArray jsonArray = jsonObject.getJSONArray(ARRAY_NAME);
                            Log.d("Array Length", String.valueOf(jsonArray.length()));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                HashMap<String, String> common = new HashMap<>();
                                HashMap<String, String> common1 = new HashMap<>();
                                common.put(TAG_STUDENT_PRIORITY_ID, jsonObject1.getString(TAG_STUDENT_PRIORITY_ID));
                                common1.put(TAG_STUDENT_PRIORITY_ID, jsonObject1.getString(TAG_STUDENT_PRIORITY_ID));
                                common.put(TAG_COMPANY_NAME, jsonObject1.getString(TAG_COMPANY_NAME));
                                common.put(TAG_COMPANY_ADDRESS, jsonObject1.getString(TAG_COMPANY_ADDRESS));
                                common.put(TAG_STUDENT_PRIORITY, jsonObject1.getString(TAG_STUDENT_PRIORITY));
                                studentCompnayPriorityArraylist.add(common);

                            }


                        }
                        pd.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new StudentSelectedCompanyPriorityAdapter(studentCompnayPriorityArraylist, StudentSelectedCompanyPriorityActivity.this);
                                recyclerView.setLayoutManager(new GridLayoutManager(StudentSelectedCompanyPriorityActivity.this, 1));
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(adapter);

                                ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
                                    @Override
                                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder dragged, RecyclerView.ViewHolder target) {
                                        int position_dragged = dragged.getAdapterPosition();
                                        int positon_target = target.getAdapterPosition();

                                        Collections.swap(studentCompnayPriorityArraylist, position_dragged, positon_target);

                                        adapter.notifyItemMoved(position_dragged, positon_target);

                                        Log.d("array", studentCompnayPriorityArraylist + "");
                                        return false;
                                    }

                                    @Override
                                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                                    }
                                });
                                helper.attachToRecyclerView(recyclerView);


                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
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

    public void updateCompnayPriority(View view) {

            selctedPostString="";
            for (int i = 0; i < studentCompnayPriorityArraylist.size(); i++) {
                HashMap<String, String> map = (HashMap<String, String>) studentCompnayPriorityArraylist.get(i);
                selctedPostString = selctedPostString + map.get(TAG_STUDENT_PRIORITY_ID) + " ";
            }
            client = new OkHttpClient();
            Priority_url = BASE_URL + "student_company_prioity_update.php";
            RequestBody formbody = new FormBody.Builder()
                    .add("student_priority_id_string", selctedPostString)
                    .build();
            final Request req = new Request.Builder()
                    .url(Priority_url)
                    .post(formbody)
                    .build();
            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("Msg", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String rs = response.body().string();
                        Log.d("response", rs);
                        JSONObject jobj = new JSONObject(rs);
                        int success = jobj.getInt(TAG_SUCCESS);
                        String msg = jobj.getString("message");
                        Log.d("Status code", success + "");
                        Log.d("Status msg", msg);
                        if (success == 1) {
                            Intent i = new Intent(StudentSelectedCompanyPriorityActivity.this, StudentCampusParticipationActivity.class);
                            startActivity(i);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

}