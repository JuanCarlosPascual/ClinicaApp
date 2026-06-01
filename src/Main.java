/**
 * Clase Main
 * Punto de entrada de la aplicacion ClinicaApp.
 * Sistema de administracion de citas para consultorio clinico.
 *
 * ClinicaApp - Entrega Final
 * Juan Carlos Pascual Lopez | AL03068354
 */
public class Main {

    public static void main(String[] args) {
        try {
            Sistema sistema = new Sistema();
            sistema.iniciar();
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}