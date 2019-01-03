package com.example.karmarkarsourabh.surat_job_expo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.karmarkarsourabh.surat_job_expo.Adaptor.StudentJobFairAdapter;
import com.example.karmarkarsourabh.surat_job_expo.Modal.JobFair.jobfair;
import com.example.karmarkarsourabh.surat_job_expo.Utill.Session;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class StudentHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList jobfairArraylist;
    String URL = "", BASE_URL, Image_uri, URL_STUDENT;
    OkHttpClient client;
    ImageView studentProfile;
    TextView studentName, studentEmail;
    String sprofile = "", sname = "", semail = "";
    private RecyclerView recyclerView;
    View v;
    ProgressDialog pd;

    private String ARRAY_NAME_FOR_STUDENT = "studentdetail";
    private static final String TAG_SUCCESS = "success";
    private String TAG_MESSAGE = "message";
    private String TAG_ISPARTICIPATED = "Isparticipated";
    private static final String TAG_JOB_FAIR_ID = "Job_fair_id";
    private static final String TAG_JOB_FAIR_NAME = "Job_fair_name";
    private static final String TAG_JOB_FAIR_START_DATE = "Job_fair_end_date";
    private static final String TAG_JOB_FAIR_END_DATE = "Job_fair_end_date";
    private static final String TAG_STUDENT_REG_START_DATE = "Student_registration_start_date";
    private static final String TAG_STUDENT_REG_END_DATE = "Student_registration_end_date";


    private static final String TAG_STUDENT_NAME = "Student_name";
    private static final String TAG_STUDENT_EMAIL = "Student_email";
    private static final String TAG_STUDENT_PROFILE = "Student_profile";


    Session session;
    private static final String ARRAY_NAME = "jobfairs";
    private SharedPreferences Stud_sharepref;
    private SharedPreferences userShare;
    private TextView hide_tv;
    private DrawerLayout drawer;
    private LinearLayout btn_note;
    private SwipeRefreshLayout swipeRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_student_home);
        toolbar.setTitle("Dashboard");
        setSupportActionBar(toolbar);

        new MaterialTapTargetPrompt.Builder(StudentHomeActivity.this)
                .setTarget(R.id.Note_btn)
                .setPrimaryText("Tap Here!")
                .setSecondaryText("All you needed to know before Getting Started!")
                .show();

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_student_home);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new JobFairLoad().execute();
            }
        });

        btn_note = (LinearLayout) findViewById(R.id.Note_btn);

        btn_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog note_dialog = new Dialog(StudentHomeActivity.this);
                note_dialog.setContentView(R.layout.student_home_note);
                note_dialog.show();


                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(note_dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;

                note_dialog.getWindow().setAttributes(lp);
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        v = navigationView.getHeaderView(0);
        studentProfile = (ImageView) v.findViewById(R.id.imageView_studentHome_imgprofile);
        studentName = (TextView) v.findViewById(R.id.textView_studentHome_studentName);
        studentEmail = (TextView) v.findViewById(R.id.textView_studentHome_studentEmail);
        navigationView.setNavigationItemSelectedListener(this);


        session = new Session(this);
        hide_tv = (TextView) findViewById(R.id.hidentTV);
        recyclerView = findViewById(R.id.recylerview_studentHome);

        BASE_URL = getResources().getString(R.string.BASE_URL);
        Image_uri = getResources().getString(R.string.Image_uri);
        client = new OkHttpClient();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        userShare = getSharedPreferences("user", 0);
        if (!userShare.getBoolean("login", false)) {
            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        jobfairArraylist = new ArrayList<>();



//        new AsycDataloadforJobfairName().execute();
        new JobFairLoad().execute();
        new AsycDataloadforStudent().execute();

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_student_home:
//                it is in the same activity so close the drwaer

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;

            case R.id.nav_student_campus_participation:
                startActivity(new Intent(StudentHomeActivity.this,MyParticipationDrives.class));
                break;
            case R.id.nav_student_logout:
                session.Destroy();
                Stud_sharepref = getSharedPreferences("stud", 0);
                Stud_sharepref.edit().clear().commit();
                userShare = getSharedPreferences("user", 0);
                userShare.edit().clear().commit();

                startActivity(new Intent(StudentHomeActivity.this, LoginActivity.class));
                break;
            case R.id.nav_cv:
                startActivity(new Intent(StudentHomeActivity.this, Resume_theme.class));
                break;
            default:
                Snackbar.make((StudentHomeActivity.this).findViewById(R.id.drawer_layout), "you have selected invalid slide", Snackbar.LENGTH_LONG).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class JobFairLoad extends AsyncTask<String, String,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                HttpUrl.Builder builder = HttpUrl.parse(getResources().getString(R.string.su_uri) + "API.php")
                        .newBuilder();
                builder.addQueryParameter("action", "JobFair_data");
                Request request = new Request.Builder()
                        .url(builder.build())
                        .build();
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();

                return response.body().string();

            }catch (Exception e){
                Log.e("List of JobFair", "doInBackground: "+e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(StudentHomeActivity.this);
            pd.setMessage("Loading Data please wait.");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equalsIgnoreCase(null)){
                Snackbar.make((StudentHomeActivity.this).findViewById(R.id.drawer_layout),"you have an eception!",Snackbar.LENGTH_LONG);
            }
            else {
                try{
                    JSONObject object = new JSONObject(s);
                    Log.e("Data", "onPostExecute: "+object);
                    Log.e("JsonArray", "onPostExecute: "+object.getJSONObject("data").toString());
                    Log.e("status", "onPostExecute: "+object.getInt("status"));
                    if (object.getInt("status")==1){
                        jobfair mjobfair = new Gson().fromJson(object.getJSONObject("data").toString(),jobfair.class);
                        StudentJobFairAdapter adapter = new StudentJobFairAdapter(mjobfair,StudentHomeActivity.this);
                        recyclerView.setLayoutManager(new LinearLayoutManager(StudentHomeActivity.this,LinearLayoutManager.VERTICAL,false));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);
                    }
                    else {
                        Snackbar.make((StudentHomeActivity.this).findViewById(R.id.drawer_layout),"Data not retrived!",Snackbar.LENGTH_LONG);
                    }
                }catch (JSONException e){
                    Log.e("json error", "onPostExecute: "+e.getMessage());
                }

            }
            pd.dismiss();
            if(swipeRefresh.isRefreshing()){
                swipeRefresh.setRefreshing(false);
            }
        }
    }


    class AsycDataloadforStudent extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            URL_STUDENT = BASE_URL + "student_display_by_id.php";
        }

        @Override
        protected String doInBackground(final String... strings) {

            RequestBody formbody = new FormBody.Builder()
                    .add("job_seeker_id", session.getLoginID())
                    .build();


            final Request req = new Request.Builder()
                    .url(URL_STUDENT)
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
                        Log.d("Response For student", rs);
                        JSONObject jsonObject = new JSONObject(rs);
                        int success = jsonObject.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            JSONArray jsonArray = jsonObject.getJSONArray(ARRAY_NAME_FOR_STUDENT);
                            Log.d("Array Length", String.valueOf(jsonArray.length()));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                sname = jsonObject1.getString(TAG_STUDENT_NAME);
                                semail = jsonObject1.getString(TAG_STUDENT_EMAIL);
                                sprofile = jsonObject1.getString(TAG_STUDENT_PROFILE);
                            }


                        }
                        pd.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                studentName.setText(sname);
                                studentEmail.setText(semail);
                                Glide.with(StudentHomeActivity.this).load(Image_uri + sprofile).into(studentProfile);

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
}



