/**
 * Clase abstracta Persona
 * Clase base de la jerarquia de herencia.
 * Contiene los atributos comunes a Doctor, Paciente y Administrador.
 *
 * ClinicaApp - Entrega Final
 * Juan Carlos Pascual Lopez | AL03068354
 */
public abstract class Persona {

    protected String id;
    protected String nombre;

    public Persona(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId()     { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public abstract void mostrarInfo();
}