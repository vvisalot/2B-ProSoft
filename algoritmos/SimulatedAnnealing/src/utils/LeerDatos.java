package utils;

import model.Oficina;
import model.Tramo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LeerDatos {
    private static final Logger logger = Logger.getLogger(LeerDatos.class.getName());

    // Método para leer las oficinas desde un archivo y almacenarlas en un HashMap
    public static Map<String, Oficina> leerOficinasDesdeArchivo(String filePath) {
        Map<String, Oficina> mapaOficinas = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(","); // Asume que los datos están separados por comas

                // Asignar los valores a los atributos correspondientes
                String codigo = datos[0];  // UBIGEO
                Oficina oficina = getOficina(datos, codigo);

                // Agregar la oficina al mapa usando el código (ubigeo) como clave
                mapaOficinas.put(codigo, oficina);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mapaOficinas;
    }

    private static Oficina getOficina(String[] datos, String codigo) {
        String departamento = datos[1];  // DEP
        String provincia = datos[2];  // PROV
        double latitud = Double.parseDouble(datos[3]);  // LATITUD
        double longitud = Double.parseDouble(datos[4]);  // LONGITUD
        String regionNatural = datos[5];  // REGION.NATURAL
        int capacidad = Integer.parseInt(datos[6]);  // ALMACEN

        // Crear una nueva instancia de Oficina
        Oficina oficina = new Oficina(codigo, departamento, provincia, latitud, longitud, regionNatural, capacidad);
        return oficina;
    }

    public static List<Tramo> leerTramosDesdeArchivo(String filePath, Map<String, Oficina> mapaOficinas) {
        List<Tramo> tramos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(" => ");  // Asumimos que están separados por comas

                // Extraemos los valores de ubigeo para origen y destino
                String ubigeoOrigen = datos[0].trim();
                String ubigeoDestino = datos[1].trim();

                // Buscamos las oficinas correspondientes en el mapa
                Oficina oficinaOrigen = mapaOficinas.get(ubigeoOrigen);
                Oficina oficinaDestino = mapaOficinas.get(ubigeoDestino);

                if (oficinaOrigen != null && oficinaDestino != null) {
                    Tramo tramo = new Tramo(oficinaOrigen, oficinaDestino);
                    tramos.add(tramo);
                } else {
                    System.err.println("Oficina no encontrada para ubigeo: " + ubigeoOrigen + " o " + ubigeoDestino);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tramos;
    }


//    public static List<Bloqueo> leerBloqueosEnCarpeta(String carpetaBloqueos, Map<String, Tramo> tramosExistentes) throws IOException {
//        List<Bloqueo> bloqueos = new ArrayList<>();
//
//        // Obtenemos todos los archivos en la carpeta de bloqueos con extensión .txt
//        DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(carpetaBloqueos), "*.txt");
//
//        for (Path archivo : stream) {
//            String nombreArchivo = archivo.getFileName().toString();
//
//            // Validar que el nombre del archivo sigue el formato "bloqueoNN.txt"
//            if (nombreArchivo.matches("bloqueo\\d{2}\\.txt")) {
//                // Leer los bloqueos en el archivo actual y agregar al total
//                List<Bloqueo> bloqueosEnArchivo = leerBloqueos(archivo.toString(), tramosExistentes);
//                bloqueos.addAll(bloqueosEnArchivo);  // Agregar los bloqueos leídos a la lista total
//            } else {
//                System.out.println("El archivo " + nombreArchivo + " no tiene el formato esperado y será ignorado.");
//            }
//        }
//
//        return bloqueos;
//    }

//    public static List<Bloqueo> leerBloqueos(String archivo, Map<String, Tramo> tramosExistentes) throws IOException {
//        List<Bloqueo> bloqueos = new ArrayList<>();
//        List<String> lineas = Files.readAllLines(Paths.get(archivo));
//
//        // Formato para "MMdd,HH:mm" (mes, día y hora)
//        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
//        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("MMdd");
//        int anioActual = LocalDateTime.now().getYear(); // Usamos el año actual
//
//        for (String linea : lineas) {
//            String[] datos = linea.split(";");
//
//            // Parsear el tramo: "250301 => 220501"
//            String[] tramos = datos[0].split("=>");
//            String ubigeoOrigen = tramos[0].trim();
//            String ubigeoDestino = tramos[1].trim();
//
//            // Verificar si el tramo ya existe en el mapa de tramos
//            String claveTramo = ubigeoOrigen + "=>" + ubigeoDestino;
//            Tramo tramo = tramosExistentes.get(claveTramo);
//            if (tramo == null) {
//                // Si no existe, crear el tramo y añadirlo al mapa
//                tramo = new Tramo(ubigeoOrigen, ubigeoDestino);
//                tramosExistentes.put(claveTramo, tramo);
//            }
//
//            // Parsear las fechas de inicio y fin: "0101,13:32==0119,10:39"
//            String[] tiempos = datos[1].split("==");
//            String fechaInicioStr = tiempos[0].split(",")[0].trim(); // "0101"
//            String horaInicioStr = tiempos[0].split(",")[1].trim(); // "13:32"
//            String fechaFinStr = tiempos[1].split(",")[0].trim(); // "0119"
//            String horaFinStr = tiempos[1].split(",")[1].trim(); // "10:39"
//
//            // Parsear fecha y hora de inicio
//            MonthDay mesDiaInicio = MonthDay.parse(fechaInicioStr, formatoFecha); // Parsear "MMdd"
//            LocalTime horaInicio = LocalTime.parse(horaInicioStr, formatoHora); // Parsear "HH:mm"
//            LocalDate fechaInicioCompleta = mesDiaInicio.atYear(anioActual); // Añadir el año actual
//            LocalDateTime fechaHoraInicio = LocalDateTime.of(fechaInicioCompleta, horaInicio);
//
//            // Parsear fecha y hora de fin
//            MonthDay mesDiaFin = MonthDay.parse(fechaFinStr, formatoFecha); // Parsear "MMdd"
//            LocalTime horaFin = LocalTime.parse(horaFinStr, formatoHora); // Parsear "HH:mm"
//            LocalDate fechaFinCompleta = mesDiaFin.atYear(anioActual); // Añadir el año actual
//            LocalDateTime fechaHoraFin = LocalDateTime.of(fechaFinCompleta, horaFin);
//
//            // Crear el objeto Bloqueo y asignarlo al Tramo
//            Bloqueo bloqueo = new Bloqueo(tramo, fechaHoraInicio, fechaHoraFin);
//            tramo.asignarBloqueo(bloqueo); // Asignar el bloqueo al tramo
//
//            // Añadir el bloqueo a la lista de bloqueos
//            bloqueos.add(bloqueo);
//        }
//
//        return bloqueos;
//    }

}