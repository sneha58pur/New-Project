package com.example.thirdyearproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class CreateNoteActivity extends AppCompatActivity {

    private ImageView imageView;
    private static final int IMAGE_REQ = 1;
    private EditText titleEditText, subtitleEditText, batchEditText;
    private String title, subtitle, batch, pdfUrl;
    private Uri pdfUri;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button button;
    private DatabaseReference reference;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_note);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageView = findViewById(R.id.imageView);
        titleEditText = findViewById(R.id.title);
        subtitleEditText = findViewById(R.id.subtitle);
        batchEditText = findViewById(R.id.batch_id);
        button = findViewById(R.id.add);
        progressBar = findViewById(R.id.progressBar);
        reference = FirebaseDatabase.getInstance().getReference().child("PDFs");

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            radioButton = findViewById(checkedId);
        });

        imageView.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(CreateNoteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGE_REQ);
            } else {
                ActivityCompat.requestPermissions(CreateNoteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, IMAGE_REQ);
            }
        });

        button.setOnClickListener(v -> {
            title = titleEditText.getText().toString().trim();
            subtitle = subtitleEditText.getText().toString().trim();
            batch = batchEditText.getText().toString().trim();

            if (pdfUri == null) {
                Toast.makeText(CreateNoteActivity.this, "Please Select a PDF", Toast.LENGTH_SHORT).show();
            } else if (title.isEmpty()) {
                titleEditText.setError("Empty");
                titleEditText.requestFocus();
            } else if (subtitle.isEmpty()) {
                subtitleEditText.setError("Empty");
                subtitleEditText.requestFocus();
            }

            else if (batch.isEmpty()) {
                batchEditText.setError("Empty");
                batchEditText.requestFocus();

            }
            else if (radioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                uploadPdfToCloudinary(pdfUri);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQ && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            Toast.makeText(CreateNoteActivity.this, "PDF selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPdfToCloudinary(Uri pdfUri) {
        MediaManager.get().upload(pdfUri)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) { }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) { }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        pdfUrl = (String) resultData.get("secure_url");
                        uploadData(pdfUrl);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(CreateNoteActivity.this, "Error uploading PDF: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) { }
                }).dispatch();
    }

    private void uploadData(String pdfUrl) {
        String key = reference.push().getKey();
        Model data = new Model(title, subtitle,batch,radioGroup, pdfUrl, key);
        reference.child(key).setValue(data)
                .addOnSuccessListener(unused -> {
                    titleEditText.setText("");
                    subtitleEditText.setText("");
                    batchEditText.setText("");
                    Toast.makeText(CreateNoteActivity.this, "PDF Added Successfully!!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateNoteActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                });
    }
}
