package com.example.karmarkarsourabh.surat_job_expo.Modal.JobList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Datum implements Serializable {

    @SerializedName("Company_name")
    @Expose
    private String Company_name;

    @SerializedName("Post_name")
    @Expose
    private String Post_name;

    @SerializedName("Post_description")
    @Expose
    private String Post_description;

    @SerializedName("Package_provided")
    @Expose
    private String Package_provided;

    public String getCompany_name() {
        return Company_name;
    }

    public void setCompany_name(String company_name) {
        Company_name = company_name;
    }

    public String getPost_name() {
        return Post_name;
    }

    public void setPost_name(String post_name) {
        Post_name = post_name;
    }

    public String getPost_description() {
        return Post_description;
    }

    public void setPost_description(String post_description) {
        Post_description = post_description;
    }

    public String getPackage_provided() {
        return Package_provided;
    }

    public void setPackage_provided(String package_provided) {
        Package_provided = package_provided;
    }
}