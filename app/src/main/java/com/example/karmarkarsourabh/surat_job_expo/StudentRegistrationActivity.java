package com.example.karmarkarsourabh.surat_job_expo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.GETSET.College;
import com.example.karmarkarsourabh.surat_job_expo.GETSET.Course;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.gson.JsonObject;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class StudentRegistrationActivity extends AppCompatActivity implements TextWatcher, View.OnFocusChangeListener {

    EditText firstname, lastname, contact, email, cgpa, password, confirmpassword, resumename;
    ProgressDialog pd;
    Spinner selectCollege, selectCourse;
    RadioGroup gen, other;
    RadioButton selectedRadio, isOtherRadio;
    CircleImageView studentUploadedPic;
    PDFView pdfView;
    Button btn;
    CheckBox tnc;
    Uri uri;

    boolean isOther = false, haveExternalPermission = false;
    OkHttpClient client;
    String URL;

    String gender = "Female", PicturePath="", ResumePath;
    Uri selectedImageUri, selectedFileUri;

    int Selected_College_id, Selected_Course_id, College_Course_id, pageNumber = 0;
    int REQ_SELECT_PIC = 1, REQ_SELECT_RESUME = 2, REQ_READ_EXTERNAL_STORAGE = 3;
    ArrayList<College> collegeArrayList;
    ArrayList<Course> courseArrayList;

    //Json Node Names
    public static final String TAG_SUCCESS = "success";
    public static final String ARRAY_NAME_COLLEGE = "colleges";
    public static final String ARRAY_NAME_COURSE = "courses";
    public static final String ARRAY_NAME_COLLEGECOURSE = "college_course";
    public static final String COLLEGE_ID = "College_id";
    public static final String COLLEGE_NAME = "College_name";
    public static final String COURSE_ID = "Course_id";
    public static final String COURSE_NAME = "Course_name";


    public String BASE_URL;
    private View v;
    private ImageView cf_psw;
    private int id;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        firstname = (EditText) findViewById(R.id.editText_studentregister_firstname);
        lastname = (EditText) findViewById(R.id.editText_studentregister_lastname);
        contact = (EditText) findViewById(R.id.editText_studentregister_contactnumber);
        email = (EditText) findViewById(R.id.editText_studentregister_email);
        password = (EditText) findViewById(R.id.editText_studentregister_password);
        confirmpassword = (EditText) findViewById(R.id.editText_studentregister_confirmpassword);
        cgpa = (EditText) findViewById(R.id.editText_studentregister_cgpa);
        selectCollege = (Spinner) findViewById(R.id.spinner_studentregister_selectcollege);
        selectCourse = (Spinner) findViewById(R.id.spinner_studentregister_selectbranch);
        gen = (RadioGroup) findViewById(R.id.radioGroup);
        other = (RadioGroup) findViewById(R.id.radioGroup_studentregistration_isOther);
        resumename = (EditText) findViewById(R.id.textview_studentregister_resume);
        studentUploadedPic = (CircleImageView) findViewById(R.id.imageView_studentregister_studentUploadedPicture);
        tnc = (CheckBox) findViewById(R.id.tnc_chB);
        cf_psw = (ImageView) findViewById(R.id.crf_psw);


        resumename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseFile(v);
            }
        });

        firstname.addTextChangedListener(this);
        firstname.setOnFocusChangeListener(this);
        lastname.addTextChangedListener(this);
        lastname.setOnFocusChangeListener(this);
        contact.addTextChangedListener(this);
        contact.setOnFocusChangeListener(this);
        email.addTextChangedListener(this);
        email.setOnFocusChangeListener(this);
        cgpa.addTextChangedListener(this);
        cgpa.setOnFocusChangeListener(this);
        resumename.addTextChangedListener(this);
        resumename.setOnFocusChangeListener(this);
        password.addTextChangedListener(this);
        password.setOnFocusChangeListener(this);
        confirmpassword.addTextChangedListener(this);
        confirmpassword.setOnFocusChangeListener(this);

        BASE_URL = (getResources().getString(R.string.BASE_URL));
        client = new OkHttpClient();//.Builder().connectTimeout(150, TimeUnit.SECONDS).build();

        FillCollegeSpinner();

        checkPermission();
    }


    public void FillCollegeSpinner() {
        URL = BASE_URL + "college_display_all.php";

        collegeArrayList = new ArrayList<College>();

        //API CALL
        Request req = new Request.Builder().url(URL).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("You got Error", e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String rs = response.body().string();

                            JSONObject jobj = new JSONObject(rs);
                            int success = jobj.getInt(TAG_SUCCESS);

                            if (success == 1) {

                                JSONArray jarray = jobj.getJSONArray(ARRAY_NAME_COLLEGE);
                                for (int i = 0; i < jarray.length(); i++) {
                                    JSONObject obj = jarray.getJSONObject(i);

                                    College college = new College(obj.getInt(COLLEGE_ID), obj.getString(COLLEGE_NAME));
                                    collegeArrayList.add(college);
                                }
                                ArrayAdapter<College> adp = new ArrayAdapter<College>(StudentRegistrationActivity.this, android.R.layout.simple_spinner_dropdown_item, collegeArrayList);
                                selectCollege.setAdapter(adp);

                                selectCollege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        College college = (College) parent.getItemAtPosition(position);
                                        Selected_College_id = college.college_id;
                                        FillCourseSpinner(college.college_id);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else {
                                Log.e("Msg", rs);
                            }

                        } catch (IOException e) {
                            Log.e("IOError", "run: " + e.getMessage());
                        } catch (JSONException e) {
                            Log.e("IOError", "run: " + e.getMessage());
                        }
                    }
                });


            }
        });


    }

    public void FillCourseSpinner(int clg_id) {
        /*Fill course with its id and name
         * at the time of insertion fetch college_course_id of that course_id and college_id
         * from college_course_tb
         * */
        URL = BASE_URL + "course_display_by_college_id.php";
        Log.d("URL", URL);
        courseArrayList = new ArrayList<Course>();
        RequestBody formbody = new FormBody.Builder()
                .add("college_id", clg_id + "")
                .build();
        final Request req = new Request.Builder()
                .url(URL)
                .post(formbody)
                .build();


        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Msg", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String rs = null;
                        try {
                            rs = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("response", rs);
                        try {
                            JSONObject jobj = new JSONObject(rs);
                            int success = jobj.getInt(TAG_SUCCESS);

                            Log.e("Status code", success + "");

                            if (success == 1) {

                                JSONArray jarray = jobj.getJSONArray(ARRAY_NAME_COURSE);
                                for (int i = 0; i < jarray.length(); i++) {
                                    JSONObject obj = jarray.getJSONObject(i);

                                    Course course = new Course(obj.getInt(COURSE_ID), obj.getString(COURSE_NAME));
                                    courseArrayList.add(course);

                                }

                                ArrayAdapter<Course> adp = new ArrayAdapter<Course>(StudentRegistrationActivity.this, android.R.layout.simple_spinner_dropdown_item, courseArrayList);
                                selectCourse.setAdapter(adp);
                                selectCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        Course course = (Course) parent.getItemAtPosition(position);
                                        Selected_Course_id = course.course_id;
                                        //Toast.makeText(StudentRegistrationActivity.this,"selected college is "+ Selected_College_id + "Selected course is " + Selected_Course_id,Toast.LENGTH_SHORT).show();
                                        FetchCollegeCourseId();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }

                        } catch (JSONException e) {
                            Log.e("Json error", "run: " + e.getMessage());
                        }

                    }
                });


            }
        });


    }

    public void FetchCollegeCourseId() {
        //Fetch college_course_id based on the course_id and college_id


        URL = BASE_URL + "collegecourse_display_by_college_and_course_id.php";
        RequestBody formbodycc = new FormBody.Builder()
                .add("college_id", Selected_College_id + "")
                .add("course_id", Selected_Course_id + "")
                .build();
        final Request reqcc = new Request.Builder()
                .url(URL)
                .post(formbodycc)
                .build();

        client.newCall(reqcc).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Msg", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String rs = null;
                        try {
                            rs = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("response for clg_course", rs);
                        try {
                            JSONObject jobj = new JSONObject(rs);
                            int success = jobj.getInt(TAG_SUCCESS);

                            Log.e("st_code college_course", success + "");

                            if (success == 1) {

                                JSONArray jarray = jobj.getJSONArray(ARRAY_NAME_COLLEGECOURSE);
                                for (int i = 0; i < jarray.length(); i++) {
                                    JSONObject obj = jarray.getJSONObject(i);
                                    College_Course_id = obj.getInt("college_course_id");
                                    // Toast.makeText(StudentRegistrationActivity.this, "Selected cc id " + College_Course_id + " Gender is" + gender, Toast.LENGTH_SHORT).show();
                                }

                            }

                        } catch (JSONException e) {
                            Log.e("Json Error 343", "run: " + e.getMessage());
                        }

                    }
                });


            }
        });


    }

    public void registerstudent(View view) {

        v = view;
        URL = BASE_URL + "student_registration.php";


        if (!(firstname.getText().toString().equals(""))) {
            if (!(lastname.getText().toString().equals(""))) {
                if (!(contact.getText().toString().equals(""))) {
                    if (!(email.getText().toString().equals(""))) {
                        if (!(password.getText().toString().equals(""))) {
                            if (!(confirmpassword.getText().toString().equals(""))) {
                                if (!(cgpa.getText().toString().equals(""))) {
                                    if (password.getText().toString().equals(confirmpassword.getText().toString())) {
                                        if (tnc.isChecked()) {
                                            new myAsyncTaskToInsertStudent().execute();
                                        } else {
                                            Snackbar.make(view, "Accept the term & Conditon to procced", Snackbar.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Snackbar.make(view, "Confirm password does not match", Snackbar.LENGTH_LONG).show();
                                    }
                                } else {
                                    cgpa.setError("cgpa is requierd to register");
                                }
                            } else {
                                confirmpassword.setError("confirm password is requierd to register");
                            }
                        } else {
                            password.setError("password! is required to register");
                        }
                    } else {
                        email.setError("email! is required to register");
                    }
                } else {
                    contact.setError("contact information! is required to register");
                }
            } else {
                lastname.setError("last name! is required to register");
            }
        } else {
            firstname.setError("first name! is required to register");
        }

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

    @RequiresApi(api = Build.VERSION_CODES.M)
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

        if (requestCode == REQ_SELECT_PIC && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();
            try {

                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                studentUploadedPic.setImageBitmap(bmp);


                PicturePath = getPicturePathFromUri(selectedImageUri,requestCode);
                Log.e("image path", PicturePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQ_SELECT_RESUME && resultCode == RESULT_OK) {
            uri = data.getData();
            ResumePath = getPicturePathFromUri(uri,requestCode);
            Log.e("pdf Path", "onActivityResult: "+ResumePath);
            resumename.setText(ResumePath);

        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getPicturePathFromUri(Uri uri, int rcode) {
        if(rcode==REQ_SELECT_PIC){

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
        } else if (rcode == REQ_SELECT_RESUME) {
            Log.e("estorage", ""+uri);
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];
        Log.e("estorage", "getPicturePathFromUri: "+type);
            if ("primary".equalsIgnoreCase(type)) {
                Log.e("estorage", "+++ "+Environment.getExternalStorageDirectory() + "/" + split[1]);
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
        }
        return "";
    }
    public void ChooseFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, REQ_SELECT_RESUME);
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    //To view the uploaded pdf
    public void ViewDocument(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(StudentRegistrationActivity.this);
        View v = LayoutInflater.from(StudentRegistrationActivity.this).inflate(R.layout.student_registration_pdfviwer__alert_layout, null);
        alertDialog.setView(v);
        pdfView = v.findViewById(R.id.pdfviewer_studentregistration_uploadedResume);
        if (ResumePath != null && ResumePath != "") {
            final File pdfFile = new File(ResumePath);
            pdfView.fromFile(pdfFile)
                    .defaultPage(pageNumber)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            pageNumber = page;
                            setTitle(String.format("%s %s / %s", pdfFile.getName(), page + 1, pageCount));
                        }
                    })
                    .enableAnnotationRendering(true)
                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {

                        }
                    })
                    .scrollHandle(new DefaultScrollHandle(this))
                    .load();
        } else {
            alertDialog.setMessage("Please Select File First");
        }
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        validation(id);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        id = v.getId();
        Log.e("CheckID", "onFocusChange: " + id);
    }

    private void validation(int ItemId) {

        switch (ItemId) {

            case R.id.editText_studentregister_firstname:
                Log.e("CheckID", "onFirstname: " + id);
                if (!(firstname.getText().toString().matches("[A-Za-z ]{4,15}"))) {
                    firstname.setError("Name must have atlist 4 character!");
                }
                break;
            case R.id.editText_studentregister_lastname:
                if (!(lastname.getText().toString().matches("[A-Za-z ]{4,15}"))) {
                    lastname.setError("Name must have atlist 4 character!");
                }
                break;
            case R.id.editText_studentregister_contactnumber:
                if (!(contact.getText().toString().matches("[0-9 ]{10}"))) {
                    contact.setError("must have 10 number of digit and only number are allowed!");
                }
                break;
            case R.id.editText_studentregister_email:
                if (!(email.getText().toString().matches("^[a-z0-9 ]+(@)[a-z]+(.)[a-z]+$"))) {
                    email.setError("email is not valid eg : someone@domai.abc");
                }
                break;
            case R.id.editText_studentregister_confirmpassword:
            case R.id.editText_studentregister_password:
                if (!(password.getText().toString().matches("[A-Za-z0-9@_ ]{8,20}"))) {

                    password.setError("password must a capital letter a small letter and lenght must be atlist 8");

                } else {
                    if (!confirmpassword.getText().toString().equals(password.getText().toString())) {
                        confirmpassword.setError("Confirm password does not match");
                        cf_psw.setImageDrawable(getResources().getDrawable(R.drawable.ic_password));
                    } else {
                        cf_psw.setImageDrawable(getResources().getDrawable(R.drawable.ic_right_mark));
                    }
                }
                break;
            case R.id.editText_studentregister_cgpa:

                if (!(cgpa.getText().toString().matches("[0-9. ]{3,4}"))) {
                    cgpa.setError("cgpa must have fractional number eg : 9.25");
                }
                break;
        }
    }

    class myAsyncTaskToInsertStudent extends AsyncTask<String, String, String> {
        public boolean isEmail;
        private int status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(StudentRegistrationActivity.this);
            pd.setMessage("Thankyou for registration! we where happy to help you. Wait for a while we prepared your dashboard.");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(final String... strings) {

            //For getting selected Gender
            selectedRadio = findViewById(gen.getCheckedRadioButtonId());

            switch (selectedRadio.getId()) {
                case R.id.radiobutton_studentregister_male:
                    gender = "Male";

                    break;
                case R.id.radiobutton_studentregister_female:
                    gender = "Female";

                    break;
            }

            //For getting if its student or passout

            isOtherRadio = findViewById(other.getCheckedRadioButtonId());
            switch (isOtherRadio.getId()) {
                case R.id.radiobutton_studentregister_student:
                    isOther = false;
                    break;
                case R.id.radiobutton_studentregister_other:
                    isOther = true;
                    break;

            }


            String name = firstname.getText().toString() + " " + lastname.getText().toString();
            //For making multipart request as to upload image and PDF file
            URL = getResources().getString(R.string.su_uri) + "API.php";
            int other = 0;
            if (isOther == true) {
                other = 1;
            }

            Log.e("Param", "doInBackground: " + College_Course_id + " " + name + " " + contact.getText().toString() + " " + email.getText().toString() + " " + gender + " " + password.getText().toString() + " " + cgpa.getText().toString() + " " + String.valueOf(other));

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {


                if (PicturePath.equals(null) || PicturePath.equals("")){
                    Log.e("profile Path", "doInBackground: "+PicturePath);
                    Log.e("profile", "doInBackground: "+"if" );
                    File pdfFile = new File(ResumePath);

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("action", "registration")
                            .addFormDataPart("College_course_id", College_Course_id + "")
                            .addFormDataPart("Student_name", name)
                            .addFormDataPart("Student_contact", contact.getText().toString())
                            .addFormDataPart("Student_email", email.getText().toString())
                            .addFormDataPart("Student_gender", gender)
                            .addFormDataPart("Student_profile", "")
                            .addFormDataPart("Student_resume", pdfFile.getName(), RequestBody.create(MediaType.parse("application/pdf"), pdfFile))
                            .addFormDataPart("Student_password", password.getText().toString())
                            .addFormDataPart("Student_cgpa", cgpa.getText().toString())
                            .addFormDataPart("isOther", String.valueOf(other))
                            .addFormDataPart("op", "insert")
                            .build();
                    final Request  req = new Request.Builder()
                            .url(URL)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(req).execute();
                    return response.body().string();
                }
                else {
                    Log.e("profile", "doInBackground: "+"if" );
                    File imgFile = new File(PicturePath);
                    File pdfFile = new File(ResumePath);

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("action", "registration")
                            .addFormDataPart("College_course_id", College_Course_id + "")
                            .addFormDataPart("Student_name", name)
                            .addFormDataPart("Student_contact", contact.getText().toString())
                            .addFormDataPart("Student_email", email.getText().toString())
                            .addFormDataPart("Student_gender", gender)
                            .addFormDataPart("Student_profile", imgFile.getName(), RequestBody.create(MediaType.parse("image/jpeg"), imgFile))
                            .addFormDataPart("Student_resume", pdfFile.getName(), RequestBody.create(MediaType.parse("application/pdf"), pdfFile))
                            .addFormDataPart("Student_password", password.getText().toString())
                            .addFormDataPart("Student_cgpa", cgpa.getText().toString())
                            .addFormDataPart("isOther", String.valueOf(other))
                            .addFormDataPart("op", "insert")
                            .build();
                    final Request req = new Request.Builder()
                            .url(URL)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(req).execute();
                    return response.body().string();
                }
            } catch (Exception e) {
                Log.e("json data error", "doInBackground: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                Log.e("Response", "onPostExecute: " + object);
                if (object.getInt("status") == 2) {
                    Snackbar.make(v, "The email is already taken!", Snackbar.LENGTH_LONG).show();
                } else if (object.getInt("status") == 1) {
                    Intent i = new Intent(StudentRegistrationActivity.this, LoginActivity.class);
                    i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(i);
                }
                pd.dismiss();
            } catch (JSONException e) {
                Log.e("JSonerror", "onPostExecute: " + e.getMessage() + " " + s);
            }
        }
    }
}
