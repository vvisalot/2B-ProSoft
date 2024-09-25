package model;

import java.util.Objects;

// Una oficina representa un nodo en nuestro grafo. Cada una tiene un ID unico y atributos extra y una
// lista de vecinos que son otras instancias de Oficina
public class Oficina {
    private String codigo;
    private String departamento;
    private String provincia;
    private double latitud;
    private double longitud;
    private String regionNatural;
    private int capacidad;

    public Oficina(String codigo, String departamento, String provincia, double latitud, double longitud, String regionNatural, int capacidad) {
        this.codigo = codigo;
        this.departamento = departamento;
        this.provincia = provincia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.regionNatural = regionNatural;
        this.capacidad = capacidad;
    }

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
                "ubigeo='" + codigo + '\'' +
                ", departamento='" + departamento + '\'' +
                ", provincia='" + provincia + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", regionNatural='" + regionNatural + '\'' +
                ", almacen='" + capacidad + '\'' +
                '}';
    }



}