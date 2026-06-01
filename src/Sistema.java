import java.util.*;

/**
 * Clase Sistema
 * Clase principal de la logica del negocio.
 * Orquesta todas las operaciones del sistema de citas.
 *
 * ClinicaApp - Entrega Final
 * Juan Carlos Pascual Lopez | AL03068354
 */
public class Sistema {

    private List<Administrador> administradores;
    private List<Doctor> doctores;
    private List<Paciente> pacientes;
    private List<Cita> citas;
    private Scanner scanner;

    public Sistema() {
        administradores = new ArrayList<>();
        doctores        = new ArrayList<>();
        pacientes       = new ArrayList<>();
        citas           = new ArrayList<>();
        scanner         = new Scanner(System.in);
        inicializarAdmins();
        cargarDatos();
    }

    // ── INICIALIZACION ────────────────────────────────

    private void inicializarAdmins() {
        administradores.add(new Administrador("admin", "Administrador", "admin123"));
        administradores.add(new Administrador("jcpascual", "Juan Carlos Pascual", "tec2026"));
    }

    private void cargarDatos() {
        // Cargar doctores
        for (String linea : Doctor.getGestor().leerArchivo()) {
            String[] p = linea.split(",", 3);
            if (p.length == 3) doctores.add(new Doctor(p[0], p[1], p[2]));
        }
        // Cargar pacientes
        for (String linea : Paciente.getGestor().leerArchivo()) {
            String[] p = linea.split(",", 2);
            if (p.length == 2) pacientes.add(new Paciente(p[0], p[1]));
        }
        // Cargar citas
        for (String linea : Cita.getGestor().leerArchivo()) {
            String[] p = linea.split(",", 5);
            if (p.length >= 3) {
                Cita c = new Cita(p[0], p[1], p[2]);
                if (p.length >= 4 && !p[3].isEmpty()) {
                    buscarDoctor(p[3]).ifPresent(c::asignarDoctor);
                }
                if (p.length == 5 && !p[4].isEmpty()) {
                    buscarPaciente(p[4]).ifPresent(c::asignarPaciente);
                }
                citas.add(c);
            }
        }
        System.out.println("Datos cargados: " + doctores.size() + " doctor(es), " +
                pacientes.size() + " paciente(s), " + citas.size() + " cita(s).");
    }

    // ── AUTENTICACION ─────────────────────────────────

    public void iniciar() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║   SISTEMA DE CITAS - CLINICA APP  ║");
        System.out.println("╚══════════════════════════════════╝\n");

        boolean autenticado = false;
        int intentos = 0;

        while (!autenticado && intentos < 3) {
            System.out.print("Usuario: ");
            String id = scanner.nextLine().trim();
            System.out.print("Password: ");
            String pwd = scanner.nextLine().trim();

            for (Administrador admin : administradores) {
                if (admin.autenticar(id, pwd)) {
                    autenticado = true;
                    System.out.println("\nBienvenido, " + admin.getNombre() + "!\n");
                    break;
                }
            }
            if (!autenticado) {
                intentos++;
                System.out.println("Credenciales incorrectas. Intento " + intentos + "/3\n");
            }
        }

