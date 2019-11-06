package com.example.proyectoii.Objetos;

import androidx.annotation.Nullable;

public class PostObject {

    private String descripcion;
    private String imageURI;
    private String videoUrl;
    private String authorId;
    private String tipo;

    public PostObject () {

    }

    public PostObject (String authorId, String descripcion) {
        this.descripcion = descripcion;
        this.authorId = authorId;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }



    public void setTipo(String type) {
        tipo = type;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImageURI() {
        return imageURI;
    }

    public String getTipo() {
        return tipo;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

}
