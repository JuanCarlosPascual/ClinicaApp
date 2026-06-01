/**
 * Clase Administrador
 * Representa un usuario administrador del sistema.
 * Hereda de Persona e implementa Autenticable.
 *
 * ClinicaApp - Entrega Final
 * Juan Carlos Pascual Lopez | AL03068354
 */
public class Administrador extends Persona implements Autenticable {

    private String password;

    public Administrador(String id, String nombre, String password) {
        super(id, nombre);
        this.password = password;
    }

    @Override
    public boolean autenticar(String id, String password) {
        return this.id.equals(id) && this.password.equals(password);
    }

    @Override
    public void mostrarInfo() {
        System.out.printf("  Admin: %-8s | Nombre: %s%n", id, nombre);
    }
}