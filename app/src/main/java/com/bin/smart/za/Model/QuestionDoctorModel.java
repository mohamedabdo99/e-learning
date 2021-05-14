package com.bin.smart.za.Model;

public class QuestionDoctorModel {
    private String questionId;
    String question;
    String Answer1;
    String Answer2;
    String Answer3;
    String Answer4;
    String correctAns;
    int mark;

    public QuestionDoctorModel(String question, String answer1, String answer2, String answer3, String answer4, String correctAns) {
        this.question = question;
        Answer1 = answer1;
        Answer2 = answer2;
        Answer3 = answer3;
        Answer4 = answer4;
        this.correctAns = correctAns;
    }

    public QuestionDoctorModel(String questionId, String question, String answer1, String answer2, String answer3, String answer4, String correctAns,int mark) {
        this.questionId = questionId;
        this.question = question;
        Answer1 = answer1;
        Answer2 = answer2;
        Answer3 = answer3;
        Answer4 = answer4;
        this.correctAns = correctAns;
        this.mark=mark;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return Answer1;
    }

    public void setAnswer1(String answer1) {
        Answer1 = answer1;
    }

    public String getAnswer2() {
        return Answer2;
    }

    public void setAnswer2(String answer2) {
        Answer2 = answer2;
    }

    public String getAnswer3() {
        return Answer3;
    }

    public void setAnswer3(String answer3) {
        Answer3 = answer3;
    }

    public String getAnswer4() {
        return Answer4;
    }

    public void setAnswer4(String answer4) {
        Answer4 = answer4;
    }

    public String getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(String correctAns) {
        this.correctAns = correctAns;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
