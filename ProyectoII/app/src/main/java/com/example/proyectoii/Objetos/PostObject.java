package com.example.proyectoii.Objetos;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostObject {

    private String descripcion;
    private String imageURI;
    private String videoUrl;
    private String authorId;
    private String tipo;
    private String fecha;

    public PostObject () {

    }

    public PostObject (String authorId, String descripcion) {
        this.descripcion = descripcion;
        this.authorId = authorId;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss", Locale.ENGLISH);
        Date now = new Date();
        this.fecha = formatter.format(now);
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


    public String getFecha() {
        return fecha;
    }
}
