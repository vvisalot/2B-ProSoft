package Utils;

import Clases.Bloqueo;
import Clases.Mantenimiento;
import Clases.Oficina;
import Clases.Tramo;
import Clases.Velocidad;
import Clases.Venta;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LeerDatos {

    // Leer el archivo de oficinas y devolver una lista de objetos Oficina
    public static List<Oficina> leerOficinas(String archivo) throws IOException {
        List<Oficina> oficinas = new ArrayList<>();
        List<String> lineas = Files.readAllLines(Paths.get(archivo));

        for (String linea : lineas) {
            // Parsear la línea: "UBIGEO,DEP,PROV,LATITUD,LONGITUD,REGION.NATURAL,ALMACEN"
            String[] datos = linea.split(",");
            if(datos.length == 7){
                String ubigeo = datos[0].trim();
                String departamento = datos[1].trim();
                String provincia = datos[2].trim();
                double latitud = Double.parseDouble(datos[3].trim());
                double longitud = Double.parseDouble(datos[4].trim());
                String regionNatural = datos[5].trim();
                String alm = datos[6].trim();
                int almacen = Integer.parseInt(String.valueOf(alm));
                // Crear el objeto Oficina y agregarlo a la lista
                Oficina oficina = new Oficina(ubigeo, departamento, provincia, latitud, longitud, regionNatural, almacen);
                oficinas.add(oficina);
            }
        }
        return oficinas;
    }

 // Método para leer el archivo de mantenimientos y devolver una lista de objetos Mantenimiento
    public static List<Mantenimiento> leerMantenimientos(String archivo) throws IOException {
        List<Mantenimiento> mantenimientos = new ArrayList<>();
        List<String> lineas = Files.readAllLines(Paths.get(archivo));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        for (String linea : lineas) {
            // Parsear la línea: "aaaammdd:TTNN"
            String[] datos = linea.split(":");
            if (datos.length == 2) {
                String fechaStr = datos[0].trim();
                String camion = datos[1].trim();

                // Convertir la fecha de formato aaaammdd a LocalDateTime
                String fechaFormateada = fechaStr.substring(0, 4) + "-" + fechaStr.substring(4, 6) + "-" + fechaStr.substring(6, 8);
                LocalDateTime fechaHoraInicio = LocalDateTime.parse(fechaFormateada + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                // Crear el objeto Mantenimiento y agregarlo a la lista
                Mantenimiento mantenimiento = new Mantenimiento(camion, fechaHoraInicio);
                mantenimientos.add(mantenimiento);
            }
        }
        return mantenimientos;
    }
    
    public static List<Tramo> leerTramos(String archivo) throws IOException {
        List<Tramo> tramos = new ArrayList<>();
        List<String> lineas = Files.readAllLines(Paths.get(archivo));

        for (String linea : lineas) {
            // Parsear la línea: "UBIGEO_ORIGEN => UBIGEO_DESTINO"
            String[] datos = linea.split("=>");
            if(datos.length == 2){
                String ubigeoOrigen = datos[0].trim();
                String ubigeoDestino = datos[1].trim();
                // Crear el objeto Tramo y agregarlo a la lista
                Tramo tramo = new Tramo(ubigeoOrigen, ubigeoDestino);
                tramos.add(tramo);
            }
        }
        return tramos;
    }

    // Leer todos los archivos de ventas en la carpeta y devolver una lista de ventas
    public static List<Venta> leerVentasEnCarpeta(String carpetaVentas) throws IOException {
        List<Venta> ventas = new ArrayList<>();
        DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(carpetaVentas), "*.txt");

        for (Path archivo : stream) {
            String nombreArch = archivo.getFileName().toString();
            // Eliminar el prefijo "c.1inf54." del nombre del archivo, si existe
            String nombreArchivo = nombreArch.replaceFirst("^c\\.1inf54\\.", "");

            // Validar que el nombre del archivo sigue el formato "ventasAAAAmm.txt"
            if (nombreArchivo.matches("ventas\\d{6}\\.txt")) {
                // Extraer el año y el mes (ventas202403.txt -> año: 2024, mes: 03)
                int anio = Integer.parseInt(nombreArchivo.substring(6, 10)); // extraemos el año (2024)
                int mes = Integer.parseInt(nombreArchivo.substring(10, 12)); // extraemos el mes (03)

                // Validar que el mes esté en el rango válido
                if (mes < 1 || mes > 12) {
                    throw new IllegalArgumentException("Mes inválido: " + mes + " en el archivo " + nombreArchivo);
                }

                // Leer las ventas en el archivo actual
                List<Venta> ventasEnArchivo = leerVentas(archivo.toString(), anio, mes);
                ventas.addAll(ventasEnArchivo);  // Agregar las ventas leídas a la lista total
            } else {
                System.out.println("El archivo " + nombreArchivo + " no tiene el formato esperado y será ignorado.");
            }
        }
        return ventas;
    }
    // Leer un archivo de ventas específico
    public static List<Venta> leerVentas(String archivo, int anio, int mes) throws IOException {
        List<Venta> ventas = new ArrayList<>();
        List<String> lineas = Files.readAllLines(Paths.get(archivo));

        // Formato para "dd HH:mm" (día y hora)
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("dd HH:mm");

        for (String linea : lineas) {
            // Dividir la línea por comas, y después limpiar espacios
            String[] datos = linea.split(",");
            for (int i = 0; i < datos.length; i++) {
                datos[i] = datos[i].trim(); // Eliminar espacios adicionales en cada parte
            }

            // Parsear el día y la hora (sin mes y año)
            String diaHoraStr = datos[0];
            LocalTime hora = LocalTime.parse(diaHoraStr.substring(3), DateTimeFormatter.ofPattern("HH:mm"));
            int dia = Integer.parseInt(diaHoraStr.substring(0, 2));

            // Construir el LocalDateTime combinando día, mes, año y la hora
            LocalDate fecha = LocalDate.of(anio, mes, dia);
            LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

            // Parsear el tramo (ubigeo origen y destino)
            String ubigeoOrigen = datos[1].split("=>")[0].trim();
            String ubigeoDestino = datos[1].split("=>")[1].trim();

            // Parsear cantidad e ID del cliente
            int cantidad = Integer.parseInt(datos[2]);
            String idCliente = datos[3];

            // Crear el objeto Venta y agregarlo a la lista
            Venta venta = new Venta(fechaHora, ubigeoOrigen, ubigeoDestino, cantidad, idCliente);
            ventas.add(venta);
        }

        return ventas;
    }
    // Leer todos los archivos de bloqueos en la carpeta y devolver una lista de bloqueos
    public static List<Bloqueo> leerBloqueosEnCarpeta(String carpetaBloqueos) throws IOException {
        List<Bloqueo> bloqueos = new ArrayList<>();

        // Obtenemos todos los archivos en la carpeta de bloqueos
        DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(carpetaBloqueos), "*.txt");

        for (Path archivo : stream) {
            String nombreArch = archivo.getFileName().toString();
            // Eliminar el prefijo "c.1inf54." del nombre del archivo, si existe
            String nombreArchivo = nombreArch.replaceFirst("^c\\.1inf54\\.24\\-2\\.", "");

            // Validar que el nombre del archivo sigue el formato "bloqueoNN.txt"
            if (nombreArchivo.matches("bloqueo\\.\\d{2}\\.txt")) {
                // Leer los bloqueos en el archivo actual
                List<Bloqueo> bloqueosEnArchivo = leerBloqueos(archivo.toString());
                bloqueos.addAll(bloqueosEnArchivo);  // Agregar los bloqueos leídos a la lista total
            } else {
                System.out.println("El archivo " + nombreArchivo + " no tiene el formato esperado y será ignorado.");
            }
        }

        return bloqueos;
    }
    // Leer un archivo de bloqueos específico
    public static List<Bloqueo> leerBloqueos(String archivo) throws IOException {
        List<Bloqueo> bloqueos = new ArrayList<>();
        List<String> lineas = Files.readAllLines(Paths.get(archivo));

        // Formato para "MMdd,HH:mm" (mes, día y hora)
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("MMdd");
        int anioActual = LocalDateTime.now().getYear(); // Usamos el año actual

        for (String linea : lineas) {
            String[] datos = linea.split(";");

            // Parsear el tramo: "250301 => 220501"
            String[] tramos = datos[0].split("=>");
            Tramo tramoBloqueo = new Tramo(tramos[0].trim(),tramos[1].trim());

            // Parsear las fechas de inicio y fin: "0101,13:32==0119,10:39"
            String[] tiempos = datos[1].split("==");
            String fechaInicioStr = tiempos[0].split(",")[0].trim(); // "0101"
            String horaInicioStr = tiempos[0].split(",")[1].trim(); // "13:32"
            String fechaFinStr = tiempos[1].split(",")[0].trim(); // "0119"
            String horaFinStr = tiempos[1].split(",")[1].trim(); // "10:39"

            // Parsear fecha y hora de inicio
            MonthDay mesDiaInicio = MonthDay.parse(fechaInicioStr, formatoFecha); // Parsear "MMdd"
            LocalTime horaInicio = LocalTime.parse(horaInicioStr, formatoHora); // Parsear "HH:mm"
            LocalDate fechaInicioCompleta = mesDiaInicio.atYear(anioActual); // Añadir el año actual
            LocalDateTime fechaHoraInicio = LocalDateTime.of(fechaInicioCompleta, horaInicio);

            // Parsear fecha y hora de fin
            MonthDay mesDiaFin = MonthDay.parse(fechaFinStr, formatoFecha); // Parsear "MMdd"
            LocalTime horaFin = LocalTime.parse(horaFinStr, formatoHora); // Parsear "HH:mm"
            LocalDate fechaFinCompleta = mesDiaFin.atYear(anioActual); // Añadir el año actual
            LocalDateTime fechaHoraFin = LocalDateTime.of(fechaFinCompleta, horaFin);

            //Crear objeto Bloqueo y añadirlo a la lista
            Bloqueo bloqueo = new Bloqueo(tramoBloqueo, fechaHoraInicio, fechaHoraFin);
            bloqueos.add(bloqueo);
        }

        return bloqueos;
    }

        
    public static List<Velocidad> leerVelocidades(String archivo) throws IOException {
        List<Velocidad> listaVelocidades = new ArrayList<>();
        List<String> lineas = Files.readAllLines(Paths.get(archivo));

        for (String linea : lineas) {
            if (linea.contains("=")) {
                // Parsear la línea, formato: "Origen - Destino = velocidad Km/h"
                String[] partes = linea.split("=");
                String[] regiones = partes[0].trim().split("-");

                String regionOrigen = regiones[0].trim();
                String regionDestino = regiones[1].trim();
                double velocidadKmh = Double.parseDouble(partes[1].replace("Km/h", "").trim());

                // Crear el objeto Velocidad y agregarlo a la lista
                Velocidad velocidad = new Velocidad(regionOrigen, regionDestino, velocidadKmh);
                listaVelocidades.add(velocidad);
            }
        }

        return listaVelocidades;
    }

}
