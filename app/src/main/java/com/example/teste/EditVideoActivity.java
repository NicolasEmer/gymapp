package com.example.teste;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditVideoActivity extends AppCompatActivity {

    private FloatingActionButton btnSalvar;
    private EditText titulo;
    private ModelVideo modelVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("modelVideo")) {
            modelVideo = (ModelVideo) intent.getSerializableExtra("modelVideo");

            if (modelVideo != null) {
                // Use modelVideo como necessário
                // Exemplo: setar o título em um EditText
                EditText editTextTitle = findViewById(R.id.titulo);
                editTextTitle.setText(modelVideo.getTitulo());
            } else {
                Toast.makeText(this, "Erro ao carregar dados do vídeo", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Nenhum dado de vídeo encontrado", Toast.LENGTH_SHORT).show();
        }
        btnSalvar= findViewById(R.id.botaoSalvar);
        titulo=findViewById(R.id.titulo);
        //assert modelVideo != null;
        //titulo.setText(modelVideo.getTitulo());


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoId = modelVideo.getId();
                String newTitle = titulo.getText().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Videos");

                databaseReference.child(videoId).child("titulo").setValue(newTitle)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditVideoActivity.this, "Título atualizado com sucesso", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditVideoActivity.this, "Erro ao atualizar título: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}