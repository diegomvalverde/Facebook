package com.example.proyectoii.Objetos;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.proyectoii.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostWithUser extends PostObject implements Parcelable {
    private String authorName;
    private String authorPhoto;
    private ArrayList<Reaccion> reacciones;
    private ArrayList<Comentario> comentarios;

    public PostWithUser(){
        reacciones = new ArrayList<>();
        comentarios = new ArrayList<>();
    }

    public PostWithUser(String authorId, String descripcion,String tipo,String idPost, String authorName,String authorPhoto) {
        super(authorId, descripcion,idPost);
        this.setTipo(tipo);
        this.authorName = authorName;
        reacciones = new ArrayList<>();
        comentarios = new ArrayList<>();
        this.authorPhoto = authorPhoto;

    }

    public PostWithUser(String idPost, String descripcion, String imageURI, String videoUrl, String authorId, String tipo, String fecha, String authorName, String authorPhoto, ArrayList<Reaccion> reacciones, ArrayList<Comentario> comentarios) {
        super(idPost, descripcion, imageURI, videoUrl, authorId, tipo, fecha);
        this.authorName = authorName;
        this.authorPhoto = authorPhoto;
        this.reacciones = reacciones;
        this.comentarios = comentarios;



    }


    protected PostWithUser(Parcel in) {
        idPost = in.readString();
        descripcion = in.readString();
        imageURI = in.readString();
        videoUrl = in.readString();
        authorId = in.readString();
        tipo = in.readString();
        fecha = in.readString();
        authorName = in.readString();
        authorPhoto = in.readString();
        reacciones = in.createTypedArrayList(Reaccion.CREATOR);
        comentarios = in.createTypedArrayList(Comentario.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idPost);
        dest.writeString(descripcion);
        dest.writeString(imageURI);
        dest.writeString(videoUrl);
        dest.writeString(authorId);
        dest.writeString(tipo);
        dest.writeString(fecha);
        dest.writeString(authorName);
        dest.writeString(authorPhoto);
        dest.writeTypedList(reacciones);
        dest.writeTypedList(comentarios);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostWithUser> CREATOR = new Creator<PostWithUser>() {
        @Override
        public PostWithUser createFromParcel(Parcel in) {
            return new PostWithUser(in);
        }

        @Override
        public PostWithUser[] newArray(int size) {
            return new PostWithUser[size];
        }
    };

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int[] getTopReacciones(){
        int[] topReacciones = {0,0,0};
        Map<Integer, Integer> map = new HashMap<>();
        for(Reaccion reaccion:this.reacciones){
            Integer val = map.get(reaccion.getTipoReaccion());
            map.put(reaccion.getTipoReaccion(), val == null ? 1 : val + 1);
        }

        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            if ( e.getValue() > topReacciones[0]){
                topReacciones[2] = topReacciones[1];
                topReacciones[1] = topReacciones[0];
                topReacciones[0] = e.getKey();
            }
            else if(e.getValue() > topReacciones[1]){
                topReacciones[2] = topReacciones[1];
                topReacciones[1] = e.getKey();
            }
            else if(e.getValue() > topReacciones[2]){
                topReacciones[2] = e.getKey();
            }
        }

        topReacciones[0] = intToReaccion(topReacciones[0]);
        topReacciones[1] = intToReaccion(topReacciones[1]);
        topReacciones[2] = intToReaccion(topReacciones[2]);


        return topReacciones;

    }

    public int intToReaccion(int numeroReaccion){
        int idImagenReaccion;
        switch (numeroReaccion) {
            case 1 :
                idImagenReaccion = R.drawable.ic_like;
                break;
            case 2 :
                idImagenReaccion = R.drawable.ic_heart;
                break;
            case 3 :
                idImagenReaccion = R.drawable.ic_happy;
                break;
            case 4 :
                idImagenReaccion = R.drawable.ic_surprise;
                break;
            case 5 :
                idImagenReaccion = R.mipmap.ic_dislike;
                break;
            case 6 :
                idImagenReaccion = R.drawable.ic_angry;
                break;
            default:
                idImagenReaccion = 0;

        }
        return  idImagenReaccion;
    }

    public ArrayList<Reaccion> getReacciones() {
        return reacciones;
    }

    public void setReacciones(ArrayList<Reaccion> reacciones) {
        this.reacciones = reacciones;
    }

    public ArrayList<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(ArrayList<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public String getAuthorPhoto() {
        return authorPhoto;
    }

    public void setAuthorPhoto(String authorPhoto) {
        this.authorPhoto = authorPhoto;
    }

    @NonNull
    @Override
    public String toString() {
        String msg = "Autor : \n\tId: " + this.authorId +"\n\t Nombre: " +this.authorName + "\n\t Foto: " + this.authorPhoto +
                "Post : \n\tId" + this.idPost + "\n\tTipo: " + this.tipo + "\n\tImagen: " + this.imageURI + "\n\tVideo: " + this.videoUrl + "\n\tDescripcion: " +this.descripcion ;
        msg += "\n\tReacciones: ";

        for (Reaccion reaccion: this.reacciones){
            msg += "\n\t\tReaccion: \n\t\t\tAutor: " + reaccion.getIdAutor() + "\n\t\t\tTipo:" + reaccion.getTipoReaccion();
        }
        msg += "\n\tComentarios: \n";
        for (Comentario comentario: this.comentarios){
            msg += "\n\t\tComentario: \n\t\t\tAutor: " + comentario.getIdAutor() + "\n\t\t\tTexto:" + comentario.getComentario();
        }

        return msg;
    }


}
