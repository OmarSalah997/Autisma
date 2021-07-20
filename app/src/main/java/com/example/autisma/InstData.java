package com.example.autisma;

class InstData {
    String InstName;
    String loc;
    String Image;
    String mobileNumber,Description,fbPage,webPage;
    double distance;
    public InstData(String instName, String loc, String image,String mobileNumber,String Description, double distance){
        this.InstName = instName;
        this.loc = loc;
        this.Image = image;
        this.Description=Description;
        this.mobileNumber=mobileNumber;
        this.distance=distance;
    }
    public InstData(String instName, String loc, String image,String mobileNumber,String Description,String fbPage,String webPage,double distance) {
        this.InstName = instName;
        this.loc = loc;
        this.Image = image;
        this.Description=Description;
        this.mobileNumber=mobileNumber;
        this.fbPage=fbPage;
        this.webPage=webPage;
        this.distance=distance;

    }
    public InstData(String instName, String loc, String image,String mobileNumber,String Description,String fbPage,double distance) {
        this.InstName = instName;
        this.loc = loc;
        this.Image = image;
        this.Description=Description;
        this.mobileNumber=mobileNumber;
        this.fbPage=fbPage;
        this.distance=distance;

    }
}