package com.example.karmarkarsourabh.surat_job_expo.Modal.JobFair;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Datum implements Serializable {
    @SerializedName("College_name")
    @Expose
    private String clg_name;

    @SerializedName("Job_fair_id")
    @Expose
    private int JFid;

    @SerializedName("Job_fair_start_date")
    @Expose
    private String start_date;

    @SerializedName("Job_fair_end_date")
    @Expose
    private String end_date;

    @SerializedName("IsOnline")
    @Expose
    private int IsOn;

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