package com.decobana.model;

public class ServicioProducto {
    private String codServicio;
    private String nombre;
    private String descripcion;
    private int idCategoria;
    private double precioUnitario;
    private boolean esTercero;
    private boolean estadoActivo;
    private Integer idProveedor; // puede ser null

    // getters/setters
    public String getCodServicio() { return codServicio; }
    public void setCodServicio(String codServicio) { this.codServicio = codServicio; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public boolean isEsTercero() { return esTercero; }
    public void setEsTercero(boolean esTercero) { this.esTercero = esTercero; }
    public boolean isEstadoActivo() { return estadoActivo; }
    public void setEstadoActivo(boolean estadoActivo) { this.estadoActivo = estadoActivo; }
    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }
}