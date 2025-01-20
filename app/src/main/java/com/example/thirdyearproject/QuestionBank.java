package com.example.thirdyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class QuestionBank extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_bank);

        ImageView image1 = findViewById(R.id.image1);
        ImageView image2 = findViewById(R.id.image2);

        // Set click listeners for each image
        image1.setOnClickListener(v -> {
                    Intent intent = new Intent(QuestionBank.this, InsertQuestion.class);
                    startActivity(intent);
                });

        image2.setOnClickListener(v -> { Intent intent = new Intent(QuestionBank.this, ViewQuestion.class);
            startActivity(intent);
        });

    }
}
