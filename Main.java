// Sistema de eventos - versión 1
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


// 1. EL MODELO (Tus "Archivos de Almacén")

class Evento {
    private String fecha;
    private String descripcion;
    private String frecuencia;
    private String email;
    private boolean alarma;

    public Evento(String fecha, String descripcion, String email, String frecuencia, boolean alarma) {
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.email = email;
        this.frecuencia = frecuencia;
        this.alarma = alarma;
    }

    public String getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }
    public String getFrecuencia() { return frecuencia; }
    public String getEmail() { return email; }
    public boolean hasAlarma() { return alarma; }
}

class GestorEventos {
    private List<Evento> listaEventos = new ArrayList<>();

    public void agregarEvento(Evento e) { listaEventos.add(e); }
    public List<Evento> obtenerEventos() { return listaEventos; }
    public void eliminarEvento(Evento e) { listaEventos.remove(e); }
}

// 2. LA VISTA (Tu "Panel de Control" con Pestañas)

class VistaApp extends JFrame {
    // --- Pestaña 1: New Event ---
    public JTextField txtDesc = new JTextField(20);
    public JTextField txtEmail = new JTextField(20);
    public JTextField txtDate = new JTextField(10);
    public JRadioButton rbDaily = new JRadioButton("Daily");
    public JRadioButton rbWeekly = new JRadioButton("Weekly");
    public JRadioButton rbMonthly = new JRadioButton("Monthly");
    public JCheckBox chkAlarm = new JCheckBox("Alarm");
    public JButton btnSave = new JButton("Save");
    public JButton btnClean = new JButton("Clean");

    // --- Pestaña 2: Events (Tabla) ---
    public DefaultTableModel modeloTablaEvents;
    public JTable tablaEvents;

    // --- Pestaña 3: Remove Event (Tabla con Checkbox) ---
    public DefaultTableModel modeloTablaRemove;
    public JTable tablaRemove;
    public JButton btnCancel = new JButton("Cancel");
    public JButton btnRemover = new JButton("Remover");
    public JButton btnSeleccionarTodos = new JButton("Seleccionar Todos");

