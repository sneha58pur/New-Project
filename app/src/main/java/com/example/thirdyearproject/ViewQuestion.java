package com.example.thirdyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewQuestion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_question);

        Button buttonUpdate = findViewById(R.id.button_update);
        Button buttonDelete = findViewById(R.id.button_delete);

        // Set click listeners for each image
        buttonUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(ViewQuestion.this, UpdateQuestion.class);
            startActivity(intent);
        });


        buttonDelete.setOnClickListener(v -> {
            Intent intent = new Intent(ViewQuestion.this, DeleteQuestion.class);
            startActivity(intent);
        });





    }
}