package com.example.proyectoii.Objetos;

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

    public void setTipo(int type) {
        switch (type) {
            case 0:
                tipo = "TEXTO";
                break;
            case 1:
                tipo = "IMAGE";
                break;
            case 2:
                tipo = "VIDEO";
                break;
        }
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
