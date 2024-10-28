package grupo2b.odiroute.model;

import grupo2b.odiroute.algorithm.GrafoTramos;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Ruta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Paquete> paquetesEntregados = new ArrayList<>();
    
    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tramo> rutaRecorrida = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "oficina_origen_id", nullable = false)
    private Oficina oficinaOrigen;
    
    @ManyToOne
    @JoinColumn(name = "oficina_destino_id", nullable = false)
    private Oficina oficinaDestino;
    
    @Column(name = "tiempo")
    private double tiempo = 0;
    
    @Transient
    private final GrafoTramos grafoTramos = GrafoTramos.getInstance();
    
    @Transient
    private final MapaVelocidad mapaVelocidad = MapaVelocidad.getInstance();
    
    @ManyToOne
    @JoinColumn(name = "punto_partida_id", nullable = false)
    private Oficina puntoPartida;
    
    @Transient
    private final double TIEMPO_DESCARGA = 1;

    public Ruta() {}
    
    public Ruta(Oficina puntoPartida) {
        for (int i = 0; i < RutaManager.cantidadPaquetes(); i++) {
            paquetesEntregados.add(null);
        }
        this.puntoPartida = puntoPartida;
    }

    public Ruta(List<Tramo> tramos) {
        this.rutaRecorrida = tramos;
    }

    public Ruta(Oficina puntoInicial, Oficina almacenRetorno){
        var paqueteVacioInicial = new Paquete(new Venta(puntoInicial),0);
        var paqueteVacioAlmacenRetorno = new Paquete(new Venta(almacenRetorno),0);
        this.paquetesEntregados.add(paqueteVacioInicial);
        this.paquetesEntregados.add(paqueteVacioAlmacenRetorno);
    }

    @SuppressWarnings("unchecked")
    public Ruta(ArrayList<Paquete> paquetes, Oficina puntoPartida) {
        this.paquetesEntregados = (ArrayList<Paquete>) paquetes.clone();
        this.puntoPartida = puntoPartida;
    }

    public ArrayList<Paquete> getPaquetesEntregados(){
        return (ArrayList<Paquete>) paquetesEntregados;
    }

    // Creamos una solución vecina intercambiando dos paquetes
    public void generateIndividual() {
        for (int paqueteIndex = 0; paqueteIndex < RutaManager.cantidadPaquetes(); paqueteIndex++) {
            setPaquete(paqueteIndex, RutaManager.obtenerPaquete(paqueteIndex));
        }
        Collections.shuffle(paquetesEntregados);
        var ventaVacia = new Venta();
        ventaVacia.setDestino(puntoPartida);
        var puntoInicial = new Paquete(ventaVacia,0);
        paquetesEntregados.add(0,puntoInicial);
        RutaManager.agregarPuntoInicial(puntoInicial);
    }

    public Paquete getPaquete(int index) {
        return paquetesEntregados.get(index);
    }

    public void setPaquete(int index, Paquete paquete) {
        paquetesEntregados.set(index, paquete);
        // If the tour has been altered we need to reset the fitness and distance
        tiempo = 0.0;
    }

    public double getTiempoTotal(){
        if (tiempo == 0.0) {
            double tiempoRuta = 0.0;
            // Loop through our tour's cities
            for (int paqueteIndex=0; paqueteIndex < cantidadPaquetes() && cantidadPaquetes()>1; paqueteIndex++) {
                // Get city we're traveling from
                Paquete paqueteActual = getPaquete(paqueteIndex);
                // City we're traveling to
                Paquete paqueteSiguiente;
                // Check we're not on our tour's last city, if we are set our
                // tour's final destination city to our starting city
                if(paqueteIndex+1 < cantidadPaquetes()){
                    paqueteSiguiente = getPaquete(paqueteIndex+1);
                }
                else{
                    paqueteSiguiente = getPaquete(0);
                }
                // Get the distance between the two cities

                tiempoRuta += calcularTiempoRuta(paqueteActual, paqueteSiguiente);
                tiempoRuta += TIEMPO_DESCARGA;
            }
            tiempo = tiempoRuta;
        }
        return tiempo;
    }

    public int cantidadPaquetes() {
        return paquetesEntregados.size();
    }

    @Override
    public String toString() {
        construirRuta();
        if (rutaRecorrida.isEmpty()) {
            return "No hay ruta para este camión";
        }
        StringBuilder s = new StringBuilder(rutaRecorrida.get(0).toString());
        for (int i = 1; i < rutaRecorrida.size(); i++) {
            s.append(" | ").append(rutaRecorrida.get(i).toString());
        }
        return s.toString();
    }

    public double calcularTiempoRuta(Paquete paqueteActual, Paquete paqueteSiguiente) {
        var oficinaActual = paqueteActual.getVenta().getDestino();
        var oficinaSiguiente = paqueteSiguiente.getVenta().getDestino();
        var mejorRuta = grafoTramos.obtenerRutaMasCorta(oficinaActual, oficinaSiguiente);
        return calcularTiempoRuta(mejorRuta);
    }

    public double calcularTiempoRuta() {
        double tiempoTotal = 0.0;
        for (Tramo tramo : rutaRecorrida) {
            var velocidad = mapaVelocidad.obtenerVelocidad(tramo.getOrigen().getRegion(), tramo.getDestino().getRegion());
            tiempoTotal += tramo.getDistancia() / velocidad;
        }
        return tiempoTotal;
    }

    public double calcularTiempoRuta(List<Tramo> ruta) {
        double tiempoTotal = 0.0;
        for (Tramo tramo : ruta) {
            var velocidad = mapaVelocidad.obtenerVelocidad(tramo.getOrigen().getRegion(), tramo.getDestino().getRegion());
            tiempoTotal += tramo.getDistancia() / velocidad;
        }
        return tiempoTotal;
    }

    public void construirRuta(){
        List<Tramo> ruta = new ArrayList<>();
        for (int i = 0; i < paquetesEntregados.size() - 1; i++) {
            var oficinaActual = paquetesEntregados.get(i).getVenta().getDestino();
            var oficinaSiguiente = paquetesEntregados.get(i + 1).getVenta().getDestino();
            var mejorRuta = grafoTramos.obtenerRutaMasCorta(oficinaActual, oficinaSiguiente);
            ruta.addAll(mejorRuta);
        }
        this.rutaRecorrida = ruta;
    }

    public void construirRutaMarcada(){
        List<Tramo> ruta = new ArrayList<>();
        for (int i = 0; i < paquetesEntregados.size() - 1; i++) {
            var oficinaActual = paquetesEntregados.get(i).getVenta().getDestino();
            var oficinaSiguiente = paquetesEntregados.get(i + 1).getVenta().getDestino();
            var mejorRuta = grafoTramos.obtenerRutaMasCorta(oficinaActual, oficinaSiguiente);
            mejorRuta.get(mejorRuta.size() - 1).setEsFinal(true);
            ruta.addAll(mejorRuta);
        }
        this.rutaRecorrida = ruta;
    }
    //TODO: Desmarcar los tramos como finales

    public List<Tramo> getRutaRecorrida(){
        return this.rutaRecorrida;
    }
}
