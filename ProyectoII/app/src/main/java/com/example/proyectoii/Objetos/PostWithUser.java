package com.example.proyectoii.Objetos;

public class PostWithUser extends PostObject {
    private String authorName;

    public PostWithUser(String authorId, String descripcion,String tipo, String authorName) {
        super(authorId, descripcion);
        this.setTipo(tipo);
        this.authorName = authorName;

    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
