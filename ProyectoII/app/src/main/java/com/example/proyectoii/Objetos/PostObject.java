package com.example.proyectoii.Objetos;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.proyectoii.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class PostObject {
    protected String idPost;
    protected String descripcion;
    protected String imageURI;
    protected String videoUrl;
    protected String authorId;
    protected String tipo;
    protected String fecha;


    public PostObject () {

    }


    public PostObject (String authorId, String descripcion,String idPost) {
        this.descripcion = descripcion;
        this.authorId = authorId;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        this.fecha = formatter.format(now);
        this.idPost = idPost;
    }

    public PostObject(String idPost, String descripcion, String imageURI, String videoUrl, String authorId, String tipo, String fecha) {
        this.idPost = idPost;
        this.descripcion = descripcion;
        this.imageURI = imageURI;
        this.videoUrl = videoUrl;
        this.authorId = authorId;
        this.tipo = tipo;
        this.fecha = fecha;
    }


    public String obtenerTimestampDifference(){

        String difference ;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Date today = c.getTime();
        sdf.format(today);
        Date timestamp;
        final String postTimestamp = this.fecha;
        try{
            timestamp = sdf.parse(postTimestamp);
            int diferenciaSegundos = Math.round(((today.getTime() - timestamp.getTime()) / 1000 ));
            if (diferenciaSegundos <  60){
                difference = "hace un momento";
            }
            else {
                int diferenciaMinutos =  Math.round(diferenciaSegundos/60);

                if (diferenciaMinutos < 60){
                    if(diferenciaMinutos == 1){
                        difference = "hace 1 minuto";
                    }
                    else {
                        difference = "hace " + diferenciaMinutos + " minutos";
                    }
                }
                else{
                    int diferenciaHoras =  Math.round(diferenciaMinutos/60);
                    
                    if(diferenciaHoras < 24){
                        if(diferenciaHoras == 1){
                            difference = "hace 1 hora";
                        }
                        else{
                            difference = "hace " + diferenciaHoras + " horas";
                        }

                    }
                    else{
                        int diferenciaDias = Math.round(diferenciaHoras/24);
                        
                        if (diferenciaDias < 7){
                            if(diferenciaDias == 1){
                                difference = "hace 1 día";
                            }
                            else {
                                difference = "hace " + diferenciaDias + " días";
                            }
                        }

                        else{
                            int diferenciaSemanas = Math.round(diferenciaDias/7);

                            if (diferenciaSemanas == 1){
                                difference = "hace 1 semana";
                            }
                            else{
                                difference = "hace " + diferenciaSemanas + " semanas";
                            }
                        }
                    }
                }

            }

        }catch (ParseException e){
            difference = "Error terminal";
        }


        return difference;
    }



    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }



    public void setTipo(String type) {
        tipo = type;
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


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }



    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

}
