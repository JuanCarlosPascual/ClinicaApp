/**
 * Clase Cita
 * Representa una cita medica del consultorio.
 * Implementa Persistible para almacenamiento en CSV.
 *
 * ClinicaApp - Entrega Final
 * Juan Carlos Pascual Lopez | AL03068354
 */
public class Cita implements Persistible {

    private String id;
    private String fechaHora;
    private String motivo;
    private Doctor doctor;
    private Paciente paciente;
    private static final GestorCSV gestor = new GestorCSV("db/citas.csv");

    public Cita(String id, String fechaHora, String motivo) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.doctor = null;
        this.paciente = null;
    }

    public String getId()        { return id; }
    public String getFechaHora() { return fechaHora; }
    public String getMotivo()    { return motivo; }
    public Doctor getDoctor()    { return doctor; }
    public Paciente getPaciente(){ return paciente; }

    public void asignarDoctor(Doctor doctor)     { this.doctor = doctor; }
    public void asignarPaciente(Paciente paciente){ this.paciente = paciente; }

    public void mostrarInfo() {
        String nomDoc = (doctor != null) ? doctor.getNombre() : "Sin asignar";
        String nomPac = (paciente != null) ? paciente.getNombre() : "Sin asignar";
        System.out.printf("  ID: %-6s | Fecha: %-16s | Motivo: %-20s | Doctor: %-20s | Paciente: %s%n",
                id, fechaHora, motivo, nomDoc, nomPac);
    }

    @Override
    public void guardar() {
        String idDoc = (doctor != null) ? doctor.getId() : "";
        String idPac = (paciente != null) ? paciente.getId() : "";
        java.util.List<String> lineas = gestor.leerArchivo();
        lineas.removeIf(l -> l.startsWith(id + ","));
        lineas.add(id + "," + fechaHora + "," + motivo + "," + idDoc + "," + idPac);
        gestor.escribirArchivo(lineas);
    }

    @Override
    public void cargar() {
        for (String linea : gestor.leerArchivo()) {
            String[] p = linea.split(",", 5);
            if (p.length >= 3 && p[0].equals(id)) {
                fechaHora = p[1]; motivo = p[2];
            }
        }
    }

    public static GestorCSV getGestor() { return gestor; }

    public String toCsv() {
        String idDoc = (doctor != null) ? doctor.getId() : "";
        String idPac = (paciente != null) ? paciente.getId() : "";
        return id + "," + fechaHora + "," + motivo + "," + idDoc + "," + idPac;
    }
}