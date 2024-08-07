package com.example.teste;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class exibirAparelho extends AppCompatActivity {

    private TextView textViewAparelho;
    private TextView textViewMusculosTrabalhados;
    private TextView textViewComoUsar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_aparelho);

        // Set up edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton playBtn = findViewById(R.id.play);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(exibirAparelho.this, GaleriaActivity.class);
                startActivity(intent);
            }
        });

        ImageButton logobt = findViewById(R.id.logobt);
        logobt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(exibirAparelho.this, TelaInicial.class);
                startActivity(intent);
            }
        });

        // Configurar OnClickListener para o botão user
        ImageButton userButton = findViewById(R.id.user);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir a MainActivity ao clicar no botão user
                Intent intent = new Intent(exibirAparelho.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Initialize TextViews
        textViewAparelho = findViewById(R.id.textView5);
        textViewMusculosTrabalhados = findViewById(R.id.textView7);
        textViewComoUsar = findViewById(R.id.textView10);

        // Get the number of the equipment from the previous activity
        int numeroAparelho = getIntent().getIntExtra("numeroAparelho", 0); // 0 is the default value if no value is found

        // Get instance of Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve data from Firestore based on the number of the equipment
        db.collection("aparelhos").document(String.valueOf(numeroAparelho))
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String aparelho = documentSnapshot.getString("aparelho ");
                        String musculosTrabalhados = documentSnapshot.getString("Músculos trabalhados");
                        String comoUsar = documentSnapshot.getString("Como usar");

                        // Update TextViews with data from Firestore
                        textViewAparelho.setText(aparelho);
                        textViewMusculosTrabalhados.setText(musculosTrabalhados);
                        textViewComoUsar.setText(comoUsar);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }
}
