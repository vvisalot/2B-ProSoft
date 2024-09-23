package main;

import Clases.Bloqueo;
import Clases.Mantenimiento;
import Clases.Oficina;
import Clases.Tramo;
import Clases.Velocidad;
import Clases.Venta;
import Utils.LeerDatos;
import java.io.IOException;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        try {
            // Leer todas las ventas en la carpeta "ventas.historico.proyectado"
            String carpetaVentas = "archivos/ventas.historico.proyectado";
            List<Venta> ventas = LeerDatos.leerVentasEnCarpeta(carpetaVentas);

            // Leer todos los bloqueos en la carpeta "bloqueos"
            String carpetaBloqueos = "archivos/bloqueos";
            List<Bloqueo> bloqueos = LeerDatos.leerBloqueosEnCarpeta(carpetaBloqueos);
            
            //Leer oficinas
            List<Oficina> oficinas = LeerDatos.leerOficinas("archivos/oficinas.txt");
            //int i=1;
            // for(Oficina of: oficinas){
            //     System.out.println(i+") Ubigeo:"+of.getUbigeo()+"  Dep: "+of.getDepartamento()+"   Prov: "+of.getProvincia()+"   Capac:"+of.getAlmacen());
            //     i++;
            // }
            
            //Leer Tramos
            List<Tramo> tramos = LeerDatos.leerTramos("archivos/tramos.txt");
            int i=1;
            for(Tramo tram: tramos){
                System.out.println(i+") Ubigeo Origen:"+tram.getUbigeoOrigen()+"  Ubigeo Destino: "+tram.getUbigeoDestino()+"   Distancia: "+tram.getDistanciaTramo()+"   Velocidad:"+tram.getVelocidadTramo());
                i++;
                if(i==20) break;
            }
            
            //Leer Velocidades
            List<Velocidad> velocidades = LeerDatos.leerVelocidades("archivos/velocidades.txt");
            
            //Leer Mantenimientos
            List<Mantenimiento> mantenimientos = LeerDatos.leerMantenimientos("archivos/mantenimiento_trim.txt");
            

            
        } catch (IOException e) {
            System.out.println("Error al leer los archivos: " + e.getMessage());
        }
    }
}

