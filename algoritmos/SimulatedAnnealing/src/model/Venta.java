package model;

import java.time.LocalDateTime;

public class Venta {
    private LocalDateTime fechaHora;
    private Oficina origen;
    private Oficina destino;
    private int cantidad;
    private String idCliente;

    // Getters y Setters
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public Oficina getOrigen() {
        return origen;
    }

    public void setOrigen(Oficina origen) {
        this.origen = origen;
    }

    public Oficina getDestino() {
        return destino;
    }

    public void setDestino(Oficina destino) {
        this.destino = destino;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "fechaHora=" + fechaHora +
                ", ubigeoDestino='" + destino.getCodigo() + '\'' +
                ", cantidad=" + cantidad +
                ", idCliente='" + idCliente + '\'' +
                '}';
    }
}