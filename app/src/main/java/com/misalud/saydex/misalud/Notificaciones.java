package com.misalud.saydex.misalud;

import org.json.JSONArray;

import java.lang.reflect.Array;

/**
 * Created by vcoronado on 03-10-2016.
 */
public class Notificaciones {
    private int Id;
    private int Tipo;
    private String Nombre;
    private String Detalle;
    private String Fecha;
    private String Url;
    private JSONArray Lineas;
    public Notificaciones()
    {

    }
    public Notificaciones(int id, int tipo, String nombre, String detalle, String fecha)
    {
        this.Id  = id;
        this.Tipo = tipo;
        this.Nombre = nombre;
        this.Detalle = detalle;
        this.Fecha = fecha;
    }
    public Notificaciones(int id, int tipo, String nombre, String detalle, String fecha, String url, JSONArray lineas)
    {
        this.Id  = id;
        this.Tipo = tipo;
        this.Nombre = nombre;
        this.Detalle = detalle;
        this.Fecha = fecha;
        this.Url = url;
        this.Lineas = lineas;
    }
    public int getId(){
        return Id;
    }
    public void  setId(int id)
    {
        this.Id = id;
    }
    public int getTipo(){
        return Tipo;
    }
    public void  setTipo(int tipo)
    {
        this.Tipo = tipo;
    }
    public String getNombre(){
        return Nombre;
    }
    public void  setNombre(String nombre)
    {
        this.Nombre = nombre;
    }
    public String getDetalle(){
        return Detalle;
    }
    public void  setDetalle(String detalle)
    {
        this.Detalle = detalle;
    }
    public String getFecha(){
        return Fecha;
    }
    public void  setFecha(String fecha)
    {
        this.Fecha = fecha;
    }
    public String getUrl(){
        return Url;
    }
    public void  setUrl(String url)
    {
        this.Url = url;
    }
    public JSONArray  getLineas(){
        return Lineas;
    }
    public void  setLineas(JSONArray lineas)
    {
        this.Lineas = lineas;
    }
}
