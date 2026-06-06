package com.decobana.model;

import java.time.LocalDateTime;
import java.util.List;

public class Evento {
    private int idEvento;
    private String nombreEvento;
    private String tipoEvento;
    private LocalDateTime fechaHoraIni;
    private LocalDateTime fechaHoraFin;
    private String ubicacion;
    private int numInvitados;
    private int idCliente;
    // Listas asociadas (para uso en UI, no persistidas directamente)
    private List<String> temas;
    private List<EventoServicio> servicios;
    private List<EventoEmpleado> empleados;

    // Getters/setters
    public int getIdEvento() { return idEvento; }
    public void setIdEvento(int idEvento) { this.idEvento = idEvento; }
    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }
    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }
    public LocalDateTime getFechaHoraIni() { return fechaHoraIni; }
    public void setFechaHoraIni(LocalDateTime fechaHoraIni) { this.fechaHoraIni = fechaHoraIni; }
    public LocalDateTime getFechaHoraFin() { return fechaHoraFin; }
    public void setFechaHoraFin(LocalDateTime fechaHoraFin) { this.fechaHoraFin = fechaHoraFin; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public int getNumInvitados() { return numInvitados; }
    public void setNumInvitados(int numInvitados) { this.numInvitados = numInvitados; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public List<String> getTemas() { return temas; }
    public void setTemas(List<String> temas) { this.temas = temas; }
    public List<EventoServicio> getServicios() { return servicios; }
    public void setServicios(List<EventoServicio> servicios) { this.servicios = servicios; }
    public List<EventoEmpleado> getEmpleados() { return empleados; }
    public void setEmpleados(List<EventoEmpleado> empleados) { this.empleados = empleados; }
}