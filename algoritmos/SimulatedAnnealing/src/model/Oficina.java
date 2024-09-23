package model;

public class Oficina {
    private String ubigeo;
    private String departamento;
    private String provincia;
    private double latitud;
    private double longitud;
    private String regionNatural;
    private int capacidad;

    public Oficina(String ubigeo, String departamento, String provincia, double latitud, double longitud, String regionNatural, int capacidad) {
        this.ubigeo = ubigeo;
        this.departamento = departamento;
        this.provincia = provincia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.regionNatural = regionNatural;
        this.capacidad = capacidad;
    }

    // Getters y Setters
    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getRegionNatural() {
        return regionNatural;
    }

    public void setRegionNatural(String regionNatural) {
        this.regionNatural = regionNatural;
    }

    public int getAlmacen() {
        return capacidad;
    }

    public void setAlmacen(int almacen) {
        this.capacidad = almacen;
    }

    @Override
    public String toString() {
        return "Oficina{" +
                "ubigeo='" + ubigeo + '\'' +
                ", departamento='" + departamento + '\'' +
                ", provincia='" + provincia + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", regionNatural='" + regionNatural + '\'' +
                ", almacen='" + capacidad + '\'' +
                '}';
    }
}