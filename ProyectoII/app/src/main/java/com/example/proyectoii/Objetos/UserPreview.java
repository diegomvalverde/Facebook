package com.example.proyectoii.Objetos;

public class UserPreview {

    public String nombre;
    public String id;
    public String profilepic;

    public UserPreview(String name, String id) {
        nombre = name;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public UserPreview(String nombre, String id, String profilepic) {
        this.nombre = nombre;
        this.id = id;
        this.profilepic = profilepic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }
}
