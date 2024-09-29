package algorithm;

import model.Camion;
import model.Paquete;
import model.Ruta;
import model.RutaManager;

import java.util.List;
import java.util.Random;

public class SimulatedAnnealing {
    public static void calcular(List<Paquete> paquetes, Camion camion) {
        RutaManager.limpiarPaquetes();
        for (var paquete : paquetes) {
            RutaManager.agregarPaquete(paquete);
        }

        double temp = 100000;
        double coolingRate = 0.003;
        Ruta currentSolution = new Ruta(camion.getPosicionActual());
        currentSolution.generateIndividual();
        System.out.println("Total time of initial solution: " + currentSolution.getTiempoTotal());
        System.out.println("Ruta: " + currentSolution);

        Ruta best = new Ruta(currentSolution.getPaquetesEntregados(), camion.getPosicionActual());
        while (temp > 1) {
            // Crea una nueva solución vecina
            Ruta newSolution = new Ruta(currentSolution.getPaquetesEntregados(), camion.getPosicionActual());
            if(newSolution.cantidadPaquetes() == 1){
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
            double currentDistance = currentSolution.getTiempoTotal();
            double neighbourDistance = newSolution.getTiempoTotal();

            // Decidimos si aceptamos la nueva solución
            double rand = randomDouble();
            if (acceptanceProbability(currentDistance, neighbourDistance, temp) > rand) {
                currentSolution = new Ruta(newSolution.getPaquetesEntregados(), camion.getPosicionActual());
            }

            //  Guardamos la mejor solución
            if (currentSolution.getTiempoTotal() < best.getTiempoTotal()) {
                best = new Ruta(currentSolution.getPaquetesEntregados(), camion.getPosicionActual());
            }

            // Enfriamos el sistema
            temp *= 1 - coolingRate;
        }

        System.out.println("Tiempo Final solución: " + best.getTiempoTotal());
        System.out.println("Ruta: " + best);
        System.out.println("\n");
    }

    public static double acceptanceProbability(double currentDistance, double newDistance, double temperature) {
        if (newDistance < currentDistance) {
            return 1.0;
        }
        return Math.exp((currentDistance - newDistance) / temperature);
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
}