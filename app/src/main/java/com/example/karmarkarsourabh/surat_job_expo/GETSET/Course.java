package com.example.karmarkarsourabh.surat_job_expo.GETSET;

/**
 * Created by Archana on 10/12/2018.
 */


/*
* This class is created for filling spinner in the student registration form
*
* */

public class Course {

    public int course_id;
    public String course_name;

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public Course(int course_id, String course_name) {

        this.course_id = course_id;
        this.course_name = course_name;
    }

    @Override
    public String toString() {
        return  course_name;
    }
}
