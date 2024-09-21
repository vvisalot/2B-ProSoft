package Clases;

public class Velocidad {
    private String regionOrigen;
    private String regionDestino;
    private double velocidadKmh;

    // Constructor con los 3 parámetros necesarios
    public Velocidad(String regionOrigen, String regionDestino, double velocidadKmh) {
        this.regionOrigen = regionOrigen;
        this.regionDestino = regionDestino;
        this.velocidadKmh = velocidadKmh;
    }

    // Getters y Setters si son necesarios
    public String getRegionOrigen() {
        return regionOrigen;
    }

    public void setRegionOrigen(String regionOrigen) {
        this.regionOrigen = regionOrigen;
    }

    public String getRegionDestino() {
        return regionDestino;
    }

    public void setRegionDestino(String regionDestino) {
        this.regionDestino = regionDestino;
    }

    public double getVelocidadKmh() {
        return velocidadKmh;
    }

    public void setVelocidadKmh(double velocidadKmh) {
        this.velocidadKmh = velocidadKmh;
    }

    @Override
    public String toString() {
        return "Velocidad{" +
                "regionOrigen='" + regionOrigen + '\'' +
                ", regionDestino='" + regionDestino + '\'' +
                ", velocidadKmh=" + velocidadKmh +
                '}';
    }
}