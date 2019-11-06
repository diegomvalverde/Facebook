package com.example.proyectoii.Objetos;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

public class Usuario {
    private String correo;
    private String id;
    private String nombre;
    private String apellido;
    private String fechaNac;
    private String genero;
    private String telefono;
    private String ciudad;
    private String linkImgPerfil;
    private ArrayList<Educacion> educacicion;
    private ArrayList<String> amigos;


    public Usuario(String correo, String id, String nombre, String apellido, String fechaNac, String genero, String telefono, String ciudad, String linkImgPerfil, ArrayList<Educacion> educacicion, ArrayList<String> amigos) {
        this.correo = correo;
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNac = fechaNac;
        this.genero = genero;
        this.telefono = telefono;
        this.ciudad = ciudad;
        this.linkImgPerfil = linkImgPerfil;
        this.educacicion = educacicion;
        this.amigos = amigos;
    }

    public Usuario(String correo, String nombre, String apellido, String fechaNac, String genero) {
        this.correo = correo;
        this.id = "";
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNac = fechaNac;
        this.genero = genero;

        this.telefono = "";
        this.ciudad = "";
        this.linkImgPerfil = "";
        this.educacicion = new ArrayList<>();
        this.amigos = new ArrayList<>();

    }

    public Usuario() {
        this.amigos = new ArrayList<>();
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getLinkImgPerfil() {
        return linkImgPerfil;
    }

    public void setLinkImgPerfil(String linkImgPerfil) {
        this.linkImgPerfil = linkImgPerfil;
    }

    public ArrayList<Educacion> getEducacicion() {
        return educacicion;
    }

    public void setEducacicion(ArrayList<Educacion> educacicion) {
        this.educacicion = educacicion;
    }

    public ArrayList<String> getAmigos() {
        return amigos;
    }

    public void setAmigos(ArrayList<String> amigos) {
        this.amigos = amigos;
    }
}
