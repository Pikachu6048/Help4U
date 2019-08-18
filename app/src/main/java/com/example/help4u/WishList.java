package com.example.help4u;

public class WishList {

    private String position;
    private String photourl;
    private String jobtitle;
    private String jobdescription;

    public WishList() {
    }

    public WishList(String position, String photourl, String jobtitle, String jobdescription) {
        this.position = position;
        this.photourl = photourl;
        this.jobtitle = jobtitle;
        this.jobdescription = jobdescription;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getJobdescription() {
        return jobdescription;
    }

    public void setJobdescription(String jobdescription) {
        this.jobdescription = jobdescription;
    }
}
