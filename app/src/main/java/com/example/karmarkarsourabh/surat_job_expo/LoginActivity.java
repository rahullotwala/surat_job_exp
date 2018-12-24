package com.example.karmarkarsourabh.surat_job_expo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karmarkarsourabh.surat_job_expo.Utill.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    Spinner categoryspinner;
    TextView t1;
    EditText email, password;
    View sv;

    OkHttpClient client;


    //Which category login is this
    // 1 for student
    // 2 for company
    // 3 for college
    int cat_code = 0;

    private SharedPreferences Stud_sharepref;
    private SharedPreferences userShare;
    private Button Login_btn;
    private Spinner reg_spin;
    private SharedPreferences CollegeSharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userShare = getSharedPreferences("user", 0);
        if (userShare.getBoolean("login", false)) {
            if (userShare.getString("user", "stud").equals("stud")) {
                startActivity(new Intent(LoginActivity.this, StudentHomeActivity.class));
            } else if (userShare.getString("user", "stud").equals("company")) {
                startActivity(new Intent(LoginActivity.this, StudentHomeActivity.class));
            } else if (userShare.getString("user", "stud").equals("College")) {
                startActivity(new Intent(LoginActivity.this, College_Home.class));
            }
        } else {

//          Declaration
            t1 = (TextView) findViewById(R.id.textView_login_forgotpassword);

            email = findViewById(R.id.editText_login_email);
            password = findViewById(R.id.editText_login_password);
            categoryspinner = findViewById(R.id.spinner_login_category);
            reg_spin = (Spinner) findViewById(R.id.reg_spinner);

            Login_btn = (Button) findViewById(R.id.login_btn);

            Login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sv = v;
                    new doLogin().execute();
                }
            });

//          Implimentation
            categoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                View v;

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (categoryspinner.getSelectedItem().toString().equals("Student")) {
                        cat_code = 1;
                    } else if (categoryspinner.getSelectedItem().toString().equals("Company")) {
                        cat_code = 2;
                    } else if (categoryspinner.getSelectedItem().toString().equals("College")) {
                        cat_code = 3;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Snackbar.make(v, "Select any type", Snackbar.LENGTH_SHORT);
                }
            });

            reg_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                View v;

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selected_item = reg_spin.getSelectedItem().toString();

                    if (selected_item.equals("Student")) {
                        startActivity(new Intent(LoginActivity.this, StudentRegistrationActivity.class));
                    } else if (selected_item.equals("Company")) {
                        startActivity(new Intent(LoginActivity.this, CompanyRegistrationActivity.class));
                    } else if (selected_item.equals("College")) {
                        startActivity(new Intent(LoginActivity.this, CollegeRegistrationActivity.class));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Snackbar.make(v, "Select any type", Snackbar.LENGTH_SHORT);

                }
            });


        }
    }

    class doLogin extends AsyncTask<Void, Void, Void> {
        private Dialog dialog;
        private boolean isAuth;

        @Override
        protected void onPreExecute() {

            if (!email.getText().toString().equals("") && !password.getText().toString().equals("")) {

                if (!categoryspinner.getSelectedItem().toString().equals("Select")) {
                    dialog = new ProgressDialog(LoginActivity.this);
                    dialog.setTitle("Checking Credentials...");
                    dialog.show();
                } else {
                    Snackbar.make(sv, "Selecte Login Type", Snackbar.LENGTH_LONG).show();
                }

            } else {
                email.setError("email");
                password.setError("password");

            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isAuth) {
                dialog.dismiss();
                if (cat_code == 1) {
                    startActivity(new Intent(LoginActivity.this, StudentHomeActivity.class));
                    Snackbar.make(sv, "Successfully Login", Snackbar.LENGTH_LONG).show();
                } else if (cat_code == 2) {
//                    company code
                } else if (cat_code == 3) {
                    startActivity(new Intent(LoginActivity.this, College_Home.class));
                    Snackbar.make(sv, "Successfully Login", Snackbar.LENGTH_LONG).show();
                }
            } else {
                dialog.dismiss();
                Snackbar.make(sv, "You have enter Wrong Credentials", Snackbar.LENGTH_LONG).show();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("do", "doInBackground: ");
            try {
                HttpUrl.Builder builder = HttpUrl.parse(getResources().getString(R.string.su_uri) + "API.php")
                        .newBuilder();
                builder.addQueryParameter("action", "lgoin");
                builder.addQueryParameter("em", email.getText().toString());
                builder.addQueryParameter("pass", password.getText().toString());
                builder.addQueryParameter("user", String.valueOf(cat_code));
                Request request = new Request.Builder()
                        .url(builder.build())
                        .build();
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();

                JSONObject jobj = new JSONObject(response.body().string());
                if (jobj.has("data")) {
                    JSONArray jsonArray = jobj.getJSONArray("data");
                    if (jsonArray.length() > 0) {

                        isAuth = true;
                        Log.d("Loginjson", jobj.getString("data"));
//                        user share preference
                        userShare = getSharedPreferences("user", 0);
                        SharedPreferences.Editor ueditor = userShare.edit();
                        ueditor.putBoolean("login", true);

                        if (cat_code == 1) {

//                          user json
                            JSONObject obj = jsonArray.getJSONObject(0);
                            new Session(LoginActivity.this).SetLoginID(obj.getString("Job_seeker_id"));
                            Stud_sharepref = getSharedPreferences("stud", 0);
                            SharedPreferences.Editor editor = Stud_sharepref.edit();
                            editor.putString("stud_id", obj.getString("Job_seeker_id"));
                            editor.commit();
                            editor.apply();

                            ueditor.putString("user", "stud");
                            ueditor.commit();
                            ueditor.apply();
                        } else if (cat_code == 2) {

                        } else if (cat_code == 3) {
//                          college json
                            JSONObject obj = jsonArray.getJSONObject(0);
                            new Session(LoginActivity.this).SetLoginID(String.valueOf(cat_code));
                            CollegeSharePref = getSharedPreferences("clg", 0);
                            SharedPreferences.Editor editor = CollegeSharePref.edit();
                            editor.putString("clg_id", obj.getString("College_id"));
                            editor.commit();
                            editor.apply();

                            ueditor.putString("user", "College");
                            ueditor.commit();
                            ueditor.apply();
                        }

                    } else {
                        isAuth = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}


