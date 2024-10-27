package grupo2b.odiroute.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Paquete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;
    
    @Column(name = "cantidad", nullable = false)
    private int cantidad;
    
    @ManyToMany(mappedBy = "paquetes")
    private List<Camion> camiones = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "ruta_id")
    private Ruta ruta;

    public Paquete() {
        this.venta = null;
        this.cantidad = 0;
    }
    
    public Paquete(Venta venta, int cantidad) {
        this.venta = venta;
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Camion> getCamiones() {
        return camiones;
    }

    public void setCamiones(List<Camion> camiones) {
        this.camiones = camiones;
    }

    
    public Venta getVenta() {
        return venta;
    }

    public int getCantidad() {
        return cantidad;
    }

    @Override
    public String toString() {
        return "Paquete{" +
                "id=" + id +
                ", venta=" + venta +
                ", cantidad=" + cantidad +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Paquete other = (Paquete) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
