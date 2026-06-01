/**
 * Clase Doctor
 * Representa un doctor del consultorio medico.
 * Hereda de Persona e implementa Persistible.
 *
 * ClinicaApp - Entrega Final
 * Juan Carlos Pascual Lopez | AL03068354
 */
public class Doctor extends Persona implements Persistible {

    private String especialidad;
    private static final GestorCSV gestor = new GestorCSV("db/doctores.csv");

    public Doctor(String id, String nombre, String especialidad) {
        super(id, nombre);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    @Override
    public void mostrarInfo() {
        System.out.printf("  ID: %-8s | Nombre: %-25s | Especialidad: %s%n",
                id, nombre, especialidad);
    }

    @Override
    public void guardar() {
        java.util.List<String> lineas = gestor.leerArchivo();
        lineas.removeIf(l -> l.startsWith(id + ","));
        lineas.add(id + "," + nombre + "," + especialidad);
        gestor.escribirArchivo(lineas);
    }

    @Override
    public void cargar() {
        for (String linea : gestor.leerArchivo()) {
            String[] p = linea.split(",", 3);
            if (p.length == 3 && p[0].equals(id)) {
                nombre = p[1]; especialidad = p[2];
            }
        }
    }

    public static GestorCSV getGestor() { return gestor; }

    public String toCsv() { return id + "," + nombre + "," + especialidad; }
}