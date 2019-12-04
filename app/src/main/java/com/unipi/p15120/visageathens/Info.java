package com.unipi.p15120.visageathens;

import android.support.design.widget.FloatingActionButton;

public class Info {

    private String name;
    private Double stars;
    private String descr;
    private String image;
    private String text;
    private String main;
    private Integer howmany;
    public Info() {}

    public Info(String descr, String image, String name, Double stars, String text, String main, Integer howmany)
    {

        this.descr = descr;
        this.image = image;
        this.name = name;
        this.stars = stars;
        this.text = text;
        this.main = main;
        this.howmany = howmany;
    }

    public Info(Double stars)
    {
        this.stars = stars;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setStars(Double stars)
    {
        this.stars = stars;
    }
    public void setDescr(String descr) {this.descr = descr;}
    public void setImage(String image) {this.image = image;}
    public void setText(String text) {this.text = text;}
    public void setMain(String main) {this.main = main;}
    public void setHowmany(Integer howmany) {this.howmany = howmany;}


    public String getName()
    {
        return name;
    }
    public Double getStars()
    {
        return stars;
    }
    public String getDescr(){return descr;}
    public String getImage(){return image;}
    public String getText() {return text;}
    public String getMain() {return main;}
    public Integer getHowmany() {return howmany;}
}

