package com.bin.smart.za.Model;

public class DownModel {
    public String name;
    public String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DownModel() {
    }

    public DownModel(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
