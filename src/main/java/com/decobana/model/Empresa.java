package com.decobana.model;

public class Empresa {
    private int codEmp;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String directorGeneral;
    private String jefeRRHH;
    private String jefeContabilidad;
    private String secretarioSindicato;

    // Getters and setters
    public int getCodEmp() { return codEmp; }
    public void setCodEmp(int codEmp) { this.codEmp = codEmp; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDirectorGeneral() { return directorGeneral; }
    public void setDirectorGeneral(String directorGeneral) { this.directorGeneral = directorGeneral; }
    public String getJefeRRHH() { return jefeRRHH; }
    public void setJefeRRHH(String jefeRRHH) { this.jefeRRHH = jefeRRHH; }
    public String getJefeContabilidad() { return jefeContabilidad; }
    public void setJefeContabilidad(String jefeContabilidad) { this.jefeContabilidad = jefeContabilidad; }
    public String getSecretarioSindicato() { return secretarioSindicato; }
    public void setSecretarioSindicato(String secretarioSindicato) { this.secretarioSindicato = secretarioSindicato; }
}