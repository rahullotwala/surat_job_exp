package com.example.karmarkarsourabh.surat_job_expo.GETSET;

/**
 * Created by Archana on 10/12/2018.
 */

/*
* This class is created for filling spinner in the student registration form
*
* */
public class College {

    public int college_id;
    public String college_name;

    public int getCollege_id() {
        return college_id;
    }

    public void setCollege_id(int college_id) {
        this.college_id = college_id;
    }

    public String getCollege_name() {
        return college_name;
    }

    public void setCollege_name(String college_name) {
        this.college_name = college_name;
    }

    public College(int college_id, String college_name) {

        this.college_id = college_id;
        this.college_name = college_name;
    }

    @Override
    public String toString() {
        return college_name;
    }
}
