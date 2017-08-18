package com.misalud.saydex.misalud;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
/**
 * Created by vcoronado on 19-08-2016.
 */
public class WebService {
    //Namespace of the Webservice - can be found in WSDL
    private static String NAMESPACE = "http://tempuri.org/";
    //Webservice URL - WSDL File location
    private static String URL = "http://catest.saludsidra.cl/misalud/webservice/Servicios.asmx";//Make sure you changed IP address
    //SOAP Action URI again Namespace + Web method name
    private static String SOAP_ACTION = "http://tempuri.org/";

    public static String invokeLoginWS(String userName,String passWord, String webMethName) {
        boolean loginStatus = false;
        String retorno = "";
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo unamePI = new PropertyInfo();
        PropertyInfo passPI = new PropertyInfo();
        // Set Username
        unamePI.setName("usuario");
        // Set Value
        unamePI.setValue(userName);
        // Set dataType
        unamePI.setType(String.class);
        // Add the property to request object
        request.addProperty(unamePI);
        //Set Password
        passPI.setName("clave");
        //Set dataType
        passPI.setValue(passWord);
        //Set dataType
        passPI.setType(String.class);
        //Add the property to request object
        request.addProperty(passPI);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to  boolean variable variable
            loginStatus = true;
            retorno = response.toString();

        } catch (Exception e) {
            //Assign Error Status true in static variable 'errored'
            MainActivity.errored = true;
            retorno = e.getMessage();
            e.printStackTrace();
        }
        //Return booleam to calling object
        return retorno;
    }

    public static String invokeNotificaciones(String idUsuario, String webMethName) {
        String retorno = "";
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo usu = new PropertyInfo();
        // Set Username
        usu.setName("idUsuario");
        // Set Value
        usu.setValue(idUsuario);
        // Set dataType
        usu.setType(String.class);
        // Add the property to request object
        request.addProperty(usu);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to  boolean variable variable
            retorno = response.toString();

        } catch (Exception e) {
            //Assign Error Status true in static variable 'errored'
            //retorno = e.getMessage();
            e.printStackTrace();
            return "";
        }
        //Return booleam to calling object
        return retorno;
    }
}
