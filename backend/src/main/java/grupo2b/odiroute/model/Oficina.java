package grupo2b.odiroute.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Oficina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;
    
    @Column(name = "departamento", nullable = false)
    private String departamento;
    
    @Column(name = "provincia", nullable = false)
    private String provincia;
    
    @Column(name = "latitud", nullable = false)
    private double latitud;
    
    @Column(name = "longitud", nullable = false)
    private double longitud;
    
    @Column(name = "region", nullable = false)
    private String region;
    
    @Column(name = "capacidad", nullable = false)
    private int capacidad;
    
    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    public Oficina() {
    }
    
    public Oficina(String codigo) {
        this.codigo = codigo;
    }

    public Oficina(String codigo, String departamento, String provincia, double latitud, double longitud, String region, int capacidad) {
        this.codigo = codigo;
        this.departamento = departamento;
        this.provincia = provincia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.region = region;
        this.capacidad = capacidad;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
    
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getAlmacen() {
        return capacidad;
    }

    public void setAlmacen(int almacen) {
        this.capacidad = almacen;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Oficina{" +
                "ubigeo='" + codigo + '\'' +
                ", departamento='" + departamento + '\'' +
                ", provincia='" + provincia + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", regionNatural='" + region + '\'' +
                ", almacen='" + capacidad + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true; // Check if both are the same object
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Check if obj is null or a different class
        }
        Oficina other = (Oficina) obj;
        return Objects.equals(codigo, other.codigo);
    }

    @Override
    public int hashCode() {
        if (codigo == null) {
            return 0;
        }
        return codigo.hashCode();
    }
}
