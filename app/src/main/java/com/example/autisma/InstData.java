package com.example.autisma;

import android.location.Location;

class InstData {
    String InstName;
    String loc;
    String Image;
    String mobileNumber,Description,fbPage,webPage;
    public InstData(String instName, String loc, String image,String mobileNumber,String Description){
        this.InstName = instName;
        this.loc = loc;
        this.Image = image;
        this.Description=Description;
        this.mobileNumber=mobileNumber;
    }
    public InstData(String instName, String loc, String image,String mobileNumber,String Description,String fbPage,String webPage) {
        this.InstName = instName;
        this.loc = loc;
        this.Image = image;
        this.Description=Description;
        this.mobileNumber=mobileNumber;
        this.fbPage=fbPage;
        this.webPage=webPage;

    }
    public InstData(String instName, String loc, String image,String mobileNumber,String Description,String fbPage) {
        this.InstName = instName;
        this.loc = loc;
        this.Image = image;
        this.Description=Description;
        this.mobileNumber=mobileNumber;
        this.fbPage=fbPage;


    }
}