package com.example.teste;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TelaInicial extends AppCompatActivity {

    // Variável global para armazenar o número do aparelho
    private int numeroAparelho = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        // Aplicar padding conforme o sistema de barras
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton playBtn=findViewById(R.id.play);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaInicial.this, GaleriaActivity.class);
                startActivity(intent);
            }
        });


        // Configurar OnClickListener para o botão user
        ImageButton userButton = findViewById(R.id.user);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir a EditUserActivity ao clicar no botão user
                Intent intent = new Intent(TelaInicial.this, EditUserActivity.class);
                startActivity(intent);
            }
        });

        // Configurar OnClickListener para os botões de aparelhos
        configureImageButton(R.id.rosca, 1);
        configureImageButton(R.id.abdominal, 2);
        configureImageButton(R.id.extensora, 3);
        configureImageButton(R.id.flexora, 4);
        configureImageButton(R.id.crossover, 5);
        configureImageButton(R.id.leg, 6);
        configureImageButton(R.id.voador, 7);
        configureImageButton(R.id.pulley, 8);
        configureImageButton(R.id.remada, 9);
        configureImageButton(R.id.supino, 10);
    }

    // Método para configurar OnClickListener para um ImageButton específico
    private void configureImageButton(int buttonId, final int numero) {
        ImageButton imageButton = findViewById(buttonId);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Atribuir o número do aparelho à variável global
                numeroAparelho = numero;

                // Abrir a ExibirAparelho ao clicar no botão
                Intent intent = new Intent(TelaInicial.this, exibirAparelho.class);
                intent.putExtra("numeroAparelho", numeroAparelho); // Passar o número do aparelho para a próxima atividade
                startActivity(intent);
            }
        });
    }
}
