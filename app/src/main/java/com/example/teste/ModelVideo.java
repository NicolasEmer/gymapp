package com.example.teste;

import java.io.Serializable;

public class ModelVideo implements Serializable {
    private String id;
    private String titulo;
    private String timestamp;
    private String videoUrl;

    public ModelVideo() {
    }

    public ModelVideo(String id, String title, String timestamp, String videoUrl) {
        this.id = id;
        this.titulo = title;
        this.timestamp = timestamp;
        this.videoUrl = videoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
