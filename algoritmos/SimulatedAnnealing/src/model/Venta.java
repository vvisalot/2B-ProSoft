package model;

import java.time.LocalDateTime;

public class Venta {
    private LocalDateTime fechaHora;
    private Ubigeo ubigeoOrigen;
    private Ubigeo ubigeoDestino;
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

    public Ubigeo getUbigeoOrigen() {
        return ubigeoOrigen;
    }

    public void setUbigeoOrigen(Ubigeo ubigeoOrigen) {
        this.ubigeoOrigen = ubigeoOrigen;
    }

    public Ubigeo getUbigeoDestino() {
        return ubigeoDestino;
    }

    public void setUbigeoDestino(Ubigeo ubigeoDestino) {
        this.ubigeoDestino = ubigeoDestino;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "fechaHora=" + fechaHora +
                ", ubigeoOrigen='" + ubigeoOrigen.getCiudad() + '\'' +
                ", ubigeoDestino='" + ubigeoDestino.getCiudad() + '\'' +
                ", cantidad=" + cantidad +
                ", idCliente='" + idCliente + '\'' +
                '}';
    }
}