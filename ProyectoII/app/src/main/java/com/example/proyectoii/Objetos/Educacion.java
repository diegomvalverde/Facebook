package com.example.proyectoii.Objetos;

import java.util.Date;

public class Educacion {
    private String nombreInstitucion;
    private String tipoInstitucion; //Universidad, escuela , ...

    public Educacion(String nombreInstitucion, String tipoInstitucion) {
        this.nombreInstitucion = nombreInstitucion;
        this.tipoInstitucion = tipoInstitucion;
    }

    public String getNombreInstitucion() {
        return nombreInstitucion;
    }

    public String getTipoInstitucion() {
        return tipoInstitucion;
    }
}
