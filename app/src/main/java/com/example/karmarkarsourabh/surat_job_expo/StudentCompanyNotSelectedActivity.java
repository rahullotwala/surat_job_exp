package com.example.karmarkarsourabh.surat_job_expo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class StudentCompanyNotSelectedActivity extends AppCompatActivity {

    TextView drivename;
    Intent intent;
    String job_fair_id;
    private String TAG_JOB_FAIR_ID="job_fair_id";
    private String TAG_COLLEGE_NAME="college_name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_company_not_selected);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent=getIntent();
        drivename=findViewById(R.id.textview_studentCompanyNotSelected_drivename);
        drivename.setText(intent.getStringExtra(TAG_COLLEGE_NAME));
        job_fair_id=intent.getStringExtra(TAG_JOB_FAIR_ID);

    }

    public void RedirectToselectCompany(View view) {

        Intent selectCompanyIntent=new Intent(this,StudentCompanySelectionActivity.class);
        selectCompanyIntent.putExtra(TAG_JOB_FAIR_ID,job_fair_id);
        selectCompanyIntent.putExtra(TAG_COLLEGE_NAME,intent.getStringExtra(TAG_COLLEGE_NAME));
        startActivity(selectCompanyIntent);

    }
}
