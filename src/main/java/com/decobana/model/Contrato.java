package com.decobana.model;

import java.time.LocalDate;
import java.util.List;

public class Contrato {
    private String numContrato;
    private LocalDate fechaContrato;
    private String terminosCond;
    private int idCliente;
    private int idEvento;
    private List<ContratoLinea> lineas;
    private List<Pago> pagos;
    private List<ModificacionContrato> modificaciones;

    // getters/setters
    public String getNumContrato() { return numContrato; }
    public void setNumContrato(String numContrato) { this.numContrato = numContrato; }
    public LocalDate getFechaContrato() { return fechaContrato; }
    public void setFechaContrato(LocalDate fechaContrato) { this.fechaContrato = fechaContrato; }
    public String getTerminosCond() { return terminosCond; }
    public void setTerminosCond(String terminosCond) { this.terminosCond = terminosCond; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public int getIdEvento() { return idEvento; }
    public void setIdEvento(int idEvento) { this.idEvento = idEvento; }
    public List<ContratoLinea> getLineas() { return lineas; }
    public void setLineas(List<ContratoLinea> lineas) { this.lineas = lineas; }
    public List<Pago> getPagos() { return pagos; }
    public void setPagos(List<Pago> pagos) { this.pagos = pagos; }
    public List<ModificacionContrato> getModificaciones() { return modificaciones; }
    public void setModificaciones(List<ModificacionContrato> modificaciones) { this.modificaciones = modificaciones; }
}