/**
 * Clase Paciente
 * Representa un paciente del consultorio medico.
 * Hereda de Persona e implementa Persistible.
 *
 * ClinicaApp - Entrega Final
 * Juan Carlos Pascual Lopez | AL03068354
 */
public class Paciente extends Persona implements Persistible {

    private static final GestorCSV gestor = new GestorCSV("db/pacientes.csv");

    public Paciente(String id, String nombre) {
        super(id, nombre);
    }

    @Override
    public void mostrarInfo() {
        System.out.printf("  ID: %-8s | Nombre: %s%n", id, nombre);
    }

    @Override
    public void guardar() {
        java.util.List<String> lineas = gestor.leerArchivo();
        lineas.removeIf(l -> l.startsWith(id + ","));
        lineas.add(id + "," + nombre);
        gestor.escribirArchivo(lineas);
    }

    @Override
    public void cargar() {
        for (String linea : gestor.leerArchivo()) {
            String[] p = linea.split(",", 2);
            if (p.length == 2 && p[0].equals(id)) {
                nombre = p[1];
            }
        }
    }

    public static GestorCSV getGestor() { return gestor; }

    public String toCsv() { return id + "," + nombre; }
}
