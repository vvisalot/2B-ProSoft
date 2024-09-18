package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SimulatedAnnealing {

    private double initialTemperature;  // Temperatura inicial
    private double coolingRate;         // Tasa de enfriamiento
    private double[][] distanceMatrix;  // Matriz de distancias entre ciudades

    public SimulatedAnnealing(double[][] distanceMatrix, double initialTemperature, double coolingRate) {
        this.distanceMatrix = distanceMatrix;
        this.initialTemperature = initialTemperature;
        this.coolingRate = coolingRate;
    }

    // Calcula el costo (distancia total) de un tour
    private double calculateCost(ArrayList<Integer> tour) {
        double cost = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            cost += distanceMatrix[tour.get(i)][tour.get(i + 1)];
        }
        cost += distanceMatrix[tour.get(tour.size() - 1)][tour.get(0)]; // Volver al punto de partida
        return cost;
    }

    // Genera una nueva solución (vecino) intercambiando dos ciudades al azar
    private ArrayList<Integer> getNeighbour(ArrayList<Integer> currentSolution) {
        ArrayList<Integer> newSolution = new ArrayList<>(currentSolution);
        Random rand = new Random();
        int idx1 = rand.nextInt(newSolution.size());
        int idx2 = rand.nextInt(newSolution.size());
        Collections.swap(newSolution, idx1, idx2); // Intercambia dos ciudades
        return newSolution;
    }

    // Ejecuta el algoritmo de Simulated Annealing
    public ArrayList<Integer> run(ArrayList<Integer> initialSolution) {
        ArrayList<Integer> currentSolution = new ArrayList<>(initialSolution);
        ArrayList<Integer> bestSolution = new ArrayList<>(initialSolution);
        double bestCost = calculateCost(currentSolution);
        double temperature = initialTemperature;

        System.out.println("Temperatura inicial: " + temperature);
        System.out.println("Solución inicial: " + currentSolution + " | Costo: " + bestCost);

        while (temperature > 1) {
            ArrayList<Integer> newSolution = getNeighbour(currentSolution);
            double currentCost = calculateCost(currentSolution);
            double newCost = calculateCost(newSolution);

            // Decidir si se acepta la nueva solución
            if (acceptanceProbability(currentCost, newCost, temperature) > Math.random()) {
                currentSolution = newSolution;
            }

            // Actualizar la mejor solución encontrada
            if (calculateCost(currentSolution) < bestCost) {
                bestSolution = new ArrayList<>(currentSolution);
                bestCost = calculateCost(bestSolution);
                System.out.println("Nueva mejor solución: " + bestSolution + " | Costo: " + bestCost);
            }

            // Enfriar el sistema
            temperature *= (1 - coolingRate);
        }

        System.out.println("Temperatura final: " + temperature);
        System.out.println("Mejor solución encontrada: " + bestSolution + " | Costo: " + bestCost);
        return bestSolution;
    }

    // Calcula la probabilidad de aceptar una solución peor
    private double acceptanceProbability(double currentCost, double newCost, double temperature) {
        if (newCost < currentCost) {
            return 1.0; // Si la nueva solución es mejor, siempre se acepta
        }
        return Math.exp((currentCost - newCost) / temperature); // Probabilidad basada en la temperatura
    }
}