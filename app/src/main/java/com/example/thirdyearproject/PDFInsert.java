package com.example.thirdyearproject;

public class PDFInsert {

    private String id;
    private String session;
    private String option;
    private String fileName;
    private String fileUrl;

    public PDFInsert() {
        // Default constructor required for Firebase
    }

    public PDFInsert(String id, String session, String option, String fileName, String fileUrl) {
        this.id = id;
        this.session = session;
        this.option = option;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
