package com.decobana.model;

import java.time.LocalDate;

public class ModificacionContrato {
    private int idMod;
    private String numContrato;
    private LocalDate fechaMod;
    private String descripcion;

    public int getIdMod() { return idMod; }
    public void setIdMod(int idMod) { this.idMod = idMod; }
    public String getNumContrato() { return numContrato; }
    public void setNumContrato(String numContrato) { this.numContrato = numContrato; }
    public LocalDate getFechaMod() { return fechaMod; }
    public void setFechaMod(LocalDate fechaMod) { this.fechaMod = fechaMod; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}