        if (!autenticado) {
            System.out.println("Acceso bloqueado. Demasiados intentos fallidos.");
            return;
        }
        mostrarMenu();
    }

    // ── MENU PRINCIPAL ────────────────────────────────

    public void mostrarMenu() {
        int opcion = 0;
        while (opcion != 5) {
            System.out.println("╔══════════════════════════════════╗");
            System.out.println("║          MENU PRINCIPAL           ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Gestion de Doctores           ║");
            System.out.println("║  2. Gestion de Pacientes          ║");
            System.out.println("║  3. Gestion de Citas              ║");
            System.out.println("║  4. Asignar Cita                  ║");
            System.out.println("║  5. Salir                         ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("Selecciona una opcion: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
                System.out.println();
                switch (opcion) {
                    case 1: menuDoctores();   break;
                    case 2: menuPacientes();  break;
                    case 3: menuCitas();      break;
                    case 4: asignarCita();    break;
                    case 5: System.out.println("Hasta luego!"); break;
                    default: System.out.println("Opcion no valida.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingresa un numero.\n");
            }
        }
        scanner.close();
    }

    // ── MENU DOCTORES ─────────────────────────────────

    private void menuDoctores() {
        System.out.println("--- GESTION DE DOCTORES ---");
        System.out.println("1. Registrar doctor");
        System.out.println("2. Listar doctores");
        System.out.print("Opcion: ");
        try {
            int op = Integer.parseInt(scanner.nextLine().trim());
            if (op == 1) registrarDoctor();
            else if (op == 2) listarDoctores();
        } catch (NumberFormatException e) {
            System.out.println("Opcion invalida.");
        }
        System.out.println();
    }

    private void registrarDoctor() {
        System.out.println("\n--- Registrar Doctor ---");
        System.out.print("ID unico: ");
        String id = scanner.nextLine().trim();
        if (buscarDoctor(id).isPresent()) {
            System.out.println("Error: ya existe un doctor con ese ID.");
            return;
        }
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Especialidad: ");
        String especialidad = scanner.nextLine().trim();
        if (id.isEmpty() || nombre.isEmpty() || especialidad.isEmpty()) {
            System.out.println("Error: todos los campos son obligatorios.");
            return;
        }
        Doctor doctor = new Doctor(id, nombre, especialidad);
        doctores.add(doctor);
        doctor.guardar();
        System.out.println("Doctor '" + nombre + "' registrado exitosamente.");
    }

    private void listarDoctores() {
        if (doctores.isEmpty()) { System.out.println("No hay doctores registrados."); return; }
        System.out.println("\nDoctores registrados:");
        doctores.forEach(Persona::mostrarInfo);
        System.out.println("Total: " + doctores.size());
    }

    // ── MENU PACIENTES ────────────────────────────────

    private void menuPacientes() {
        System.out.println("--- GESTION DE PACIENTES ---");
        System.out.println("1. Registrar paciente");
        System.out.println("2. Listar pacientes");
        System.out.print("Opcion: ");
        try {
            int op = Integer.parseInt(scanner.nextLine().trim());
            if (op == 1) registrarPaciente();
            else if (op == 2) listarPacientes();
        } catch (NumberFormatException e) {
            System.out.println("Opcion invalida.");
        }
        System.out.println();
    }

    private void registrarPaciente() {
        System.out.println("\n--- Registrar Paciente ---");
        System.out.print("ID unico: ");
        String id = scanner.nextLine().trim();
        if (buscarPaciente(id).isPresent()) {
            System.out.println("Error: ya existe un paciente con ese ID.");
            return;
        }
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine().trim();
        if (id.isEmpty() || nombre.isEmpty()) {
            System.out.println("Error: todos los campos son obligatorios.");
            return;
        }
        Paciente paciente = new Paciente(id, nombre);
        pacientes.add(paciente);
        paciente.guardar();
        System.out.println("Paciente '" + nombre + "' registrado exitosamente.");
    }

    private void listarPacientes() {
        if (pacientes.isEmpty()) { System.out.println("No hay pacientes registrados."); return; }
        System.out.println("\nPacientes registrados:");
        pacientes.forEach(Persona::mostrarInfo);
        System.out.println("Total: " + pacientes.size());
    }

    // ── MENU CITAS ────────────────────────────────────

    private void menuCitas() {
        System.out.println("--- GESTION DE CITAS ---");
        System.out.println("1. Crear cita");
        System.out.println("2. Listar citas");
        System.out.print("Opcion: ");
        try {
            int op = Integer.parseInt(scanner.nextLine().trim());
            if (op == 1) crearCita();
            else if (op == 2) listarCitas();
        } catch (NumberFormatException e) {
            System.out.println("Opcion invalida.");
        }
        System.out.println();
    }

    private void crearCita() {
        System.out.println("\n--- Crear Cita ---");
        System.out.print("ID unico de la cita: ");
        String id = scanner.nextLine().trim();
        if (buscarCita(id).isPresent()) {
            System.out.println("Error: ya existe una cita con ese ID.");
            return;
        }
        System.out.print("Fecha y hora (DD/MM/AAAA HH:MM): ");
        String fechaHora = scanner.nextLine().trim();
        System.out.print("Motivo de la cita: ");
        String motivo = scanner.nextLine().trim();
        if (id.isEmpty() || fechaHora.isEmpty() || motivo.isEmpty()) {
            System.out.println("Error: todos los campos son obligatorios.");
            return;
        }
        Cita cita = new Cita(id, fechaHora, motivo);
        citas.add(cita);
        cita.guardar();
        System.out.println("Cita '" + id + "' creada exitosamente.");
    }

    private void listarCitas() {
        if (citas.isEmpty()) { System.out.println("No hay citas registradas."); return; }
        System.out.println("\nCitas registradas:");
        citas.forEach(Cita::mostrarInfo);
        System.out.println("Total: " + citas.size());
    }

    // ── ASIGNAR CITA ──────────────────────────────────

    private void asignarCita() {
        System.out.println("\n--- Asignar Cita ---");
        System.out.print("ID de la cita: ");
        String idCita = scanner.nextLine().trim();
        Optional<Cita> optCita = buscarCita(idCita);
        if (!optCita.isPresent()) {
            System.out.println("Error: cita no encontrada."); return;
        }
        System.out.print("ID del doctor: ");
        String idDoc = scanner.nextLine().trim();
        Optional<Doctor> optDoc = buscarDoctor(idDoc);
        if (!optDoc.isPresent()) {
            System.out.println("Error: doctor no encontrado."); return;
        }
        System.out.print("ID del paciente: ");
        String idPac = scanner.nextLine().trim();
        Optional<Paciente> optPac = buscarPaciente(idPac);
        if (!optPac.isPresent()) {
            System.out.println("Error: paciente no encontrado."); return;
        }
        Cita cita = optCita.get();
        cita.asignarDoctor(optDoc.get());
        cita.asignarPaciente(optPac.get());
        cita.guardar();
        System.out.println("Cita asignada exitosamente.");
        System.out.println("  Doctor:   " + optDoc.get().getNombre());
        System.out.println("  Paciente: " + optPac.get().getNombre());
        System.out.println();
    }

    // ── BUSQUEDAS ─────────────────────────────────────

    private Optional<Doctor> buscarDoctor(String id) {
        return doctores.stream().filter(d -> d.getId().equals(id)).findFirst();
    }
    private Optional<Paciente> buscarPaciente(String id) {
        return pacientes.stream().filter(p -> p.getId().equals(id)).findFirst();
    }
    private Optional<Cita> buscarCita(String id) {
        return citas.stream().filter(c -> c.getId().equals(id)).findFirst();
    }
}