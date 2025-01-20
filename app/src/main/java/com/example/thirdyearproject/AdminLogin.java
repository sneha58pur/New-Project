package com.example.thirdyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminLogin extends AppCompatActivity {
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_login);
        firestore= FirebaseFirestore.getInstance();

        Map<String,Object> user= new HashMap<>();
        user.put("firstName", "easy");
        user.put("lastName", "tuto");
        user.put("thirdName", "hello");


        firestore.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();


            }
        } );








        Button btnGo = findViewById(R.id.btn_signRegister);


        btnGo.setOnClickListener(v -> {
            // Implement your sign up logic here
            //Toast.makeText(AdminLogin.this, "Sign up clicked", Toast.LENGTH_SHORT).show();
            // Navigate to sign up activity if needed
            Intent intent = new Intent(AdminLogin.this, QuestionBank.class);
            startActivity(intent);
        });
    }
}
