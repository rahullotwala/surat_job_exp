package com.example.karmarkarsourabh.surat_job_expo.Utill;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.example.karmarkarsourabh.surat_job_expo.Adaptor.College_Home_Adapter;
import com.example.karmarkarsourabh.surat_job_expo.R;

public class utill {
Context mContext;
View view;

    public utill(Context mContext,View v) {
        this.mContext = mContext;
        this.view = v;
    }

    public boolean internet_connection(){
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public void internet_Retry() {
        Snackbar snackbar = Snackbar.make(view,
                "No internet connection.",
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(ContextCompat.getColor(mContext,
                R.color.colorPrimary));
        snackbar.setAction(R.string.try_again, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recheck internet connection and call DownloadJson if there is internet
                if (!internet_connection()) {
                    internet_Retry();
                }
                else {
                    Toast.makeText(mContext, "Connected", Toast.LENGTH_SHORT).show();
                }
            }
        }).show();
    }
}