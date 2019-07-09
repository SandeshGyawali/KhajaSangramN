package com.example.khajasangram.Classes;

public class UserdetailsClass {
    private String fname;
    private String lname;
    private String email;
    private String latitude;
    private String longitude;

    public UserdetailsClass(String fname, String lname, String email, String latitude, String longitude) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
