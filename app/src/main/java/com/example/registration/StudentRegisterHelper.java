package com.example.registration;

public class StudentRegisterHelper {
    String fname, lname, dob, email, mobile, disability, password;

    public StudentRegisterHelper() {
    }

    public StudentRegisterHelper(String fname, String lname, String dob, String email, String mobile, String disability, String password) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.email = email;
        this.mobile = mobile;
        this.disability = disability;
        this.password = password;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDisability() {
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
