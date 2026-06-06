package com.decobana.model;

public class ContratoLinea {
    private String numContrato;
    private String codServicio;
    private int cantidad;
    private double precioNegociado;

    public String getNumContrato() { return numContrato; }
    public void setNumContrato(String numContrato) { this.numContrato = numContrato; }
    public String getCodServicio() { return codServicio; }
    public void setCodServicio(String codServicio) { this.codServicio = codServicio; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioNegociado() { return precioNegociado; }
    public void setPrecioNegociado(double precioNegociado) { this.precioNegociado = precioNegociado; }
}