package com.example.teste;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentReference;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
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
                // Check if any field is empty
                if (isEmpty(firstNameET) || isEmpty(phoneET) || isEmpty(emailET) || isEmpty(senhaET) || isEmpty(repsenhaET) || isEmpty(bioET)) {
                    Toast.makeText(AddUserActivity.this, "Por favor preencha todas as informações!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if passwords match
                String senha = Objects.requireNonNull(senhaET.getText()).toString();
                String repSenha = Objects.requireNonNull(repsenhaET.getText()).toString();
                if (!senha.equals(repSenha)) {
                    Toast.makeText(AddUserActivity.this, "Senha não está igual", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if email format is valid
                String email = Objects.requireNonNull(emailET.getText()).toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(AddUserActivity.this, "Formato de email incorreto", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if phone number is valid
                String phone = Objects.requireNonNull(phoneET.getText()).toString();
                if (!phone.matches("\\(\\d{2}\\)\\d{9}")) {
                    Toast.makeText(AddUserActivity.this, "Formato de telefone incorreto! Formato esperado (DD)987654321", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Check if password is strong (example: at least 6 characters with letters and numbers)
                if (!isStrongPassword(senha)) {
                    Toast.makeText(AddUserActivity.this, "A senha deve possuir mais de 6 caracteres e possuir letras e números!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a map to hold user data
                Map<String, Object> user = new HashMap<>();
                user.put("firstName", Objects.requireNonNull(firstNameET.getText()).toString());
                user.put("phone", phone);
                user.put("email", email);
                user.put("senha", senha);
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

    // Function to check if EditText is empty
    private boolean isEmpty(TextInputEditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    // Function to check if password is strong
    private boolean isStrongPassword(String password) {
        // At least 6 characters with letters and numbers
        return password.length() >= 6 && password.matches(".*[a-zA-Z].*") && password.matches(".*\\d.*");
    }
}
