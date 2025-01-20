package com.example.thirdyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CGPACalculatorActivity extends AppCompatActivity {

    private EditText gradesInput, creditsInput;
    private Button calculateButton;
    private TextView resultTextView;

    // Firestore instance
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgpacalculator);

        gradesInput = findViewById(R.id.gradesInput);
        creditsInput = findViewById(R.id.creditsInput);
        calculateButton = findViewById(R.id.calculateButton);
        resultTextView = findViewById(R.id.resultTextView);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                String gradesText = gradesInput.getText().toString().trim();
                String creditsText = creditsInput.getText().toString().trim();

                if (!gradesText.isEmpty() && !creditsText.isEmpty()) {
                    try {
                        double cgpa = calculateCGPA(gradesText, creditsText);
                        resultTextView.setText(String.format("Your CGPA: %.2f", cgpa));

                        // Store data in Firestore
                        storeCGPAData(gradesText, creditsText, cgpa);
                    } catch (Exception e) {
                        Toast.makeText(CGPACalculatorActivity.this, "Invalid input. Ensure grades and credits are properly formatted.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CGPACalculatorActivity.this, "Please enter both grades and credits.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private double calculateCGPA(String gradesText, String creditsText) {
        String[] gradesArray = gradesText.split(",");
        String[] creditsArray = creditsText.split(",");

        if (gradesArray.length != creditsArray.length) {
            throw new IllegalArgumentException("Grades and credits count must match.");
        }

        double totalWeightedGrades = 0;
        double totalCredits = 0;

        for (int i = 0; i < gradesArray.length; i++) {
            double grade = Double.parseDouble(gradesArray[i].trim());
            double credit = Double.parseDouble(creditsArray[i].trim());

            totalWeightedGrades += grade * credit;
            totalCredits += credit;
        }

        if (totalCredits == 0) {
            throw new ArithmeticException("Total credits cannot be zero.");
        }

        return totalWeightedGrades / totalCredits;
    }

    private void storeCGPAData(String grades, String credits, double cgpa) {
        // Create a map to store the data
        Map<String, Object> data = new HashMap<>();
        data.put("grades", grades);
        data.put("credits", credits);
        data.put("cgpa", cgpa);

        // Save data to Firestore
        firestore.collection("CGPARecords")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(CGPACalculatorActivity.this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
                    Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CGPACalculatorActivity.this, "Error saving data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error adding document", e);
                });
    }
}
