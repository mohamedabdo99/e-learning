package com.bin.smart.za.Model;

public class NumberLecture {

    private  String Video ;
    private String text ;
    private String pdf ;
    private String pdfName ;


    public NumberLecture() {
    }

    public NumberLecture(String video, String text) {

        this.Video = video;
        this.text = text;
    }

    public NumberLecture(String video, String text, String pdf, String pdfName) {
        Video = video;
        this.text = text;
        this.pdf = pdf;
        this.pdfName = pdfName;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }
}
