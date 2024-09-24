package model;

public class Tramo {
    private Bloqueo bloqueo = null;
    private int distancia= 0;

    private Oficina origen;
    private Oficina destino;

    public Tramo(Oficina origen, Oficina destino) {
        this.origen = origen;
        this.destino = destino;
    }

    public Bloqueo getBloqueo() {
        return bloqueo;
    }

    public void setBloqueo(Bloqueo bloqueo) {
        this.bloqueo = bloqueo;
    }

    public int getDistancia() {
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
    public String toString() {
        return "Tramo{" + "origen=" + origen.getCodigo() + ", destino=" + destino.getCodigo() + '}';
    }
}