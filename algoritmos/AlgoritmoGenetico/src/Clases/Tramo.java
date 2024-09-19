package Clases;
                           
public class Tramo {
    private String ubigeoOrigen;
    private String ubigeoDestino;

    public Tramo(String ubigeoOrigen, String ubigeoDestino) {
        this.ubigeoOrigen = ubigeoOrigen;
        this.ubigeoDestino = ubigeoDestino;
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