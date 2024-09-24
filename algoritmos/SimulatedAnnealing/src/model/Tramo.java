package model;

public class Tramo {
    private Bloqueo bloqueo;
    private int distancia;

    private Oficina origen;
    private Oficina destino;
    private int velocidad;

    public Tramo(String ubigeoOrigen, String ubigeoDestino) {
        //ESTO SE LLENA Y SE LIMPIA DEPENDIENDO DEL ARCHIVO
        this.bloqueo = null;
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
        return "Tramo{" +
                "Duracion del bloqueo=" + bloqueo.getFechaHoraInicio() + "=>" + bloqueo.getFechaHoraFin() +
                ", distancia=" + distancia +
                ", origen=" + origen.getCodigo() +
                ", destino=" + destino.getCodigo() +
                '}';
    }
}