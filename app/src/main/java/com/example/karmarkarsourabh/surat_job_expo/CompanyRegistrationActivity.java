package com.example.karmarkarsourabh.surat_job_expo;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CompanyRegistrationActivity extends AppCompatActivity {

    DatePickerDialog dtpicker;
    Calendar calendar;
    EditText company_name,person_name,address,weblink,contactno,email,established_on,pass,confirm_pass,contact_person;
    TextView Errmsg,imgname;
    ImageView companyUploadedPic;

    OkHttpClient client;
    String BASE_URL,PicturePath,URL;

    boolean haveExternalPermission = false;
    Uri selectedImageUri;
    int REQ_SELECT_PIC = 1, REQ_READ_EXTERNAL_STORAGE = 3;
    //JSON node names
    public static final String TAG_SUCEESS = "success";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_registration);

        //get all view instance
        company_name=findViewById(R.id.edittext_companyregistration_companyname);
        person_name=findViewById(R.id.edittext_companyregistration_contactpersonname);
        address=findViewById(R.id.edittext_companyregistration_address);
        weblink=findViewById(R.id.edittext_companyregistration_websitelink);
        contactno=findViewById(R.id.edittext_companyregistration_contactno);
       email=findViewById(R.id.edittext_companyregistration_email);
        pass=findViewById(R.id.edittext_companyregistration_password);
        confirm_pass=findViewById(R.id.editText_companyRegister_confirmpassword);
        established_on=findViewById(R.id.edittext_companyregistration_establishedOn);
        Errmsg=findViewById(R.id.textview_companyRegistration_errormsg);
        imgname=findViewById(R.id.textview_companyRegister_logo);
        companyUploadedPic=findViewById(R.id.imageView_companyRegister_companyUploadedPicture);

        BASE_URL=getResources().getString(R.string.BASE_URL);
        client=new OkHttpClient();
        //To open date picker on click of edit text
        openDatePicker();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openDatePicker()
    {
        calendar = Calendar.getInstance();

        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        dtpicker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //as date is selected we set it in our calender object
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        updateText();
                    }
                }, y, m, day);


        established_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dtpicker.show();

            }
        });
        established_on.setShowSoftInputOnFocus(false);

    }

    void updateText() {
        //For setting in textview
        String format = "yyyy/MM/dd";

        SimpleDateFormat sdf = new SimpleDateFormat(format);


        established_on.setText(sdf.format(calendar.getTime()));
    }

    public void InsertCompany(View view) {

        URL=BASE_URL+"company_insert.php";
        String err="";
        if(established_on.getText().toString().equalsIgnoreCase(""))
        {
         err="Select Established date please";
        }
        else {
            if (pass.getText().toString().equalsIgnoreCase(confirm_pass.getText().toString()) && !pass.getText().toString().equalsIgnoreCase("")) {
                new CompanyRegisAsync().execute();
            } else {
                err = "Confirm password does not match";
            }
        }
        Errmsg.setText(err);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_READ_EXTERNAL_STORAGE);

        } else {
            haveExternalPermission = true;

        }
        return haveExternalPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_READ_EXTERNAL_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            haveExternalPermission = true;
        }
    }


    public void ChooseImage(View view) {

        if (checkPermission() == true) {
            //Select image  from gallery
            Intent selectPicIntent = new Intent();
            selectPicIntent.setType("image/*");
            selectPicIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(selectPicIntent, "Select Picture"), REQ_SELECT_PIC);
        } else {
            checkPermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_SELECT_PIC && resultCode==RESULT_OK) {
            selectedImageUri = data.getData();
            try {

                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                companyUploadedPic.setImageBitmap(bmp);

                Log.d("image path", selectedImageUri.getPath());
                PicturePath = getPicturePathFromUri(selectedImageUri);
                imgname.setText(PicturePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    /*
    * To read absolute path of the selected image
    * */
    private String getPicturePathFromUri(Uri uri) {
        Log.d("URI", uri.toString());
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        Log.d("FullDocument_id", document_id);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        Log.d("Documt_id afr substring", document_id);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + "= ?", new String[]{document_id}, null
        );
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        Log.d("Path from getPicmethod ", path);
        return path;
    }

    public class CompanyRegisAsync extends AsyncTask<String,String,String>
    {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(CompanyRegistrationActivity.this);
            pd.setMessage("Your Data is being processed");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {

            File imgFile = new File(PicturePath);


            RequestBody formbody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("Company_name",company_name.getText().toString())
                    .addFormDataPart("Company_address",address.getText().toString())
                    .addFormDataPart("Company_email",email.getText().toString())
                    .addFormDataPart("Company_contact_person",person_name.getText().toString())
                    .addFormDataPart("Company_contact",contactno.getText().toString())
                    .addFormDataPart("Company_webURL",weblink.getText().toString())
                    .addFormDataPart("Company_establishedOn",established_on.getText().toString())
                    .addFormDataPart("Company_password",pass.getText().toString())
                    .addFormDataPart("Company_logo",imgFile.getName(), RequestBody.create(MediaType.parse("image/jpeg"), imgFile))
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
                        final String msg = jobj.getString("message");
                        Log.d("Status code",success+"");
                        Log.d("Status msg",msg);
                        if(success==1)
                        {

                            Intent i = new Intent(CompanyRegistrationActivity.this,LoginActivity.class);
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
