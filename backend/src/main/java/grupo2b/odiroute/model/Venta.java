package grupo2b.odiroute.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Venta implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private final UUID codigo;
    
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    
    @ManyToOne
    @JoinColumn(name = "oficina_origen_id", nullable = false)
    private Oficina origen;
    
    @ManyToOne
    @JoinColumn(name = "oficina_destino_id", nullable = false)
    private Oficina destino;
    
    @Column(name = "cantidad", nullable = false)
    private int cantidad;
    
    @Column(name = "id_cliente", nullable = false)
    private String idCliente;


    public Venta() {
        this.codigo = UUID.randomUUID();
        this.fechaHora = LocalDateTime.now();
    }

    public Venta(Oficina destino) {
        this.codigo = UUID.randomUUID();
        this.destino = destino;
    }
    
    public Venta(Oficina origen, Oficina destino, int cantidad, String idCliente) {
        this.codigo = UUID.randomUUID();
        this.fechaHora = LocalDateTime.now();
        this.origen = origen;
        this.destino = destino;
        this.cantidad = cantidad;
        this.idCliente = idCliente;
    }

    // Getters y Setters
    public UUID getCodigo() {
        return codigo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Check if both are the same object
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Check if obj is null or a different class
        }
        Venta other = (Venta) obj;
        return Objects.equals(codigo, other.codigo);
    }

    @Override
    public int hashCode() {
        if (codigo == null) {
            return 0;
        }
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        return "Venta: {" +
                "fechaHora=" + fechaHora +
                " Departamento= " + destino.getDepartamento() +
                ", Provincia= " + destino.getProvincia() +
                ", ubigeoDestino='" + destino.getCodigo() + '\'' +
                ", cantidad=" + cantidad +
//                ", idCliente='" + idCliente + '\'' +
                "Region Destino= " + destino.getRegion() +
                '}';
    }

    @Override
    public Venta clone() {
        try {
            return (Venta) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar la venta", e);
        }
    }
}