    // --- Pestaña 4: Registrar invitado ---
    public JTextField txtNombreInv = new JTextField(20);
    public JTextField txtCelularInv = new JTextField(20);
    public JRadioButton rbMasc = new JRadioButton("Masculino");
    public JRadioButton rbFem = new JRadioButton("Femenino");
    public JComboBox<String> cbDia = new JComboBox<>();
    public JComboBox<String> cbMes = new JComboBox<>(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
    public JComboBox<String> cbAnio = new JComboBox<>();
    public JTextField txtDireccion = new JTextField(20);
    public JCheckBox chkTerminos = new JCheckBox("Acepta Términos y Condiciones");

    public VistaApp() {
        setTitle("Sistema de Eventos MVC - Bianca");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla

        // Contenedor principal de pestañas
        JTabbedPane pestanas = new JTabbedPane();

        // Construir cada pestaña
        pestanas.addTab("New event", crearPanelNewEvent());
        pestanas.addTab("Events", crearPanelEvents());
        pestanas.addTab("Remove Event", crearPanelRemove());
        pestanas.addTab("Registrar invitado", crearPanelInvitado());
        pestanas.addTab("Buscar evento", new JPanel()); // Pestaña vacía por ahora

        add(pestanas);
    }

    // --- Métodos para dibujar cada pestaña ---

    private JPanel crearPanelNewEvent() {
        // Usamos Absolute Layout (null) para imitar exactamente la imagen
        JPanel panel = new JPanel(null);

        JLabel lblDesc = new JLabel("Event description");
        lblDesc.setBounds(50, 40, 120, 25);
        txtDesc.setBounds(180, 40, 200, 25);

        JLabel lblEmail = new JLabel("Forward e-mail");
        lblEmail.setBounds(50, 80, 120, 25);
        txtEmail.setBounds(180, 80, 200, 25);

        JLabel lblDate = new JLabel("Date");
        lblDate.setBounds(50, 120, 120, 25);
        txtDate.setBounds(180, 120, 100, 25);

        JLabel lblFreq = new JLabel("Frequency");
        lblFreq.setBounds(50, 160, 120, 25);
        rbDaily.setBounds(180, 160, 60, 25);
        rbWeekly.setBounds(250, 160, 70, 25);
        rbMonthly.setBounds(330, 160, 80, 25);

        // Agrupar radio buttons para que solo se seleccione uno
        ButtonGroup bgFreq = new ButtonGroup();
        bgFreq.add(rbDaily); bgFreq.add(rbWeekly); bgFreq.add(rbMonthly);
        rbMonthly.setSelected(true); // Seleccionado por defecto

        chkAlarm.setBounds(50, 220, 80, 25);
        btnSave.setBounds(180, 220, 80, 25);
        btnClean.setBounds(280, 220, 80, 25);

        panel.add(lblDesc); panel.add(txtDesc);
        panel.add(lblEmail); panel.add(txtEmail);
        panel.add(lblDate); panel.add(txtDate);
        panel.add(lblFreq); panel.add(rbDaily); panel.add(rbWeekly); panel.add(rbMonthly);
        panel.add(chkAlarm); panel.add(btnSave); panel.add(btnClean);

        return panel;
    }

    private JPanel crearPanelEvents() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnas = {"Date", "Description", "Frequency", "E-mail", "Alarm"};
        modeloTablaEvents = new DefaultTableModel(columnas, 0);
        tablaEvents = new JTable(modeloTablaEvents);
        panel.add(new JScrollPane(tablaEvents), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelRemove() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tabla especial para que la última columna sea un Checkbox (Boolean)
        String[] columnas = {"Date", "Description", "Frequency", "E-mail", "Alarm", "Boolean"};
        modeloTablaRemove = new DefaultTableModel(columnas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) return Boolean.class; // Muestra el Checkbox
                return super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Solo se puede editar/hacer clic en el checkbox
            }
        };
        tablaRemove = new JTable(modeloTablaRemove);
        panel.add(new JScrollPane(tablaRemove), BorderLayout.CENTER);

        // Botones de abajo
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnCancel);
        panelBotones.add(btnRemover);
        panelBotones.add(btnSeleccionarTodos);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelInvitado() {
        JPanel panel = new JPanel(null);

        JLabel lblNom = new JLabel("Ingrese Nombre:");
        lblNom.setBounds(50, 40, 150, 25);
        txtNombreInv.setBounds(200, 40, 200, 25);

        JLabel lblCel = new JLabel("Ingrese número celular:");
        lblCel.setBounds(50, 80, 150, 25);
        txtCelularInv.setBounds(200, 80, 200, 25);

        JLabel lblGen = new JLabel("Género:");
        lblGen.setBounds(50, 120, 150, 25);
        rbMasc.setBounds(200, 120, 90, 25);
        rbFem.setBounds(300, 120, 90, 25);
        ButtonGroup bgGen = new ButtonGroup();
        bgGen.add(rbMasc); bgGen.add(rbFem);
        rbMasc.setSelected(true);

        JLabel lblNac = new JLabel("Fecha de Nacimiento:");
        lblNac.setBounds(50, 160, 150, 25);

        // Llenar ComboBoxes de fecha
        for(int i=1; i<=31; i++) cbDia.addItem(String.valueOf(i));
        for(int i=1950; i<=2024; i++) cbAnio.addItem(String.valueOf(i));
        cbAnio.setSelectedItem("1995");

        cbDia.setBounds(200, 160, 50, 25);
        cbMes.setBounds(260, 160, 60, 25);
        cbAnio.setBounds(330, 160, 70, 25);

        JLabel lblDir = new JLabel("Dirección:");
        lblDir.setBounds(50, 200, 150, 25);
        txtDireccion.setBounds(200, 200, 200, 25);

        chkTerminos.setBounds(50, 250, 250, 25);

        panel.add(lblNom); panel.add(txtNombreInv);
        panel.add(lblCel); panel.add(txtCelularInv);
        panel.add(lblGen); panel.add(rbMasc); panel.add(rbFem);
        panel.add(lblNac); panel.add(cbDia); panel.add(cbMes); panel.add(cbAnio);
        panel.add(lblDir); panel.add(txtDireccion);
        panel.add(chkTerminos);

        return panel;
    }
}

