package com.example.karmarkarsourabh.surat_job_expo.Modal.resume;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rujuu on 3/31/2018.
 */
public class Datum {
    @SerializedName("resume_id")
    @Expose
    private int resume_id;

    @SerializedName("theme_name")
    @Expose
    private String theme_name;

    @SerializedName("theme_category")
    @Expose
    private String theme_category;

    @SerializedName("theme_preview")
    @Expose
    private String theme_prev;

    public String getTheme_prev() {
        return theme_prev;
    }

    public void setTheme_prev(String theme_prev) {
        this.theme_prev = theme_prev;
    }

    public int getResume_id() {
        return resume_id;
    }

    public void setResume_id(int resume_id) {
        this.resume_id = resume_id;
    }

    public String getTheme_name() {
        return theme_name;
    }

    public void setTheme_name(String theme_name) {
        this.theme_name = theme_name;
    }

    public String getTheme_category() {
        return theme_category;
    }

    public void setTheme_category(String theme_category) {
        this.theme_category = theme_category;
    }
}