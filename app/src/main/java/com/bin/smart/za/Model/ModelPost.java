package com.bin.smart.za.Model;

public class ModelPost {
    String pId, PTitle, PImage, pTime, uid, uEmail, uName;

    public ModelPost() {
    }

    public ModelPost(String pId, String PTitle, String pImage, String pTime, String uid, String uEmail, String uName) {
        this.pId = pId;
        this.PTitle = PTitle;

        this.PImage = pImage;
        this.pTime = pTime;
        this.uid = uid;
        this.uEmail = uEmail;
        this.uName = uName;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getPTitle() {
        return PTitle;
    }

    public void setPTitle(String PTitle) {
        this.PTitle = PTitle;
    }

    public String getpImage() {
        return PImage;
    }

    public void setpImage(String pImage) {
        this.PImage = pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }
}
