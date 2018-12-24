package com.example.karmarkarsourabh.surat_job_expo.Utill;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Archana on 10/22/2018.
 */

public class Session {

    private Context context;
    private SharedPreferences sharedPreferences;
    public Session(Context context) {
        this.context = context;
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);

    }

    public void SetLoginID(String id)
    {
        sharedPreferences.edit().putString("LoginID",id).commit();

    }

    public String getLoginID()
    {
        return sharedPreferences.getString("LoginID","");
    }

    public void Destroy()
    {
        sharedPreferences.edit().clear().commit();
    }
}
