package com.example.proyectoii.Objetos;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Comentario implements Parcelable {
    private String idAutor;
    private String comentario;
    private String fecha;
    private String nombreAutor;
    private String imgUrlAutor;

    public Comentario (){

    }
    public Comentario(String idAutor, String comentario) {
        this.idAutor = idAutor;
        this.comentario = comentario;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        this.fecha = formatter.format(now);
    }

    public Comentario(String idAutor, String comentario, String fecha) {
        this.idAutor = idAutor;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    public Comentario(String idAutor, String comentario, String fecha, String nombreAutor, String imgUrlAutor) {
        this.idAutor = idAutor;
        this.comentario = comentario;
        this.fecha = fecha;
        this.nombreAutor = nombreAutor;
        this.imgUrlAutor = imgUrlAutor;
    }


    protected Comentario(Parcel in) {
        idAutor = in.readString();
        comentario = in.readString();
        fecha = in.readString();
        nombreAutor = in.readString();
        imgUrlAutor = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idAutor);
        dest.writeString(comentario);
        dest.writeString(fecha);
        dest.writeString(nombreAutor);
        dest.writeString(imgUrlAutor);
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public String getImgUrlAutor() {
        return imgUrlAutor;
    }

    public void setImgUrlAutor(String imgUrlAutor) {
        this.imgUrlAutor = imgUrlAutor;
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
}
