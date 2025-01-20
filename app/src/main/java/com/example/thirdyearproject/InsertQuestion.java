package com.example.thirdyearproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class InsertQuestion extends AppCompatActivity {

    private Spinner sessionSpinner;
    private String session = ""; // Selected session (default empty)
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private static final int PICK_PDF_CODE = 1000;
    private Uri pdfUri; // URI of the selected PDF file

    // Firebase references
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_question);

        Button btnSelectFile = findViewById(R.id.btnSelectFile);
        Button btnUploadFile = findViewById(R.id.btnUploadFile);

        // Initialize Firebase references
        storageReference = FirebaseStorage.getInstance().getReference("pdfs");
        databaseReference = FirebaseDatabase.getInstance().getReference("pdfs");

        // Setup Spinner
        sessionSpinner = findViewById(R.id.spinner);
        String[] items = new String[]{"Session : ", "FALL", "SPRING"};
        sessionSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items));
        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                session = parent.getItemAtPosition(position).toString();
                if (session.equals("Session : ")) {
                    session = ""; // Reset to empty for invalid selection
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                session = "";
            }
        });

        // Setup RadioGroup
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            radioButton = findViewById(checkedId);
        });

        // Select file button
        btnSelectFile.setOnClickListener(v -> selectPdfFile());

        // Upload file button
        btnUploadFile.setOnClickListener(v -> {
            if (pdfUri != null && validateInputs()) {
                uploadPdfToFirebase(pdfUri);
            } else {
                Toast.makeText(InsertQuestion.this, "Please select a file and complete all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectPdfFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData(); // Get the selected file's URI
            Toast.makeText(this, "File Selected: " + pdfUri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs() {
        if (session.isEmpty()) {
            Toast.makeText(this, "Please select a session", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void uploadPdfToFirebase(Uri fileUri) {
        String fileName = System.currentTimeMillis() + ".pdf"; // Unique file name
        StorageReference fileRef = storageReference.child(fileName);

        // Upload file to Firebase Storage
        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String fileUrl = uri.toString();

                            // Save file URL to Realtime Database
                            String uploadId = databaseReference.push().getKey();
                            String option = radioButton.getText().toString();

                            // Construct a database entry
                            PDFInsert question = new PDFInsert(uploadId, session, option, fileName, fileUrl);
                            databaseReference.child(uploadId).setValue(question);

                            Toast.makeText(InsertQuestion.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }))
                .addOnFailureListener(e -> Toast.makeText(InsertQuestion.this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
