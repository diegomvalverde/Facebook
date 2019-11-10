package com.example.proyectoii.Objetos;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.proyectoii.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostWithUser extends PostObject  {
    private String authorName;
    private String authorPhoto;
    private ArrayList<Reaccion> reacciones;
    private ArrayList<Comentario> comentarios;

    public PostWithUser(String authorId, String descripcion,String tipo,String idPost, String authorName,String authorPhoto) {
        super(authorId, descripcion,idPost);
        this.setTipo(tipo);
        this.authorName = authorName;
        reacciones = new ArrayList<>();
        comentarios = new ArrayList<>();
        this.authorPhoto = authorPhoto;

    }

    protected PostWithUser(Parcel in) {
        authorName = in.readString();
    }


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
}
