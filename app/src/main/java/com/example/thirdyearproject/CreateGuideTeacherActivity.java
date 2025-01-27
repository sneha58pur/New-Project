package com.example.thirdyearproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class CreateGuideTeacherActivity extends AppCompatActivity {

    private static final int PDF_REQ = 1;

    private EditText titleEditText, subtitleEditText, batchEditText;
    private TextView pdfTextView;
    private ProgressBar progressBar;
    private Button addButton;

    private String title, subtitle, batch, examType, pdfUrl;
    private Uri pdfUri;

    private RadioGroup radioGroup;
    private RadioButton selectedRadioButton;

    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_guide_teacher);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        titleEditText = findViewById(R.id.title);
        subtitleEditText = findViewById(R.id.subtitle);
        batchEditText = findViewById(R.id.batch_id);
        pdfTextView = findViewById(R.id.pdf_in);
        progressBar = findViewById(R.id.progressBar);
        addButton = findViewById(R.id.add);

        radioGroup = findViewById(R.id.radioGroup);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("PDFs");

        pdfTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent, PDF_REQ);
        });

        addButton.setOnClickListener(v -> {
            title = titleEditText.getText().toString().trim();
            subtitle = subtitleEditText.getText().toString().trim();
            batch = batchEditText.getText().toString().trim();
            int selectedId = radioGroup.getCheckedRadioButtonId();

            if (selectedId == R.id.radio_mid) {
                examType = "Mid";
            } else if (selectedId == R.id.radio_final) {
                examType = "Final";
            }

            if (validateInputs()) {
                progressBar.setVisibility(View.VISIBLE);
                uploadPdfToCloudinary(pdfUri);
            }
        });
    }

    private boolean validateInputs() {
        if (pdfUri == null) {
            Toast.makeText(this, "Please Select a PDF", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (title.isEmpty()) {
            titleEditText.setError("Empty");
            titleEditText.requestFocus();
            return false;
        }
        if (subtitle.isEmpty()) {
            subtitleEditText.setError("Empty");
            subtitleEditText.requestFocus();
            return false;
        }
        if (batch.isEmpty()) {
            batchEditText.setError("Empty");
            batchEditText.requestFocus();
            return false;
        }
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PDF_REQ && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            pdfTextView.setText(pdfUri.getLastPathSegment());
            Toast.makeText(this, "PDF selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPdfToCloudinary(Uri pdfUri) {
        MediaManager.get().upload(pdfUri)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {}

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {}

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        pdfUrl = (String) resultData.get("secure_url");
                        saveDataToFirebase();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(CreateGuideTeacherActivity.this, "Error uploading PDF: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {}
                }).dispatch();
    }

    private void saveDataToFirebase() {
        String key = databaseReference.push().getKey();
        ModelTeacher model = new ModelTeacher(title, subtitle, batch, examType, pdfUrl, key);
        databaseReference.child(key).setValue(model)
                .addOnSuccessListener(unused -> {
                    titleEditText.setText("");
                    subtitleEditText.setText("");
                    batchEditText.setText("");
                    pdfTextView.setText("");
                    Toast.makeText(this, "PDF added successfully!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                });
    }
}
