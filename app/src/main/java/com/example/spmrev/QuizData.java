package com.example.spmrev;

public class QuizData {
    private String Qid;
    private String dataQuestion;
    private String dataOption1;
    private String dataOption2;
    private String dataOption3;
    private String dataOption4;
    private String dataAnswer;
    //private String chapter;

    public QuizData(String qid, String dataQuestion, String dataOption1, String dataOption2, String dataOption3, String dataOption4, String dataAnswer) {
        this.Qid = qid;
        this.dataQuestion = dataQuestion;
        this.dataOption1 = dataOption1;
        this.dataOption2 = dataOption2;
        this.dataOption3 = dataOption3;
        this.dataOption4 = dataOption4;
        this.dataAnswer = dataAnswer;
    }

    public QuizData() {
    }

    public String getQid() {
        return Qid;
    }

    public void setQid(String qid) {
        Qid = qid;
    }

    public String getDataQuestion() {
        return dataQuestion;
    }

    public void setDataQuestion(String dataQuestion) {
        this.dataQuestion = dataQuestion;
    }

    public String getDataOption1() {
        return dataOption1;
    }

    public void setDataOption1(String dataOption1) {
        this.dataOption1 = dataOption1;
    }

    public String getDataOption2() {
        return dataOption2;
    }

    public void setDataOption2(String dataOption2) {
        this.dataOption2 = dataOption2;
    }

    public String getDataOption3() {
        return dataOption3;
    }

    public void setDataOption3(String dataOption3) {
        this.dataOption3 = dataOption3;
    }

    public String getDataOption4() {
        return dataOption4;
    }

    public void setDataOption4(String dataOption4) {
        this.dataOption4 = dataOption4;
    }

    public String getDataAnswer() {
        return dataAnswer;
    }

    public void setDataAnswer(String dataAnswer) { this.dataAnswer = dataAnswer;}

}
