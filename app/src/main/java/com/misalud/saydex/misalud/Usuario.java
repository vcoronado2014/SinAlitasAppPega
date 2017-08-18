package com.misalud.saydex.misalud;

/**
 * Created by vcoronado on 22-08-2016.
 */
public class Usuario {

    private int Id;
    private int NodId;
    private String Nombres;
    private String ApellidoPaterno;
    private String ApellidoMaterno;
    private String Rut;
    private String Direccion;
    private int RolId;

    public Usuario ()
    {

    }
    public Usuario(int Id, int NodId, String Nombres, String ApellidoPaterno, String ApellidoMaterno, String Rut, String Direccion)
    {
        this.Id = Id;
        this.NodId = NodId;
        this.Nombres = Nombres;
        this.ApellidoPaterno = ApellidoPaterno;
        this.ApellidoMaterno = ApellidoMaterno;
        this.Rut = Rut;
        this.Direccion = Direccion;
    }
    //Id
    public int getId(){
        return Id;
    }
    public void  setId(int Id)
    {
        this.Id = Id;
    }
    //NodId
    public int getNodId(){
        return NodId;
    }
    public void  setNodId(int NodId)
    {
        this.NodId = NodId;
    }
    //Nombres
    public String getNombres(){
        return Nombres;
    }
    public void  setNombres(String Nombres)
    {
        this.Nombres = Nombres;
    }
    //ApellidoPaterno
    public String getApellidoPaterno(){
        return ApellidoPaterno;
    }
    public void  setApellidoPaterno(String ApellidoPaterno)
    {
        this.ApellidoPaterno = ApellidoPaterno;
    }
    //ApellidoMaterno
    public String getApellidoMaterno(){
        return ApellidoPaterno;
    }
    public void  setApellidoMaterno(String ApellidoMaterno)
    {
        this.ApellidoMaterno = ApellidoMaterno;
    }
    //Rut
    public String getRut(){
        return Rut;
    }
    public void  setRut(String Rut)
    {
        this.Rut = Rut;
    }
    //Direccion
    public String getDireccion(){
        return Direccion;
    }
    public void  setDireccion(String Direccion)
    {
        this.Direccion = Direccion;
    }
}
