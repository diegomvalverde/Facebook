package com.example.proyectoii.Objetos;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;


public class Reaccion implements Parcelable {
    private String idAutor;
    private int tipoReaccion;

    public Reaccion(String idAutor, int tipoReaccion) {
        this.idAutor = idAutor;
        this.tipoReaccion = tipoReaccion;
    }

    protected Reaccion(Parcel in) {
        idAutor = in.readString();
        tipoReaccion = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idAutor);
        dest.writeInt(tipoReaccion);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Reaccion> CREATOR = new Creator<Reaccion>() {
        @Override
        public Reaccion createFromParcel(Parcel in) {
            return new Reaccion(in);
        }

        @Override
        public Reaccion[] newArray(int size) {
            return new Reaccion[size];
        }
    };

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
