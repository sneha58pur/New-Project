package com.example.thirdyearproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class StudentPlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // show activity in full screen

        setContentView(R.layout.activity_student_place);
        try {
            initConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnNote = findViewById(R.id.btn_note);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnRead = findViewById(R.id.btn_read);
       @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button submit=findViewById(R.id.btn_read_stu);


        btnNote.setOnClickListener(v -> {
            // Implement your sign up logic here

            // Navigate to sign up activity if needed



        startActivity(new Intent(getApplicationContext(), CreateGuideTeacherActivity.class));
        finish();
    });




        btnRead.setOnClickListener(v -> {
            // Implement your sign up logic here

            // Navigate to sign up activity if needed



            startActivity(new Intent(getApplicationContext(), ReadGuideTeacherActivity.class));
            finish();
        });


        submit.setOnClickListener(v -> {
            // Implement your sign up logic here

            // Navigate to sign up activity if needed



            startActivity(new Intent(getApplicationContext(), StudReadCourseOutActivity.class));
            finish();
        });

    }

    private void initConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dr6zrvhzs");  // Replace with your actual Cloudinary cloud name
        config.put("api_key", "396351212989152");  // Replace with your Cloudinary API key
        config.put("api_secret", "PPpb787-P_sWsRR-nuMr9qucQpU");  // Replace with your Cloudinary API secret

        MediaManager.init(this, config);
    }
}
