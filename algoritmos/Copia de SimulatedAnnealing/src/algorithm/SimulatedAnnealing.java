package algorithm;

import dto.Solucion;
import model.*;
import utils.RelojSimulado;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatedAnnealing {
    private static final MapaVelocidad mapaVelocidad = MapaVelocidad.getInstance();
    private static final double TIEMPO_DESCARGA = 1;
    private static final double TIEMPO_EN_OFICINA = 2;
    private final GrafoTramos grafoTramos = GrafoTramos.getInstance();

    public static Solucion calcular(List<Paquete> paquetes, Camion camion, RelojSimulado reloj,
                                  List<Oficina> almacenesPrincipales) {
        RutaManager.limpiarPaquetes();
        for (var paquete : paquetes) {
            RutaManager.agregarPaquete(paquete);
        }

        double temp = 100000;
        double coolingRate = 0.003;
        Ruta currentSolution;
        Ruta best;
        double time;
        do {
            currentSolution = new Ruta(camion.getPosicionFinal());
            currentSolution.generateIndividual();
//        System.out.println("Total time of initial solution: " + currentSolution.getTiempoTotal());
 //       System.out.println("Ruta: " + currentSolution);

            best = new Ruta(currentSolution.getPaquetesEntregados(), camion.getPosicionFinal());
            time = best.getTiempoTotal();
        } while (esRutaInvalida(best, time));

        double bestTime = time;
        while (temp > 1) {
            // Crea una nueva solución vecina
            Ruta newSolution = new Ruta(currentSolution.getPaquetesEntregados(), camion.getPosicionFinal());
            if (newSolution.cantidadPaquetes() <= 2) {
                break;
            }
            // Escoge dos posiciones aleatorias en la ruta, excluyendo la posicion inicial del camion
            int rutaPos1 = randomInt(1, newSolution.cantidadPaquetes());
            int rutaPos2 = randomInt(1, newSolution.cantidadPaquetes());

            // Nos aseguramos de que las posiciones sean diferentes
            while (rutaPos1 == rutaPos2) {
                rutaPos2 = randomInt(1, newSolution.cantidadPaquetes());
            }

            // Consigue los paquetes en las posiciones seleccionadas
            Paquete paqueteACambiar1 = newSolution.getPaquete(rutaPos1);
            Paquete paqueteACambiar2 = newSolution.getPaquete(rutaPos2);

            // Intercambia los paquetes
            newSolution.setPaquete(rutaPos2, paqueteACambiar1);
            newSolution.setPaquete(rutaPos1, paqueteACambiar2);

            // Consigue la energía de las soluciones actuales y nuevas
            double currentTime = currentSolution.getTiempoTotal();
            double neighbourTime = newSolution.getTiempoTotal();

            if (esRutaInvalida(newSolution, neighbourTime)) {
                temp *= 1 - coolingRate;
                continue;
            }
            // Decidimos si aceptamos la nueva solución
            double rand = randomDouble();
            if (acceptanceProbability(currentTime, neighbourTime, temp) > rand) {
                currentSolution = new Ruta(newSolution.getPaquetesEntregados(), camion.getPosicionFinal());
            }

            //  Guardamos la mejor solución
            if (currentSolution.getTiempoTotal() < best.getTiempoTotal()) {
                best = new Ruta(currentSolution.getPaquetesEntregados(), camion.getPosicionFinal());
                bestTime = currentSolution.getTiempoTotal();
            }
            temp *= 1 - coolingRate;
        }
        best.construirRutaMarcada();
        var posicionFinal = best.getPaquetesEntregados().get(best.getPaquetesEntregados().size() - 1).getVenta().getDestino();
        camion.setPosicionFinal(posicionFinal);

        //AGREGAR TIEMPO DE LLEGADA A ULTIMO PUNTO
        double hoursToAdd = bestTime;
        long wholeHours = (long) hoursToAdd;
        long minutes = (long) ((hoursToAdd - wholeHours) * 60);
        var fechaEntregaUltimoPaquete = reloj.getTiempo().plusHours(wholeHours).plusMinutes(minutes);
        camion.setFechaDeLlegadaPosicionFinal(fechaEntregaUltimoPaquete);
        camion.setEnRuta(true);

        //Escoger almacén de retorno
        var mejorTiempo = Double.MAX_VALUE;
        Oficina almacenRegreso = null;
        Ruta rutaRegreso = new Ruta();
        for (Oficina almacen : almacenesPrincipales) {
            rutaRegreso = new Ruta(posicionFinal, almacen);
            var tiempoRegreso = rutaRegreso.getTiempoRegreso();
            if (tiempoRegreso < mejorTiempo) {
                mejorTiempo = tiempoRegreso;
                almacenRegreso = almacen;
            }
        }
        System.out.println(rutaRegreso);



        camion.setAlmacenCarga(almacenRegreso);
        var paquetesAEntregar = new ArrayList<dto.Paquete>();

        for(int i = 1; i < best.getPaquetesEntregados().size();i++){
            var paquete = best.getPaquetesEntregados().get(i);
            var destino = paquete.getVenta().getDestino();
            paquetesAEntregar.add(new dto.Paquete(
                    paquete.getVenta().getCodigo(),
                    paquete.getVenta().getFechaHora(),
                    new dto.Oficina(destino.getLatitud(),destino.getLongitud()),
                    paquete.getCantidad(),
                    paquete.getVenta().getCantidad(),
                    paquete.getVenta().getIdCliente()
            ));
        }
        var camionSolucion = new dto.Camion(camion.getCodigo(),camion.getTipo(),camion.getCapacidad(),camion.getCargaActual(),paquetesAEntregar);
        var tramosSolucion = new ArrayList<dto.Tramo>();
        LocalDateTime tiempoActual = reloj.getTiempo();
        for (int i=0;  i< best.getRutaRecorrida().size(); i++) {
            var tramo = best.getRutaRecorrida().get(i);
            var origen = tramo.getOrigen();
            var destino = tramo.getDestino();
            var velocidad = mapaVelocidad.obtenerVelocidad(origen.getRegion(), destino.getRegion());
            tramosSolucion.add(new dto.Tramo(
                    new dto.Oficina(origen.getLatitud(),origen.getLongitud()),
                    new dto.Oficina(destino.getLatitud(),destino.getLongitud()),
                    tramo.getDistancia(),
                    velocidad,
                    tiempoActual,
                    tiempoActual = tiempoActual.plusMinutes((long) (tramo.getDistancia() * 60 / velocidad)),
                    i != best.getRutaRecorrida().size() -1 ? (int) TIEMPO_EN_OFICINA: (int) TIEMPO_DESCARGA,
                    tramo.getEsFinal()
            ));
            tiempoActual = tiempoActual.plusHours((long) TIEMPO_EN_OFICINA);
        }
        best.desmarcarTramos();
        for (int i=0;  i< rutaRegreso.getRutaRecorrida().size(); i++) {
            var tramo = rutaRegreso.getRutaRecorrida().get(i);
            var origen = tramo.getOrigen();
            var destino = tramo.getDestino();
            var velocidad = mapaVelocidad.obtenerVelocidad(origen.getRegion(), destino.getRegion());
            tramosSolucion.add(new dto.Tramo(
                    new dto.Oficina(origen.getLatitud(),origen.getLongitud()),
                    new dto.Oficina(destino.getLatitud(),destino.getLongitud()),
                    tramo.getDistancia(),
                    velocidad,
                    tiempoActual,
                    tiempoActual = tiempoActual.plusMinutes((long) (tramo.getDistancia() * 60.0 / (double) velocidad)),
                    (int) TIEMPO_EN_OFICINA,
                    tramo.getEsFinal()
            ));
            tiempoActual = tiempoActual.plusHours((long) TIEMPO_EN_OFICINA);
        }

        hoursToAdd = bestTime + mejorTiempo;
        wholeHours = (long) hoursToAdd;
        minutes = (long) ((hoursToAdd - wholeHours) * 60);
        camion.setRegresoAlmacen(reloj.getTiempo().plusHours(wholeHours).plusMinutes(minutes));

        var solucion = new Solucion(
                camionSolucion,
                tramosSolucion,
                hoursToAdd
        );


//        System.out.println("Tiempo Final solución: " + best.getTiempoTotal());
        System.out.println("Ruta: " + best);
        return solucion;
    }

    public static double acceptanceProbability(double currentTime, double newTime, double temperature) {
        if (newTime < currentTime) {
            return 1.0;
        }
        return Math.exp((currentTime - newTime) / temperature);
    }

    public static double randomDouble() {
        Random r = new Random();
        return r.nextInt(1000) / 1000.0;
    }

    public static int randomInt(int min, int max) {
        Random r = new Random();
        double d = min + r.nextDouble() * (max - min);
        return (int) d;
    }

    private static boolean esRutaInvalida(Ruta ruta, double tiempo) {
        if (tiempo <= 24.0) {
            return false;
        } else if (tiempo <= 48.0) {
            ruta.construirRutaMarcada();
            var rutaEvaluar = ruta.getRutaRecorrida();
            var tiempoEnRuta = 0.0;
            for (int i = 0; i< rutaEvaluar.size(); i++) {
                var tramo = rutaEvaluar.get(i);
                var velocidad = mapaVelocidad.obtenerVelocidad(tramo.getOrigen().getRegion(), tramo.getDestino().getRegion());
                tiempoEnRuta += tramo.getDistancia() / velocidad;
                tiempoEnRuta += i != rutaEvaluar.size() -1 ? TIEMPO_EN_OFICINA: TIEMPO_DESCARGA;
                if (tramo.getEsFinal() && tramo.getDestino().getRegion().equals("COSTA") && tiempoEnRuta >= 24.0) {
                    ruta.desmarcarTramos();
                    return true;
                }
            }
            ruta.desmarcarTramos();
            return false;
        } else if (tiempo <= 72.0) {
            ruta.construirRutaMarcada();
            var rutaEvaluar = ruta.getRutaRecorrida();
            var tiempoEnRuta = 0.0;
            for (int i = 0; i< rutaEvaluar.size(); i++) {
                var tramo = rutaEvaluar.get(i);
                var velocidad = mapaVelocidad.obtenerVelocidad(tramo.getOrigen().getRegion(), tramo.getDestino().getRegion());
                tiempoEnRuta += tramo.getDistancia() / velocidad;
                tiempoEnRuta += i != rutaEvaluar.size() -1 ? TIEMPO_EN_OFICINA: TIEMPO_DESCARGA;
                if (tramo.getEsFinal()) {
                    if (tramo.getDestino().getRegion().equals("COSTA") && tiempoEnRuta >= 24.0) {
                        ruta.desmarcarTramos();
                        return true;
                    } else if (tramo.getDestino().getRegion().equals("SIERRA") && tiempoEnRuta >= 48.0) {
                        ruta.desmarcarTramos();
                        return true;
                    }
                }
            }
            ruta.desmarcarTramos();
            return false;
        }
        ruta.desmarcarTramos();
        return true;
    }
}