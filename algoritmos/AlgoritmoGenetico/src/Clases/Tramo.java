package Clases;
                           

public class Tramo {
    private String ubigeoOrigen;
    private String ubigeoDestino;
    private Double distanciaTramo;
    private Double velocidadTramo;
    private Double horasTramo;

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

    public Double getVelocidadTramo() {
        return velocidadTramo;
    }

    public void setVelocidadTramo(Double velocidadTramo) {
        this.velocidadTramo = velocidadTramo;
    }

    public Double getHorasTramo() {
        return horasTramo;
    }

    public void setHorasTramo(Double horasTramo){
        this.horasTramo = horasTramo;
    }

    public Double getDistanciaTramo() {
        return distanciaTramo;
    }

    public void setDistanciaTramo(Double distanciaTramo) {
        this.distanciaTramo = distanciaTramo;
    }


    @Override
    public String toString() {
        return "Tramo{" +
                "ubigeoOrigen='" + ubigeoOrigen + '\'' +
                ", ubigeoDestino='" + ubigeoDestino + '\'' +
                '}';
    }


}