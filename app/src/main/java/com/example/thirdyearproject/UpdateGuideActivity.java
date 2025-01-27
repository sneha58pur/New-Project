package com.example.thirdyearproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UpdateGuideActivity extends AppCompatActivity {

    private static final int PDF_REQUEST_CODE = 2;

    private EditText titleEditText, subtitleEditText, batchEditText;
    private TextView pdfInfoTextView, selectPdfTextView;;
    private Button updateButton;
    private ProgressBar progressBar;

    private DatabaseReference databaseReference;
    private String key, title, subtitle, batch, pdfUrl;
    private Uri pdfUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_guide);

        // Initialize views
        titleEditText = findViewById(R.id.title);
        subtitleEditText = findViewById(R.id.subtitle);
        batchEditText = findViewById(R.id.batch_id);
        pdfInfoTextView = findViewById(R.id.pdf_in);
        updateButton = findViewById(R.id.updateButton);
        selectPdfTextView = findViewById(R.id.selectPdf);
        progressBar = findViewById(R.id.progressBar);

        // Get Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("PDFs");

        // Get data from intent
        key = getIntent().getStringExtra("key");
        title = getIntent().getStringExtra("title");
        subtitle = getIntent().getStringExtra("subtitle");
        pdfUrl = getIntent().getStringExtra("pdfUrl");
        batch = getIntent().getStringExtra("batch");

        // Set existing data in fields
        titleEditText.setText(title);
        subtitleEditText.setText(subtitle);
        pdfInfoTextView.setText(pdfUrl != null ? "PDF: " + pdfUrl : "No PDF Selected");

        // Select PDF
        selectPdfTextView.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, PDF_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PDF_REQUEST_CODE);
            }
        });




        // Update record
        updateButton.setOnClickListener(v -> {
            title = titleEditText.getText().toString().trim();
            subtitle = subtitleEditText.getText().toString().trim();
            batch = batchEditText.getText().toString().trim();

            if (title.isEmpty() || subtitle.isEmpty() || batch.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            if (pdfUri == null) {
                // Update metadata only
                updateData(pdfUrl);
            } else {
                // Update metadata and PDF URL
                String newPdfUrl = "your_pdf_upload_url_here"; // Replace with your PDF upload logic
                updateData(newPdfUrl);
            }
        });
    }

    private void updateData(String newPdfUrl) {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("title", title);
        updatedData.put("subtitle", subtitle);
        updatedData.put("batch", batch);
        updatedData.put("pdfUrl", newPdfUrl);

        databaseReference.child(key).updateChildren(updatedData)
                .addOnSuccessListener(unused -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Failed to update: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PDF_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            pdfInfoTextView.setText("PDF selected: " + pdfUri.getLastPathSegment());
            Toast.makeText(this, "PDF selected", Toast.LENGTH_SHORT).show();
        }
    }
}
