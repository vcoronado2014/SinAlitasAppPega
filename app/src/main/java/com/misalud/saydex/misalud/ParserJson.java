package com.misalud.saydex.misalud;

import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vcoronado on 22-08-2016.
 */
public class ParserJson {
    public static Usuario leerJson(String jsonStr) throws JSONException {

        //declaracion de variables de la clase
        int id= 0;
        int nodId = 0;
        String rut = null;
        String nombres = null;
        String apellidoPaterno = null;
        String apellidoMaterno = null;
        String direccion = null;

        //org.json.JSONObject jsonObj = new org.json.JSONObject(jsonStr);
        JSONArray jsonObj = new JSONArray(jsonStr);

        //org.json.JSONObject jsonObjAu =jsonObj.getJSONObject("");
        if (jsonObj != null)
        {
            for (int i = 0; i < jsonObj.length(); i++) {
                JSONObject jsonobject = jsonObj.getJSONObject(i);
                id = jsonobject.getInt("Id");
                nodId = jsonobject.getInt("NodId");
                nombres = jsonobject.getString("Nombres");
                apellidoPaterno = jsonobject.getString("ApellidoPaterno");
                apellidoMaterno = jsonobject.getString("ApellidoMaterno");
                direccion = jsonobject.getString("Direccion");
            }
            //id = jsonObj.get(0);
/*
                id = jsonObj.getInt("Id");
                nodId = jsonObj.getInt("NodId");
                nombres = jsonObj.getString("Nombres");
                apellidoPaterno = jsonObj.getString("ApellidoPaterno");
                apellidoMaterno = jsonObj.getString("ApellidoMaterno");
                direccion = jsonObj.getString("Direccion");
*/
        }


        //JSONArray jsonArrayUs = jsonObj.getJSONArray("AutentificacionUsuario");
        //JSONArray jsonArrayPer = jsonObj.getJSONArray("Persona");
        //JSONArray mJsonArray = new JSONArray(jsonStr);
        //JSONObject mJsonObject = mJsonArray.getJSONObject(0);

        Usuario us = new Usuario(id, nodId, nombres, apellidoPaterno, apellidoMaterno, rut, direccion);
        return us;

    }

    public static Notificaciones leerJsonNot(String jsonStr) throws JSONException {

        //declaracion de variables de la clase
        int id= 0;
        int tipo = 0;
        String rut = null;
        String nombre = null;
        String detalle = null;
        String fecha = null;
        String url = null;
        JSONArray lineas = null;

        org.json.JSONObject jsonObj = new org.json.JSONObject(jsonStr);

        //org.json.JSONObject jsonObjAu =jsonObj.getJSONObject("");
        if (jsonObj != null)
        {

            id = jsonObj.getInt("Id");
            tipo = jsonObj.getInt("Tipo");
            nombre = jsonObj.getString("Nombre");
            detalle = jsonObj.getString("Detalle");
            fecha = jsonObj.getString("Fecha");
            url  = jsonObj.getString("Url");
            lineas  = jsonObj.getJSONArray("Lineas");

        }


        //JSONArray jsonArrayUs = jsonObj.getJSONArray("AutentificacionUsuario");
        //JSONArray jsonArrayPer = jsonObj.getJSONArray("Persona");
        //JSONArray mJsonArray = new JSONArray(jsonStr);
        //JSONObject mJsonObject = mJsonArray.getJSONObject(0);

        Notificaciones not = new Notificaciones(id, tipo, nombre, detalle, fecha, url, lineas);
        return not;

    }

}
