package model;

public class Tramo {
    private Bloqueo bloqueo;
    private int distancia;

    private Ubigeo origen;
    private Ubigeo destino;

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

    public Ubigeo getOrigen() {
        return origen;
    }

    public void setOrigen(Ubigeo origen) {
        this.origen = origen;
    }

    public Ubigeo getDestino() {
        return destino;
    }

    public void setDestino(Ubigeo destino) {
        this.destino = destino;
    }

    @Override
    public String toString() {
        return "Tramo{" +
                "Duracion del bloqueo=" + bloqueo.getFechaHoraInicio() + "=>" + bloqueo.getFechaHoraFin() +
                ", distancia=" + distancia +
                ", origen=" + origen.getCiudad() +
                ", destino=" + destino.getCiudad() +
                '}';
    }
}