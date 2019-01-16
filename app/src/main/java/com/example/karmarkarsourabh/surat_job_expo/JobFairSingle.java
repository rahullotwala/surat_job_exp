package com.example.karmarkarsourabh.surat_job_expo;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.Modal.JobFair.jobfair;

public class JobFairSingle extends AppCompatActivity {

    private TextView tv;
    private jobfair mJobfair;
    private TextView st_tv;
    private TextView en_tv;
    private TextView location_tv;
    private TextView email_tv;
    private TextView contact_tv;
    private CollapsingToolbarLayout toolbarLayout;
    private AppBarLayout barLayout;
    private Toolbar app_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_fair_single);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collap_toolbar_job_details);
        barLayout = (AppBarLayout) findViewById(R.id.app_bar_job_details);
        app_toolbar = (Toolbar) findViewById(R.id.job_detai_toolbar);
        app_toolbar = (Toolbar) findViewById(R.id.job_detai_toolbar);
        setSupportActionBar(app_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv = (TextView) findViewById(R.id.Drive_name);
        st_tv = (TextView) findViewById(R.id.JFstart_datetv);
        en_tv = (TextView) findViewById(R.id.JFend_datetv);
        location_tv = (TextView) findViewById(R.id.location_tv);
        email_tv = (TextView) findViewById(R.id.ema_tv);
        contact_tv = (TextView) findViewById(R.id.cont_tv);


        Intent mintent = getIntent();
        mJobfair = (jobfair) mintent.getExtras().getSerializable("obj");
        final String CollegeName = mJobfair.getData().get(mintent.getIntExtra("pos", 0)).getClg_name();
        tv.setText(CollegeName);

        st_tv.setText(mJobfair.getData().get(mintent.getIntExtra("pos", 0)).getStart_date());
        en_tv.setText(mJobfair.getData().get(mintent.getIntExtra("pos", 0)).getStud_end_date());
        location_tv.setText(mJobfair.getData().get(mintent.getIntExtra("pos", 0)).getClg_loc());
        email_tv.setText(mJobfair.getData().get(mintent.getIntExtra("pos", 0)).getClg_email());
        contact_tv.setText(mJobfair.getData().get(mintent.getIntExtra("pos", 0)).getClg_contat());

        barLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbarLayout.setTitle(CollegeName);
                    isShow = true;
                } else if (isShow) {
                    toolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }
}
