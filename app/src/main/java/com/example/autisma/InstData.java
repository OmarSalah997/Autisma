package com.example.autisma;

import android.location.Location;

class InstData {
    String InstName;
    String loc;
    String Image;
    String mobileNumber,Description;
    public InstData(String instName, String loc, String image,String mobileNumber,String Description) {
        this.InstName = instName;
        this.loc = loc;
        this.Image = image;
        this.Description=Description;
        this.mobileNumber=mobileNumber;

    }
}