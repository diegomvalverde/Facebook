package com.example.proyectoii.Objetos;

import android.os.Parcel;
import android.os.Parcelable;

public class Comentario implements Parcelable {
    private String idAutor;
    private String comentario;


    public Comentario(String idAutor, String comentario) {
        this.idAutor = idAutor;
        this.comentario = comentario;
    }

    protected Comentario(Parcel in) {
        idAutor = in.readString();
        comentario = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idAutor);
        dest.writeString(comentario);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comentario> CREATOR = new Creator<Comentario>() {
        @Override
        public Comentario createFromParcel(Parcel in) {
            return new Comentario(in);
        }

        @Override
        public Comentario[] newArray(int size) {
            return new Comentario[size];
        }
    };

    public String getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(String idAutor) {
        this.idAutor = idAutor;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
