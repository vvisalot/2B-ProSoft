package model;

public class Tramo {
    private String ubigeoOrigen;
    private String ubigeoDestino;
    private Bloqueo bloqueo;

    public Tramo(String ubigeoOrigen, String ubigeoDestino) {
        this.ubigeoOrigen = ubigeoOrigen;
        this.ubigeoDestino = ubigeoDestino;
        this.bloqueo = null;
    }

    // Funcion para agregarle un bloqueo al tramo!
    public void asignarBloqueo(Bloqueo bloqueo) {
        if (this.bloqueo != null) { //Esta prohibido que un tramo tenga mas de un bloqueo.
            throw new IllegalStateException("Este tramo ya tiene un bloqueo.");
        }
        this.bloqueo = bloqueo;
    }

    // Getters y Setters
    public String getUbigeoOrigen() {
        return ubigeoOrigen;
    }

    public void setUbigeoOrigen(String ubigeoOrigen) {
        this.ubigeoOrigen = ubigeoOrigen;
    }

    public String getUbigeoDestino() {
        return ubigeoDestino;
    }

    public void setUbigeoDestino(String ubigeoDestino) {
        this.ubigeoDestino = ubigeoDestino;
    }

    @Override
    public String toString() {
        return "Tramo{" +
                "ubigeoOrigen='" + ubigeoOrigen + '\'' +
                ", ubigeoDestino='" + ubigeoDestino + '\'' +
                '}';
    }
}