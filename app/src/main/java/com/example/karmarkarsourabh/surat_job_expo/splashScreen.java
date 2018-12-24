package com.example.karmarkarsourabh.surat_job_expo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class splashScreen extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mviewPager;
    private LinearLayout dots;
    private TextView[] mdots;
    private TextView skip_text;
    private ImageView skip_img;
    private SharedPreferences introsharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        introsharePref = getSharedPreferences("intro", 0);
        if (introsharePref.getBoolean("intro", false)) {
            startActivity(new Intent(splashScreen.this,LoginActivity.class));
        } else {
            mviewPager = (ViewPager) findViewById(R.id.introSlide);
            dots = (LinearLayout) findViewById(R.id.dotLayer);
            skip_text = (TextView) findViewById(R.id.skip_tv);
            skip_img = (ImageView) findViewById(R.id.skip_ic);

            sildeAdaptor adaptor = new sildeAdaptor(this);
            mviewPager.setAdapter(adaptor);
            dot_indicator(0);
            mviewPager.addOnPageChangeListener(listener);

            skip_img.setOnClickListener(this);
            skip_text.setOnClickListener(this);
        }
    }

    private void dot_indicator(int pos) {
        mdots = new TextView[4];
        dots.removeAllViews();

        for (int i = 0; i < mdots.length; i++) {

            mdots[i] = new TextView(this);
            mdots[i].setText(Html.fromHtml("&#8226;"));
            mdots[i].setTextSize(35);
            mdots[i].setTextColor(getResources().getColor(R.color.primary_text));

            dots.addView(mdots[i]);
        }

        if (mdots.length > 0) {
            mdots[pos].setTextColor(getResources().getColor(R.color.transperent_card));
        }
    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            dot_indicator(i);
            if(i==3){
                skip_text.setText("Next");
            }else {
                skip_text.setText("Skip");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    @Override
    public void onClick(View v) {
        introsharePref = getSharedPreferences("intro", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = introsharePref.edit();
        editor.putBoolean("intro", true);
        editor.commit();
        editor.apply();
        startActivity(new Intent(splashScreen.this, LoginActivity.class));
    }
}
