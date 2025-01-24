
package com.example.thirdyearproject;

import android.widget.RadioGroup;

public class ModelTeacher {
    public String title;       // public field
    public String subtitle;    // public field

    //public String batch;
    //public String radioGroup;
    public String pdfUrl;      // public field
    public String key;         // public field

    // Default constructor required for Firebase
    public ModelTeacher() {
    }

    // Constructor with parameters
    public ModelTeacher(String title, String subtitle, String pdfUrl, String key) {
        this.title = title;
        this.subtitle = subtitle;
       // this.batch= batch;
        //this.radioGroup= radioGroup;

        this.pdfUrl = pdfUrl;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and setter for the subtitle
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    // Getter and setter for the PDF URL
    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    // Getter and setter for the key
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}





