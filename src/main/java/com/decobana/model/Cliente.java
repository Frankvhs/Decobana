package com.decobana.model;

public class Cliente {
    private int idCliente;
    private String nombre;
    private String apellidos;
    private String numDocumento;
    private String direccion;
    private String telefono;
    private String email;
    private boolean tratoPref;

    // getters/setters
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getNumDocumento() { return numDocumento; }
    public void setNumDocumento(String numDocumento) { this.numDocumento = numDocumento; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isTratoPref() { return tratoPref; }
    public void setTratoPref(boolean tratoPref) { this.tratoPref = tratoPref; }
}