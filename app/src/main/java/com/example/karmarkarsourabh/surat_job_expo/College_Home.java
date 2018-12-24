package com.example.karmarkarsourabh.surat_job_expo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.Adaptor.College_Home_Adapter;
import com.example.karmarkarsourabh.surat_job_expo.GETSET.College;
import com.example.karmarkarsourabh.surat_job_expo.Utill.Session;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.karmarkarsourabh.surat_job_expo.Utill.utill;

public class College_Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Intent intent;

    //Host_college_Display static block
    ArrayList collegeArraylist;
    TextView collegeName, collegeEmail, collegelocation, collegecontact;
    String cname = "", cemail = "", cadd = "", ccontact = "";

    //end

    ArrayList jobfairdetailArraylist;
    ArrayList hostcollegenameArraylist;
    ArrayList collegecountArraylist;
    ArrayList companycountArraylist;
    ArrayList studentcountArraylist;

    String URL = "", BASE_URL;
    OkHttpClient client;
    private RecyclerView recyclerView;
    College_Home_Adapter adapter;
    ProgressDialog pd;

    Session session;

    //for static block of hostcollege

    private static final String TAG_COLLEGE_LOCATION = "College_location";
    private static final String TAG_COLLEGE_EMAIL = "College_email";
    private static final String TAG_COLLEGE_CONTACT = "College_contact";

    private static final String ARRAY_NAMEC = "collegedetail";
    //end


    private static final String TAG_Host_College_Name = "College_name";
    private static final String TAG_job_fair_id = "job_fair_id";
    private static final String TAG_Total_Company_Count = "Total_company";
    private static final String TAG_Total_Student_Count = "Total_student";
    private static final String TAG_Total_College_Count = "Total_college";
    private static final String TAG_SUCCESS = "success";


    private static final String ARRAY_NAME = "jobfairdetails";
    private static final String ARRAY_NAME1 = "jobfairdetails1";
    private static final String ARRAY_NAME2 = "jobfairdetails2";
    private static final String ARRAY_NAME3 = "jobfairdetails3";


    DrawerLayout drawer;
    NavigationView navigationView;

    Toolbar toolbar;
    private TextView clg_name;
    private TextView clg_loc;
    private LinearLayout lldatanotfound;
    private int id;
    private SharedPreferences CollegeSharePref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college__home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        utill utillobj = new utill(College_Home.this, (College_Home.this).findViewById(R.id.clg_content));
        if (utillobj.internet_connection()) {
            new getCollegeData().execute();
            session = new Session(this);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            recyclerView = findViewById(R.id.recylerview_collegeHome);
            lldatanotfound = (LinearLayout) findViewById(R.id.llnotfound);
            BASE_URL = getResources().getString(R.string.BASE_URL);
            client = new OkHttpClient();
            jobfairdetailArraylist = new ArrayList<>();
            collegeArraylist = new ArrayList<>();
            hostcollegenameArraylist = new ArrayList<>();
            URL = BASE_URL + "college_home.php";

            new AsycDataloadforJobfairDetail().execute();
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        } else {
            utillobj.internet_Retry();
        }
    }

    class AsycDataloadforJobfairDetail extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(College_Home.this);
            pd.setMessage("Loading Data please wait....");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();
            URL = BASE_URL + "college_home.php";

            collegeArraylist = new ArrayList<>();
            jobfairdetailArraylist = new ArrayList<>();
            hostcollegenameArraylist = new ArrayList<>();
            collegecountArraylist = new ArrayList<>();
            companycountArraylist = new ArrayList<>();
            studentcountArraylist = new ArrayList<>();

        }

        @Override
        protected String doInBackground(final String... strings) {
            RequestBody formbody = new FormBody.Builder()
                    .add("College_id", session.getLoginID())
                    .add("job_fair_id", TAG_job_fair_id)
                    .add("College_name", TAG_Host_College_Name)
                    .add("Total_student", TAG_Total_Student_Count)
                    .add("Total_college", TAG_Total_College_Count)
                    .add("Total_company", TAG_Total_Company_Count)

                    .build();

            Request req = new Request.Builder()
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
                        Log.e("Data", "onResponse: "+jsonObject);
                        int success = jsonObject.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            JSONArray jsonArrayc = jsonObject.getJSONArray(ARRAY_NAMEC);
                            JSONArray jsonArray = jsonObject.getJSONArray(ARRAY_NAME);
                            JSONArray jsonArray1 = jsonObject.getJSONArray(ARRAY_NAME1);
                            JSONArray jsonArray2 = jsonObject.getJSONArray(ARRAY_NAME2);
                            JSONArray jsonArray3 = jsonObject.getJSONArray(ARRAY_NAME3);



                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                HashMap<String, String> common = new HashMap<>();
                                common.put(TAG_Host_College_Name, jsonObject2.getString(TAG_Host_College_Name));
                                common.put(TAG_job_fair_id, jsonObject2.getString(TAG_job_fair_id));

                                hostcollegenameArraylist.add(common);
                            }
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                                HashMap<String, String> common = new HashMap<>();

                                common.put(TAG_Total_Student_Count, jsonObject2.getString(TAG_Total_Student_Count));

                                studentcountArraylist.add(common);
                            }
                            for (int i = 0; i < jsonArray2.length(); i++) {
                                JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                                HashMap<String, String> common = new HashMap<>();

                                common.put(TAG_Total_Company_Count, jsonObject2.getString(TAG_Total_Company_Count));

                                companycountArraylist.add(common);
                            }
                            for (int i = 0; i < jsonArray3.length(); i++) {
                                JSONObject jsonObject2 = jsonArray3.getJSONObject(i);
                                HashMap<String, String> common = new HashMap<>();
                                common.put(TAG_Total_College_Count, jsonObject2.getString(TAG_Total_College_Count));

                                collegecountArraylist.add(common);
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                HashMap<String, String> common = new HashMap<>();
                                HashMap<String, String> commonclgname = (HashMap<String, String>) hostcollegenameArraylist.get(i);
                                HashMap<String, String> commonstudent = (HashMap<String, String>) studentcountArraylist.get(i);
                                HashMap<String, String> commoncompany = (HashMap<String, String>) companycountArraylist.get(i);
                                HashMap<String, String> commoncollege = (HashMap<String, String>) collegecountArraylist.get(i);
                                common.put("job_fair_id", commonclgname.get(TAG_job_fair_id));
                                common.put("colgnm", commonclgname.get(TAG_Host_College_Name));
                                common.put("studentcount", commonstudent.get(TAG_Total_Student_Count));
                                common.put("companycount", commoncompany.get(TAG_Total_Company_Count));
                                common.put("collegecount", commoncollege.get(TAG_Total_College_Count));
                                jobfairdetailArraylist.add(common);

                            }

                            pd.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        //company details in recycler view
                                        adapter = new College_Home_Adapter(jobfairdetailArraylist, College_Home.this);
                                        LinearLayoutManager llm = new LinearLayoutManager(College_Home.this);
                                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                                        recyclerView.setLayoutManager(llm);
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setAdapter(adapter);
                                    }
                                    catch (Exception e){
                                        Log.e("Recyleview Error", "run: "+e.getMessage());
                                    }
                                }
                            });
                        }
                        else {
                            recyclerView.setVisibility(View.INVISIBLE);
                            lldatanotfound.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        Log.e("JSon Error", "onResponse: "+e.getMessage());
                    } catch (IOException e) {
                        Log.e("IO Error", "onResponse: "+e.getMessage());
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_profile:
                Intent i = new Intent(College_Home.this, CollegeProfile.class);
                i.putExtra("cid", this.id);
                startActivity(i);
                break;
            case R.id.nav_College_Manage_Course:
//                Intent i2 = new Intent(College_Home.this, College_Course_Manage.class);
//                startActivity(i2);
                break;
            case R.id.nav_College_Edit_Profile:
//                Intent i3 = new Intent(College_Home.this, College_Edit_Profile.class);
//                startActivity(i3);
                break;
            case R.id.nav_logout:
                session.Destroy();
                Intent i4 = new Intent(this, LoginActivity.class);
                startActivity(i4);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class getCollegeData extends AsyncTask<Void, JSONArray, JSONArray> {
        private SharedPreferences CollegeSharePref;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CollegeSharePref = getSharedPreferences("clg", 0);
            id = Integer.valueOf(CollegeSharePref.getString("clg_id", "1"));
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {

            try {
                HttpUrl.Builder builder = HttpUrl.parse(getResources().getString(R.string.su_uri) + "API.php")
                        .newBuilder();
                builder.addQueryParameter("action", "getclgdata");
                builder.addQueryParameter("clg_id", String.valueOf(id));
                Request request = new Request.Builder()
                        .url(builder.build())
                        .build();
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                JSONObject jobj = new JSONObject(response.body().string());
                JSONArray jsonArray = jobj.getJSONArray("data");
                Log.e("DATA", "doInBackground: " + jsonArray);
                return jsonArray;
            } catch (Exception e) {
                Log.e("clgData Error", "doInBackground: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonElements) {
            Log.e("Data", "onPostExecute: " + jsonElements);
            if (!(jsonElements == null)) {
                try {
                    JSONObject object = jsonElements.getJSONObject(0);
//                    Log.e("object", "onPostExecute: " + object);
                    clg_name = (TextView) findViewById(R.id.clg_name);
                    clg_loc = (TextView) findViewById(R.id.clg_location);
                    clg_name.setText(object.getString("College_name"));
                    clg_loc.setText(object.getString("College_location"));
                } catch (JSONException e) {
                    Log.e("JSON Error", "onPostExecute: " + e.getMessage());
                }
            } else {
                Snackbar.make((College_Home.this).findViewById(R.id.clg_content), "Something went Wrong for Json Data!", Snackbar.LENGTH_INDEFINITE).show();
            }
        }
    }
}
