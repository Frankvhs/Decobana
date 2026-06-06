package com.decobana.model;

import java.time.LocalDate;

public class Pago {
    private int idPago;
    private String numContrato;
    private LocalDate fechaPago;
    private double monto;

    public int getIdPago() { return idPago; }
    public void setIdPago(int idPago) { this.idPago = idPago; }
    public String getNumContrato() { return numContrato; }
    public void setNumContrato(String numContrato) { this.numContrato = numContrato; }
    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
}