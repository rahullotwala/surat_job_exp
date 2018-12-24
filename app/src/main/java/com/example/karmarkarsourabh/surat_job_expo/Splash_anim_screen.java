package com.example.karmarkarsourabh.surat_job_expo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class Splash_anim_screen extends AppCompatActivity {

    private Thread mThread;
    private final int DURATION = 3500;

    private ImageView mImageViewCloud1;
    private ImageView mImageViewCloud2;
    private ImageView mImageViewCloud3;
    private ImageView mImageViewsurat;
    private ImageView mImageViewjobExpo;
    private ImageView mImageViewline1;
    private ImageView mImageViewline2;
    private ImageView mImageViewline3;
    private Animation AnimLeftRight;
    private Animation AnimfromTop;
    private Animation AnimfromBottom;
    private Thread mSplashThread;
    private Animation Animline;
    private Animation AnimCloud;
    private SharedPreferences introsharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_anim_screen);
//      Delay
        DoAnimation();
    }


    private void DoAnimation() {

        mImageViewCloud1 = (ImageView) findViewById(R.id.imageView7);
        mImageViewCloud2 = (ImageView) findViewById(R.id.imageView8);
        mImageViewCloud3 = (ImageView) findViewById(R.id.imageView9);
        mImageViewsurat = (ImageView) findViewById(R.id.imageView);
        mImageViewjobExpo = (ImageView) findViewById(R.id.imageView6);
        mImageViewline1 = (ImageView) findViewById(R.id.imageView3);
        mImageViewline2 = (ImageView) findViewById(R.id.imageView4);
        mImageViewline3 = (ImageView) findViewById(R.id.imageView5);
        AnimLeftRight = AnimationUtils.loadAnimation(this, R.anim.leftright);
        AnimfromTop = AnimationUtils.loadAnimation(this, R.anim.fromtop);
        AnimfromBottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        Animline = AnimationUtils.loadAnimation(this, R.anim.leftrightline);
        AnimCloud = AnimationUtils.loadAnimation(this, R.anim.leftrightcloud);
        mImageViewCloud1.setAnimation(AnimCloud);
        mImageViewCloud2.setAnimation(AnimLeftRight);
        mImageViewCloud3.setAnimation(AnimLeftRight);
        mImageViewsurat.setAnimation(AnimfromTop);
        mImageViewjobExpo.setAnimation(AnimfromBottom);
        mImageViewline1.setAnimation(Animline);
        mImageViewline2.setAnimation(Animline);
        mImageViewline3.setAnimation(Animline);

        mThread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        wait(DURATION);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        introsharePref = getSharedPreferences("intro", 0);
                        if (introsharePref.getBoolean("intro", false)) {
                            startActivity(new Intent(Splash_anim_screen.this, LoginActivity.class));
                        } else {
                            startActivity(new Intent(Splash_anim_screen.this, splashScreen.class));
                        }
                    }
                }
            }
        };
        mThread.start();
    }
}
