package com.example.karmarkarsourabh.surat_job_expo.Adaptor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.karmarkarsourabh.surat_job_expo.College_Course_Manage_Detail_Fragment_College;
import com.example.karmarkarsourabh.surat_job_expo.College_Course_Manage_Detail_Fragment_Company;

/**
 * Created by lenovo on 10-26-2018.
 */

public class College_Course_Manage_Detail_TabBar_Adapter extends FragmentStatePagerAdapter {

   public int jfid;
    String[] tabarray=new String[]{"company","college"};
    Integer tabnumber=2;

    public College_Course_Manage_Detail_TabBar_Adapter(FragmentManager fm, int jfid) {
        super(fm);
        this.jfid=jfid;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabarray[position];
    }

    @Override
    public Fragment getItem(int position) {


        switch(position)
        {
            case 0:
                College_Course_Manage_Detail_Fragment_Company company=new College_Course_Manage_Detail_Fragment_Company();
                company.jobfairid=jfid;
                return company;
            case 1:

            College_Course_Manage_Detail_Fragment_College col=new College_Course_Manage_Detail_Fragment_College();
            col.jobfairid=jfid;

            return col;
        }

        return null;
    }

    @Override
    public int getCount() {
        return tabnumber;
    }
}