/*

 
import Clases.Camion;
import Clases.Pedido;
import Clases.Posicion;
import Clases.TanqueCisterna;
import Clases.LectorDatos;
import Clases.Cromosoma;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class algoritmoGenetico {
    

    public static void main(String[] args) throws IOException {
        List<Camion> camiones = new ArrayList<>();
        List<Pedido> pedidos = new ArrayList<>();
        
        Posicion ubicacionAlmacen = new Posicion(12, 8);
        Posicion ubicacionTanque1 = new Posicion(42, 42);
        Posicion ubicacionTanque2 = new Posicion(63, 3);
        
        TanqueCisterna Almacen = new TanqueCisterna(ubicacionAlmacen, 9999999, 9999999);
        TanqueCisterna tanqueNorte = new TanqueCisterna(ubicacionTanque1, 160, 160);
        TanqueCisterna tanqueEste = new TanqueCisterna(ubicacionTanque2, 160, 160);
        
        String archivo1 = "camiones.txt";
        String archivo2 = "ventas202407.txt";
        
        LectorDatos lector1 = new LectorDatos();
        LectorDatos lector2 = new LectorDatos();
        
        camiones = lector1.leerCamionesDesdeArchivo(archivo1, ubicacionAlmacen);
        pedidos = lector2.leerPedidosDesdeArchivo(archivo2);
        
        int tamanioPoblacion = 50;
        double tasaMutacion = 0.2;
        
        double mejorAptitud = Double.POSITIVE_INFINITY;
        List<Cromosoma> mejorSolucion = null;        
        int generaciones = 1000;
        List<List<Cromosoma>> poblacionInicial = inicializarPoblacion(tamanioPoblacion, pedidos, camiones);

        
                
        for(int generacion=0; generacion<generaciones; generacion++){
            List<Double> puntajes = new ArrayList<>();

            for (List<Cromosoma> cromosoma : poblacionInicial) {
                double puntaje = calcularAptitud(cromosoma, camiones, pedidos); 
                puntajes.add(puntaje);
            }


            List<List<Cromosoma>> padres = seleccionarPadres(poblacionInicial);
            List<Cromosoma> hijo = cruzar(padres);
            hijo = mutar(hijo, tasaMutacion);

            Random random = new Random();
            int indiceAReemplazar = random.nextInt(poblacionInicial.size());
            poblacionInicial.set(indiceAReemplazar, hijo);


            double aptitudActual = 0;
            for (List<Cromosoma> cromosoma : poblacionInicial) {
                double puntaje = calcularAptitud(cromosoma, camiones, pedidos); 
                aptitudActual += puntaje;
            }

            if (aptitudActual < mejorAptitud) {
                mejorAptitud = aptitudActual;
                mejorSolucion = poblacionInicial.get(indiceAReemplazar);
            }
            System.out.println("Generación " + generacion + ": Mejor aptitud = " + mejorAptitud);
        }
        
        for (Cromosoma cromosoma : mejorSolucion){
            int indiceCamion = cromosoma.getIndiceCamion();
            int ruta[] = cromosoma.getRutaIndices();
            System.out.println("Camion: " + indiceCamion);
            for(int i=0; i<ruta.length; i++){
                System.out.println("ID Pedido: " + ruta[i]);
            }
        }
    }
    
    static List<List<Cromosoma>> inicializarPoblacion(int tamanoPoblacion, List<Pedido> pedidos, List<Camion> camiones) {
        List<List<Cromosoma>> poblacion = new ArrayList<>();

        for (int i = 0; i < tamanoPoblacion; i++) {
            List<Cromosoma> cromosoma = new ArrayList<>();
            for (int j = 0; j < camiones.size(); j++) {
                int[] rutaIndices = new int[pedidos.size()];
                for (int k = 0; k < rutaIndices.length; k++) {
                    rutaIndices[k] = k; 
                }
                mezclar(rutaIndices);
                cromosoma.add(new Cromosoma(j, rutaIndices));  
            }
            poblacion.add(cromosoma);
        }

        return poblacion;
    }
         
    
    static double calcularAptitud(List<Cromosoma> cromosoma, List<Camion> c, List<Pedido> p) {        
        List<Camion> camiones = c;
        List<Pedido> pedidos = p;        
        double tiempoRecorrido;
        int numFallas = calculaNumFallas(cromosoma, camiones, pedidos);
        //Agregar variable de cisternas intermedios
        //Agregar variable de peso total usado
        double distanciaRecorrida = calcularConsumoPetróleo(cromosoma, camiones, pedidos);                        
        return distanciaRecorrida;
    }
    
    static void mezclar(int[] array) {
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
    
    static List<List<Cromosoma>> seleccionarPadres(List<List<Cromosoma>> poblacion) {
        List<List<Cromosoma>> padres = new ArrayList<>();
        Random random = new Random();
        List<Cromosoma> padre1 = poblacion.get(random.nextInt(poblacion.size()));
        List<Cromosoma> padre2 = poblacion.get(random.nextInt(poblacion.size()));
        padres.add(padre1);
        padres.add(padre2);
        return padres;
    }
    
    static List<Cromosoma> cruzar(List<List<Cromosoma>> padres) {
        List<Cromosoma> padre1 = padres.get(0);
        List<Cromosoma> padre2 = padres.get(1);
        
        int longitudPadres = padre1.size();        
        Random random = new Random();
        int puntoDeCorte = random.nextInt(longitudPadres - 1) + 1;

        
        List<Cromosoma> hijo = new ArrayList<>();
        hijo.addAll(padre1.subList(0, puntoDeCorte));
        hijo.addAll(padre2.subList(puntoDeCorte, longitudPadres));

        return hijo;
    }
    
    static List<Cromosoma> mutar(List<Cromosoma> hijo, double tasaMutacion) {
    
        List<Cromosoma> hijoMutado = new ArrayList<>(hijo);
        Random random = new Random();
        for (Cromosoma camion : hijoMutado) {            
            if (random.nextDouble() < tasaMutacion) {            
                int[] ruta = camion.getRutaIndices();
                mezclar(ruta);
                camion.setRutaIndices(ruta);
            }
        }

        return hijoMutado;
    }
    
    static int calculaNumFallas(List<Cromosoma> cr, List<Camion> camiones, List<Pedido> pedidos){
        int numFallas = 0;
        for (Cromosoma cromosoma : cr){
            int horaActual = 0;
            int indiceCamion = cromosoma.getIndiceCamion();
            int[] ruta = cromosoma.getRutaIndices();
            
            Camion cisterna = camiones.get(indiceCamion);            
            Posicion posicionAnterior = cisterna.getPosicion();
            
            for (int indicePedido : ruta){
                Pedido pedido = pedidos.get(indicePedido);
                int horaRecibo = pedido.getHoraRecibida();
                int horaLimite = horaRecibo + (60*pedido.getHLimite());
                Posicion posPedido = pedido.getPos();
                double distancia = calcularDistancia(posicionAnterior, posPedido);
                int tiempoEntrega = (int)distancia * 72; 
                if(horaActual<horaRecibo || horaActual>horaLimite || (horaActual + tiempoEntrega)>horaLimite){
                    numFallas++;
                }else{
                    horaActual = horaActual + tiempoEntrega;
                    cisterna.setPosicion(posPedido);
                    posicionAnterior = posPedido;
                }                
            }
        }
        return numFallas;
    }
    
    
    
    static double calcularConsumoPetróleo(List<Cromosoma> cromosoma, List<Camion> camiones, List<Pedido> pedidos) {
        double consumoTotal = 0.0;
        
        for (Cromosoma camion : cromosoma) {
            int indiceCamion = camion.getIndiceCamion();
            int[] ruta = camion.getRutaIndices();      
            
            Camion cisterna = camiones.get(indiceCamion);            
            Posicion posicionAnterior = cisterna.getPosicion();
            
            for (int indice : ruta) {
                Pedido pedido = pedidos.get(indice);
                Posicion posPedido = pedido.getPos();
                double distancia = calcularDistancia(posicionAnterior, posPedido);
                cisterna.setPosicion(posPedido);
                posicionAnterior = posPedido;
                consumoTotal += distancia;
            }                            
        }
        return consumoTotal;
    }
    
    
    
    public static double calcularDistancia(Posicion punto1, Posicion punto2) {
        int x1 = punto1.getCoordenadaX();
        int y1 = punto1.getCoordenadaY();
        int x2 = punto2.getCoordenadaX();
        int y2 = punto2.getCoordenadaY();
        
        int distancia = Math.abs(x1 - x2) + Math.abs(y1 - y2);
        return distancia;
    }   
}

*/


/*
           
            Imprimir datos
            
            System.out.println("Ventas cargadas: " + ventas.size());
            for (Venta venta : ventas) {
                System.out.println(venta);
            }

            System.out.println("Bloqueos cargados: " + bloqueos.size());
            for (Bloqueo bloqueo : bloqueos) {
                System.out.println(bloqueo);
            }
            
            
            System.out.println("Oficinas cargados: " + oficinas.size());
            for (Oficina oficina : oficinas) {
                System.out.println(oficina);
            }
            
            System.out.println("Tramos cargados: " + tramos.size());
            for (Tramo tramo : tramos) {
                System.out.println(tramo);
            }
            */
            /*
            System.out.println("Velocidades cargadas: " + velocidades.size());
            for (Velocidad velocidad : velocidades) {
                System.out.println(velocidad);
            }
            
            System.out.println("Mantenimientos cargados: " + mantenimientos.size());
            for (Mantenimiento mantenimiento : mantenimientos) {
                System.out.println(mantenimiento);
            }
 */