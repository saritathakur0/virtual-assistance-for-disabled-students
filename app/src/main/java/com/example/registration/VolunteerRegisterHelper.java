package com.example.registration;

public class VolunteerRegisterHelper {

    String fname1, lname1, dob1, email1, mobile1, password1;

    public VolunteerRegisterHelper() {
    }

    public VolunteerRegisterHelper(String fname1, String lname1, String dob1, String email1, String mobile1, String password1) {
        this.fname1 = fname1;
        this.lname1 = lname1;
        this.dob1 = dob1;
        this.email1 = email1;
        this.mobile1 = mobile1;
        this.password1 = password1;
    }

    public String getFname1() {
        return fname1;
    }

    public void setFname1(String fname1) {
        this.fname1 = fname1;
    }

    public String getLname1() {
        return lname1;
    }

    public void setLname1(String lname1) {
        this.lname1 = lname1;
    }

    public String getDob1() {
        return dob1;
    }

    public void setDob1(String dob1) {
        this.dob1 = dob1;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }
}
