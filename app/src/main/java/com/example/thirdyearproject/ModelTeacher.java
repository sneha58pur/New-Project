package com.example.thirdyearproject;

public class ModelTeacher {
    private String title;
    private String subtitle;
    private String batch;
    private String examType;
    private String pdfUrl;
    private String key;

    public ModelTeacher() {
        // Default constructor required for Firebase
    }

    public ModelTeacher(String title, String subtitle, String batch, String examType, String pdfUrl, String key) {
        this.title = title;
        this.subtitle = subtitle;
        this.batch = batch;
        this.examType = examType;
        this.pdfUrl = pdfUrl;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
