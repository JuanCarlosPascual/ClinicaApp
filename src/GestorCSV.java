import java.io.*;
import java.util.*;

/**
 * Clase GestorCSV
 * Clase utilitaria para manejo de archivos CSV.
 * Implementa validacion y regeneracion automatica de archivos.
 *
 * ClinicaApp - Entrega Final
 * Juan Carlos Pascual Lopez | AL03068354
 */
public class GestorCSV {

    private String rutaArchivo;

    public GestorCSV(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        validarArchivo();
    }

    /**
     * Valida si el archivo existe; si no, lo regenera automaticamente.
     */
    public boolean validarArchivo() {
        File archivo = new File(rutaArchivo);
        File carpeta = archivo.getParentFile();
        try {
            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }
            if (!archivo.exists()) {
                archivo.createNewFile();
                return false;
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error al validar archivo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lee todas las lineas del archivo CSV.
     */
    public List<String> leerArchivo() {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) lineas.add(linea.trim());
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
        }
        return lineas;
    }

    /**
     * Sobreescribe el archivo con una lista de lineas.
     */
    public void escribirArchivo(List<String> datos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo, false))) {
            for (String linea : datos) {
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al escribir archivo: " + e.getMessage());
        }
    }

    /**
     * Agrega una linea al final del archivo.
     */
    public void agregarLinea(String linea) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            writer.write(linea);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error al agregar linea: " + e.getMessage());
        }
    }

    public String getRutaArchivo() { return rutaArchivo; }
}