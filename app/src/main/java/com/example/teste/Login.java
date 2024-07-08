package com.example.teste;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private EditText emailLogin;
    private EditText passwordLogin;
    private AppCompatButton btLogin;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.editTextTextPassword);
        btLogin = findViewById(R.id.btLogin);

        // Set up edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find the "Cadastrar" button
        AppCompatButton btCadastro = findViewById(R.id.btCadastro);

        // Set an OnClickListener on the "Cadastrar" button
        btCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start AddUserActivity
                Intent intent = new Intent(Login.this, AddUserActivity.class);
                startActivity(intent);
            }
        });

        // Set an OnClickListener on the "Login" button
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get email and password from EditText
                String email = emailLogin.getText().toString().trim();
                String password = passwordLogin.getText().toString().trim();

                // Validate email and password
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Authenticate using Firestore
                db.collection("users")
                        .whereEqualTo("email", email)
                        .whereEqualTo("senha", password)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                // Sign in success
                                Toast.makeText(Login.this, "Authentication Successful.", Toast.LENGTH_SHORT).show();
                                // Navigate to TelaInicial after login
                                Intent intent = new Intent(Login.this, TelaInicial.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
