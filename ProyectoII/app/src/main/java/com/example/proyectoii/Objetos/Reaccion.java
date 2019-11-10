package com.example.proyectoii.Objetos;

import android.util.Log;

import androidx.annotation.Nullable;


public class Reaccion {
    private String idAutor;
    private int tipoReaccion;

    public Reaccion(String idAutor, int tipoReaccion) {
        this.idAutor = idAutor;
        this.tipoReaccion = tipoReaccion;
    }

    public String getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(String idAutor) {
        this.idAutor = idAutor;
    }

    public int getTipoReaccion() {
        return tipoReaccion;
    }

    public void setTipoReaccion(int tipoReaccion) {
        this.tipoReaccion = tipoReaccion;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == this){
            return  true;
        }
        else if(obj == null || obj.getClass() != this.getClass()){
            return false;
        }
        else{
            Reaccion reaccion = (Reaccion) obj;
            return reaccion.idAutor.equals(this.idAutor);
        }

    }
}
