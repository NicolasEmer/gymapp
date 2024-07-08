package com.example.teste;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GaleriaActivity extends AppCompatActivity {

    private FloatingActionButton CamBtn;
    private ArrayList<ModelVideo> videoArrayList;
    private AdapterVideo adapterVideo;
    private RecyclerView videosRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_galeria);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setTitle("Videos");

        CamBtn = findViewById(R.id.botaoCamera);
        videosRV = findViewById(R.id.lista_videos);

        carregarVideos();


        CamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(  GaleriaActivity.this, AddVideoActivity.class));
            }
        });
    }

    private void carregarVideos(){
        if (videoArrayList == null) {
            videoArrayList = new ArrayList<>();
            adapterVideo = new AdapterVideo(this, videoArrayList);
            videosRV.setAdapter(adapterVideo);
        }

        DatabaseReference df = FirebaseDatabase.getInstance().getReference("Videos");
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                videoArrayList.clear();  // Limpe a lista antes de adicionar os novos dados
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelVideo modelVideo = ds.getValue(ModelVideo.class);
                    videoArrayList.add(modelVideo);
                }
                adapterVideo.notifyDataSetChanged();  // Notifique o Adapter que os dados mudaram
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GaleriaActivity.this, "Erro:" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}