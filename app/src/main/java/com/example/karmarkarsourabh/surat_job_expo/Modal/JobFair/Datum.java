package com.example.karmarkarsourabh.surat_job_expo.Modal.JobFair;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Datum implements Serializable {
    @SerializedName("College_name")
    @Expose
    private String clg_name;

    @SerializedName("College_location")
    @Expose
    private String clg_loc;

    @SerializedName("College_email")
    @Expose
    private String clg_email;

    public String getClg_loc() {
        return clg_loc;
    }

    public void setClg_loc(String clg_loc) {
        this.clg_loc = clg_loc;
    }

    public String getClg_email() {
        return clg_email;
    }

    public void setClg_email(String clg_email) {
        this.clg_email = clg_email;
    }

    public String getClg_contat() {
        return clg_contat;
    }

    public void setClg_contat(String clg_contat) {
        this.clg_contat = clg_contat;
    }

    @SerializedName("College_contact")
    @Expose
    private String clg_contat;

    @SerializedName("Job_fair_id")
    @Expose
    private int JFid;

    @SerializedName("Job_fair_start_date")
    @Expose
    private String start_date;

    @SerializedName("Job_fair_end_date")
    @Expose
    private String end_date;

    @SerializedName("Company_registration_start_date")
    @Expose
    private String comp_start_date;

    @SerializedName("Company_registration_end_date")
    @Expose
    private String comp_end_date;

    @SerializedName("Student_registration_start_date")
    @Expose
    private String Stud_start_date;

    @SerializedName("Student_registration_end_date")
    @Expose
    private String Stud_end_date;

    @SerializedName("IsOnline")
    @Expose
    private int IsOn;

    public String getComp_start_date() {
        return comp_start_date;
    }

    public void setComp_start_date(String comp_start_date) {
        this.comp_start_date = comp_start_date;
    }

    public String getComp_end_date() {
        return comp_end_date;
    }

    public void setComp_end_date(String comp_end_date) {
        this.comp_end_date = comp_end_date;
    }

    public String getStud_start_date() {
        return Stud_start_date;
    }

    public void setStud_start_date(String stud_start_date) {
        Stud_start_date = stud_start_date;
    }

    public String getStud_end_date() {
        return Stud_end_date;
    }

    public void setStud_end_date(String stud_end_date) {
        Stud_end_date = stud_end_date;
    }

    public int getIsOn() {
        return IsOn;
    }

    public void setIsOn(int isOn) {
        IsOn = isOn;
    }

    public String getClg_name() {
        return clg_name;
    }

    public void setClg_name(String clg_name) {
        this.clg_name = clg_name;
    }

    public int getJFid() {
        return JFid;
    }

    public void setJFid(int JFid) {
        this.JFid = JFid;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

}