package com.example.karmarkarsourabh.surat_job_expo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.Modal.JobFair.jobfair;

public class JobFairSingle extends AppCompatActivity {

    private TextView tv;
    private jobfair mJobfair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_fair_single);

        tv = (TextView) findViewById(R.id.chk);
        Intent mintent = getIntent();
        mJobfair =(jobfair) mintent.getExtras().getSerializable("obj");
        tv.setText(mJobfair.getData().get(mintent.getIntExtra("pos",0)).getClg_name());

    }
}
