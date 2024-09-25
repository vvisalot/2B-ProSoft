package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tramo {
    private List<Bloqueo> bloqueos = null;
    private double distancia;

    private Oficina origen;
    private Oficina destino;

    public Tramo(Oficina origen, Oficina destino, double distancia) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.bloqueos= new ArrayList<>();
    }

    public void agregarBloqueo(Bloqueo bloqueo) {
        bloqueos.add(bloqueo);
    }

    public void eliminarBloqueo(Bloqueo bloqueo) {
        bloqueos.remove(bloqueo);
    }

    public List<Bloqueo> getBloqueos() {
        return bloqueos;
    }

    public void setBloqueos(List<Bloqueo> bloqueos) {
        this.bloqueos = bloqueos;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Check if both are the same object
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Check if obj is null or a different class
        }
        Tramo other = (Tramo) obj;
        var thisCode = origen.getCodigo() + destino.getCodigo();
        var otherCode = other.origen.getCodigo() + other.destino.getCodigo();
        return Objects.equals(thisCode, otherCode);
    }

    @Override
    public int hashCode() {
        if (origen == null || destino == null) {
            return 0;
        }
        var codigo = origen.getCodigo() + destino.getCodigo();
        return codigo.hashCode();
    }
    @Override
    public String toString() {
        return "Tramo{" + "origen=" + origen.getCodigo() + ", destino=" + destino.getCodigo() + '}';
    }
}