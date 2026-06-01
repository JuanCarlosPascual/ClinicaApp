/**
 * Interfaz Persistible
 * Define el contrato para todas las clases que necesitan
 * almacenarse y recuperarse desde archivos CSV.
 *
 * ClinicaApp - Entrega Final
 * Juan Carlos Pascual Lopez | AL03068354
 */
public interface Persistible {
    void guardar();
    void cargar();
}