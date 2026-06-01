/**
 * Interfaz Autenticable
 * Define el contrato de autenticacion para usuarios del sistema.
 *
 * ClinicaApp - Entrega Final
 * Juan Carlos Pascual Lopez | AL03068354
 */
public interface Autenticable {
    boolean autenticar(String id, String password);
}