package model;

import algorithm.GrafoTramos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Ruta {

    private List<Paquete> paquetesEntregados = new ArrayList<>();
    private List<Tramo> rutaRecorrida = new ArrayList<>();
    private double tiempo = 0;
    private final GrafoTramos grafoTramos = GrafoTramos.getInstance();
    private final MapaVelocidad mapaVelocidad = MapaVelocidad.getInstance();
    private Oficina puntoPartida;

    public Ruta(Oficina puntoPartida) {
        for (int i = 0; i < RutaManager.cantidadPaquetes(); i++) {
            paquetesEntregados.add(null);
        }
        this.puntoPartida = puntoPartida;
    }

    public Ruta(List<Tramo> tramos) {
        this.rutaRecorrida = tramos;
    }


    @SuppressWarnings("unchecked")
    public Ruta(ArrayList<Paquete> paquetes, Oficina puntoPartida) {
        this.paquetesEntregados = (ArrayList<Paquete>) paquetes.clone();
        this.puntoPartida = puntoPartida;
    }

    public ArrayList<Paquete> getPaquetesEntregados(){
        return (ArrayList<Paquete>) paquetesEntregados;
    }

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

    /**
     * Computes and returns the total distance of the tour
     * @return distance total distance of the tour
     */
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
            }
            tiempo = tiempoRuta;
        }
        return tiempo;
    }

    /**
     * Get number of cities on our tour
     * @return number how many cities there are in the tour!
     */
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
}
