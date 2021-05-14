package com.bin.smart.za.Model;

public class CategoryQuiz {
    private  String id;
    private String name ;
    private String noOfSets;
    private String setsCounter;

    public CategoryQuiz() {
    }

    public CategoryQuiz(String id, String name, String noOfSets,String setsCounter) {
        this.id = id;
        this.name = name;
        this.noOfSets = noOfSets;
        this.setsCounter=setsCounter;
    }

    public String getSetsCounter() {
        return setsCounter;
    }

    public void setSetsCounter(String setsCounter) {
        this.setsCounter = setsCounter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoOfSets() {
        return noOfSets;
    }

    public void setNoOfSets(String noOfSets) {
        this.noOfSets = noOfSets;
    }
}
