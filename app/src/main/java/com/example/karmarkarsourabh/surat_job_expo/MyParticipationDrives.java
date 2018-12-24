package com.example.karmarkarsourabh.surat_job_expo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MyParticipationDrives extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_participation_drives);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarpart);
        toolbar.setTitle("Campus Drive");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
