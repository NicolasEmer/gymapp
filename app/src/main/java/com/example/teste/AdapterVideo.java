package com.example.teste;

import static android.content.Intent.getIntent;

import static androidx.core.content.ContextCompat.startActivity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.HolderVideo>{
    private Context context;

    private ArrayList<ModelVideo> videoArrayList;


    public AdapterVideo (Context context, ArrayList<ModelVideo> videoArrayList) {
        this.context = context;
        this.videoArrayList = videoArrayList;
    }

    @NonNull
    @Override
    public HolderVideo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.formato_video, parent, false);
        return new HolderVideo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderVideo holder, int position) {
        ModelVideo modelVideo = videoArrayList.get(position);

        String title = modelVideo.getTitulo();
        String timestamp = modelVideo.getTimestamp();

        long timestampInMilli = Long.parseLong(timestamp);

        Instant instant = Instant.ofEpochMilli(timestampInMilli);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm a");
        String formattedDT = formatter.format(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));

        holder.titleTv.setText(title);
        holder.timeTv.setText(formattedDT);
        setVideoUrl(modelVideo, holder);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Deletar")
                                .setMessage("Você tem certeza que quer excluir o vídeo?")
                                .setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteVideo(modelVideo);

                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
            }
        });

        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditVideoActivity.class);
                intent.putExtra("modelVideo", modelVideo);
                context.startActivity(intent);
            }
        });
    }


    private void setVideoUrl(ModelVideo modelVi, HolderVideo hold){
        String videoUrl = modelVi.getVideoUrl();

        MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(hold.videoView);

        Uri videoUri = Uri.parse(videoUrl);
        hold.videoView.setMediaController(mediaController);
        hold.videoView.setVideoURI(videoUri);

        hold.videoView.requestFocus();
        hold.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        hold.videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what){
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:{
                        hold.progressBar.setVisibility(View.VISIBLE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:{
                        hold.progressBar.setVisibility(View.VISIBLE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:{
                        hold.progressBar.setVisibility(View.GONE);
                        return true;
                    }
                }
                return false;
            }
        });
        hold.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });


    }

    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }

    private void deleteVideo(ModelVideo modelVideo){
        String videoId = modelVideo.getId();
        String videoUrl = modelVideo.getVideoUrl();

        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(videoUrl);
        reference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess (Void avoid) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Videos");
                        databaseReference.child(videoId)
                                .removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Vídeo excluído com sucesso", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context,"Erro ao excluir: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure (@NonNull Exception e) {
                        Toast.makeText(context,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    class HolderVideo extends RecyclerView.ViewHolder {

        VideoView videoView;

        TextView titleTv, timeTv;

        ProgressBar progressBar;

        FloatingActionButton deleteBtn;

        FloatingActionButton updateBtn;

        public HolderVideo(@NonNull View itemView) {
            super(itemView);

            videoView=itemView.findViewById(R.id.videoView);
            titleTv=itemView.findViewById(R.id.titleTv);
            timeTv=itemView.findViewById(R.id.timeTv);
            progressBar=itemView.findViewById(R.id.progressBar);
            deleteBtn=itemView.findViewById(R.id.botaoDelete);
            updateBtn=itemView.findViewById(R.id.botaoUpdate);
        }
    }
}
