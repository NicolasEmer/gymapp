package com.example.teste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddUserActivity extends AppCompatActivity {

    private TextInputEditText firstNameET;
    private TextInputEditText phoneET;
    private TextInputEditText emailET;
    private TextInputEditText senhaET;
    private TextInputEditText repsenhaET;
    private TextInputEditText bioET;
    private MaterialButton addUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Initialize Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Initialize EditTexts and Button
        firstNameET = findViewById(R.id.firstNameET);
        phoneET = findViewById(R.id.phoneET);
        emailET = findViewById(R.id.emailET);
        senhaET = findViewById(R.id.senhaET);
        repsenhaET = findViewById(R.id.repsenhaET);
        bioET = findViewById(R.id.bioET);
        addUser = findViewById(R.id.addUser);

        // Set OnClickListener for the addUser button
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if passwords match
                if (!Objects.requireNonNull(senhaET.getText()).toString().equals(Objects.requireNonNull(repsenhaET.getText()).toString())) {
                    Toast.makeText(AddUserActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a map to hold user data
                Map<String, Object> user = new HashMap<>();
                user.put("firstName", Objects.requireNonNull(firstNameET.getText()).toString());
                user.put("phone", Objects.requireNonNull(phoneET.getText()).toString());
                user.put("email", Objects.requireNonNull(emailET.getText()).toString());
                user.put("senha", Objects.requireNonNull(senhaET.getText()).toString());
                user.put("bio", Objects.requireNonNull(bioET.getText()).toString());

                // Add user data to Firestore
                db.collection("users").add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(AddUserActivity.this, "User Added Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddUserActivity.this, "There was an error while adding user", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
