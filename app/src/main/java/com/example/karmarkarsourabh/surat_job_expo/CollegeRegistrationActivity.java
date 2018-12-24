package com.example.karmarkarsourabh.surat_job_expo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CollegeRegistrationActivity extends AppCompatActivity {
    EditText name,address,email,weblink,contact,password,confirmpasword;
    TextView id,Errmsg;
    OkHttpClient client;
    String URL;
    ProgressDialog pd;
    //JSON node names
    public static final String TAG_SUCEESS = "success";
    public String BASE_URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_registration);
        id  = (TextView)findViewById(R.id.textView_collegeRegister_collegeid);
       Errmsg=findViewById(R.id.textview_collegeRegistration_errormsg);
        name = (EditText)findViewById(R.id.editText_collegeRegister_collegename);
        address = (EditText)findViewById(R.id.editText_collegeRegister_collegeaddress);
        email = (EditText)findViewById(R.id.editText_collegeRegister_collegemail);
        weblink = (EditText)findViewById(R.id.editText_collegeRegister_collegelink);
        contact = (EditText)findViewById(R.id.editText_collegeRegister_contact);
        password = (EditText)findViewById(R.id.editText_collegeRegister_password);
        confirmpasword = (EditText)findViewById(R.id.editText_collegeRegister_confirmpassword);
        BASE_URL = (getResources().getString(R.string.BASE_URL));
        client = new OkHttpClient();
    }

    public void InsertCollege(View view) {
        URL = BASE_URL+"college_insert.php";

        if(!contact.getText().toString().matches("/^[789][0-9]{9}$/"))
        {
            Errmsg.setText("Contact should be digits only");
        }

        if (password.getText().toString().equalsIgnoreCase(confirmpasword.getText().toString()) && !password.getText().toString().equalsIgnoreCase("")) {
            new myAsyncTaskToInsertCollege().execute();
        }
        else
        {
Errmsg.setText("Confirm password does not match");
        }
    }

    class myAsyncTaskToInsertCollege extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(CollegeRegistrationActivity.this);
            pd.setMessage("Your Data is being processed");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(final String... strings) {

            RequestBody formbody = new FormBody.Builder()
                    .add("College_name",name.getText().toString())
                    .add("College_location",address.getText().toString())
                    .add("College_email",email.getText().toString())
                    .add("College_webURL",weblink.getText().toString())
                    .add("College_contact",contact.getText().toString())
                    .add("College_password",password.getText().toString())
                    .build();
            final Request req = new Request.Builder()
                    .url(URL)
                    .post(formbody)
                    .build();

            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e)
                {
                    Log.d("Msg",e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String rs = response.body().string();
                        Log.d("response",rs);
                        JSONObject jobj = new JSONObject(rs);
                        int success = jobj.getInt(TAG_SUCEESS);
                        String msg = jobj.getString("message");
                        Log.d("Status code",success+"");
                        Log.d("Status msg",msg);
                        if(success==1)
                        {
                            Intent i = new Intent(CollegeRegistrationActivity.this,LoginActivity.class);
                            startActivity(i);
                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
        }
    }


}
