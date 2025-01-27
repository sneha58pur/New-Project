package com.example.thirdyearproject;

import android.widget.RadioGroup;

public class Model {
    public String title;       // public field
    public String subtitle;    // public field

    public String batch;
    public String radioGroup;
    public String pdfUrl;      // public field
    public String key;         // public field

    // Default constructor required for Firebase
    public Model(String title, String subtitle, String batch, RadioGroup radioGroup, String pdfUrl, String key) {
    }

    // Constructor with parameters
    public Model(String title, String subtitle, String batch, String radioGroup, String pdfUrl, String key) {
        this.title = title;
        this.subtitle = subtitle;
        this.batch= batch;
        this.radioGroup= radioGroup;

        this.pdfUrl = pdfUrl;
        this.key = key;
    }
}