// 3. EL CONTROLADOR (El "Supervisor Lógico")
// Controlador principal de la aplicación
class ControladorApp {
    private VistaApp vista;
    private GestorEventos modelo;

    public ControladorApp(VistaApp vista, GestorEventos modelo) {
        this.vista = vista;
        this.modelo = modelo;

        // Acción: Botón SAVE (Guardar Evento)
        this.vista.btnSave.addActionListener(e -> {
            String desc = vista.txtDesc.getText();
            String email = vista.txtEmail.getText();
            String date = vista.txtDate.getText();

            String freq = "MONTHLY";
            if(vista.rbDaily.isSelected()) freq = "DAILY";
            if(vista.rbWeekly.isSelected()) freq = "WEEKLY";

            boolean alarm = vista.chkAlarm.isSelected();

            if(!desc.isEmpty() && !date.isEmpty()) {
                Evento nuevoEvento = new Evento(date, desc, email, freq, alarm);
                modelo.agregarEvento(nuevoEvento);
                actualizarTablas();
                JOptionPane.showMessageDialog(vista, "Evento guardado con éxito.");
            } else {
                JOptionPane.showMessageDialog(vista, "Descripción y Fecha son obligatorios.");
            }
        });

        // Acción: Botón CLEAN (Limpiar formulario)
        this.vista.btnClean.addActionListener(e -> {
            vista.txtDesc.setText("");
            vista.txtEmail.setText("");
            vista.txtDate.setText("");
            vista.rbMonthly.setSelected(true);
            vista.chkAlarm.setSelected(false);
        });

        // Acción: Botón SELECCIONAR TODOS (Pestaña Remove)
        this.vista.btnSeleccionarTodos.addActionListener(e -> {
            for (int i = 0; i < vista.modeloTablaRemove.getRowCount(); i++) {
                vista.modeloTablaRemove.setValueAt(true, i, 5); // 5 es la columna "Boolean"
            }
        });

        // Acción: Botón REMOVER
        this.vista.btnRemover.addActionListener(e -> {
            // Recorremos la tabla de atrás hacia adelante para evitar errores al borrar
            List<Evento> eventosActuales = modelo.obtenerEventos();
            for (int i = vista.modeloTablaRemove.getRowCount() - 1; i >= 0; i--) {
                Boolean isChecked = (Boolean) vista.modeloTablaRemove.getValueAt(i, 5);
                if (isChecked != null && isChecked) {
                    modelo.eliminarEvento(eventosActuales.get(i));
                }
            }
            actualizarTablas();
        });
    }

    // Este método refresca las tablas estilo "Excel" para que muestren los datos reales
    private void actualizarTablas() {
        // Limpiar filas antiguas
        vista.modeloTablaEvents.setRowCount(0);
        vista.modeloTablaRemove.setRowCount(0);

        // Llenar con los datos actualizados del Modelo
        for (Evento e : modelo.obtenerEventos()) {
            String alarmaStr = e.hasAlarma() ? "ON" : "OFF";

            // Fila para la pestaña "Events"
            vista.modeloTablaEvents.addRow(new Object[]{
                    e.getFecha(), e.getDescripcion(), e.getFrecuencia(), e.getEmail(), alarmaStr
            });

            // Fila para la pestaña "Remove Event" (tiene la columna extra false por defecto)
            vista.modeloTablaRemove.addRow(new Object[]{
                    e.getFecha(), e.getDescripcion(), e.getFrecuencia(), e.getEmail(), alarmaStr, false
            });
        }
    }
}

// 4. MAIN (Arranque del Programa)

public class Main {
    public static void main(String[] args) {
        // Estilo visual de Windows clásico (como en tus fotos)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        GestorEventos modelo = new GestorEventos();
        VistaApp vista = new VistaApp();
        new ControladorApp(vista, modelo);

        vista.setVisible(true);
    }
}
