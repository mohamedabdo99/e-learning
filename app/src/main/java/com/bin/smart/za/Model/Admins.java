package com.bin.smart.za.Model;

public class Admins {
    private String  Email;
    private String password;
    private String phone;
    private String doctorName;
    private String uidDoctor;

    public Admins() {
    }

    public Admins(String email, String password, String phone, String doctorName, String uidDoctor) {
        Email = email;
        this.password = password;
        this.phone = phone;
        this.doctorName = doctorName;
        this.uidDoctor = uidDoctor;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getUidDoctor() {
        return uidDoctor;
    }

    public void setUidDoctor(String uidDoctor) {
        this.uidDoctor = uidDoctor;
    }
}
