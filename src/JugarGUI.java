import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class JugarGUI extends JFrame implements ActionListener {

    private JMenuBar jugarMenuBar;
    private JMenu jugarArchivoMenu, jugarAyudaMenu, jugarEdicionMenu;
    private JMenuItem jugarGuardarMenuItem, jugarDeshacerMenuItem, jugarRehacerMenuItem, jugarResolverMenuItem;

    private boolean juegoTerminado;
    private boolean rehacer;
    private boolean deshacer;

    private Tablero tablero;
    private ArrayList<Movimiento> movimientos;
    private Component[] buttons;

    private Stack<Tablero> prevTableros, postTableros;
    private Stack<Movimiento> postMovimientos;

    private Resolver resolver;

    public JugarGUI(Tablero tablero, ArrayList<Movimiento> movimientos, Component[] buttons) {

        jugarMenuBar = new JMenuBar();

        jugarArchivoMenu = new JMenu("Archivo");
        jugarAyudaMenu = new JMenu("Ayuda");
        jugarEdicionMenu = new JMenu("Edicion");

        jugarGuardarMenuItem = new JMenuItem("Guardar");
        jugarDeshacerMenuItem = new JMenuItem("Deshacer");
        jugarRehacerMenuItem = new JMenuItem("Rehacer");
        jugarResolverMenuItem = new JMenuItem("Resolver");

        jugarGuardarMenuItem.addActionListener(this);
        jugarDeshacerMenuItem.addActionListener(this);
        jugarRehacerMenuItem.addActionListener(this);
        jugarResolverMenuItem.addActionListener(this);

        jugarArchivoMenu.add(jugarGuardarMenuItem);
        jugarEdicionMenu.add(jugarDeshacerMenuItem);
        jugarEdicionMenu.add(jugarRehacerMenuItem);
        jugarAyudaMenu.add(jugarResolverMenuItem);

        jugarMenuBar.add(jugarArchivoMenu);
        jugarMenuBar.add(jugarEdicionMenu);
        jugarMenuBar.add(jugarAyudaMenu);

        this.setSize(1000, 600);
        this.setTitle("Jugar");
        this.setVisible(true);
        this.setResizable(false);

        this.setJMenuBar(jugarMenuBar);

        this.tablero = tablero;
        this.movimientos = movimientos;
        this.buttons = buttons;

        this.prevTableros = new Stack<>();
        this.postTableros = new Stack<>();
        this.postMovimientos = new Stack<>();

        this.juegoTerminado = false;
        this.rehacer = false;
        this.deshacer = false;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == jugarGuardarMenuItem) {
            if (juegoTerminado) {

                JFileChooser fileChooser = new JFileChooser(new File("soluciones/"));
                fileChooser.setDialogTitle("Guardar solucion");
                int response = fileChooser.showSaveDialog(null);

                if (response == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String fichasFormat, puntosFormat;

                    try {
                        FileWriter writer = new FileWriter(file, false);
                        int cont = 1;
                        for (Movimiento movimiento : this.movimientos) {

                            if (movimiento.getNumFichasEliminadas() == 1)
                                fichasFormat = "ficha";
                            else
                                fichasFormat = "fichas";

                            if (movimiento.getScore() == 1)
                                puntosFormat = "punto";
                            else
                                puntosFormat = "puntos";

                            writer.write("Movimiento " + cont + " en (" +
                                    (this.tablero.getFilas()
                                            - movimiento.getFila())
                                    + ", " +
                                    (movimiento.getColumna() + 1) +
                                    "): eliminó " + movimiento.getNumFichasEliminadas()
                                    + " "
                                    + fichasFormat +
                                    " de color " + movimiento.getColor() +
                                    " y obtuvo " + movimiento.getScore() + " "
                                    + puntosFormat
                                    + ".\n");

                            cont++;
                        }

                        if (this.tablero.getNumFichas() == 1)
                            fichasFormat = "ficha";
                        else
                            fichasFormat = "fichas";

                        FichasIguales fi = new FichasIguales(this.tablero);

                        writer.write("Puntuación final: " +
                                fi.calcularScore(this.tablero, this.movimientos)
                                + ", quedando " + this.tablero.getNumFichas() + " " + fichasFormat
                                + ".\n");
                        writer.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
            }

        } else if (e.getSource() == jugarResolverMenuItem) {

            int filas = this.tablero.getFilas();
            int columnas = this.tablero.getColumnas();

            JRootPane rootPane = this.getRootPane();
            Component[] components = rootPane.getContentPane().getComponents();
            JPanel mainPanel = (JPanel) components[0];
            Component[] buttons = mainPanel.getComponents();

            for (int i = 0; i < filas * columnas; i++) {
                JButton button = (JButton) buttons[i];
                button.removeActionListener(button.getActionListeners()[0]);
            }

            this.resolver = new Resolver(this, buttons, this.tablero, this.movimientos);
            this.resolver.execute();
            this.juegoTerminado = true;

        } else if (e.getSource() == jugarDeshacerMenuItem) {

            if (this.prevTableros.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay movimientos para deshacer");
                return;
            }

            this.deshacer = true;

            this.postTableros.push(new Tablero(this.tablero));
            this.tablero = new Tablero(this.prevTableros.pop());
            this.postMovimientos.push(this.movimientos.get(this.movimientos.size() - 1));
            this.movimientos.remove(this.movimientos.size() - 1);

            for (int i = 0; i < this.tablero.getFilas(); i++) {
                for (int j = 0; j < this.tablero.getColumnas(); j++) {
                    if (this.tablero.getFichas()[i][j] == 'R') {
                        this.buttons[i * this.tablero.getColumnas() + j].setBackground(Color.RED);
                    } else if (this.tablero.getFichas()[i][j] == 'A') {
                        this.buttons[i * this.tablero.getColumnas() + j].setBackground(Color.BLUE);
                    } else if (this.tablero.getFichas()[i][j] == 'V') {
                        this.buttons[i * this.tablero.getColumnas() + j].setBackground(Color.GREEN);
                    } else {
                        this.buttons[i * this.tablero.getColumnas() + j].setBackground(null);
                    }
                }
            }

        } else if (e.getSource() == jugarRehacerMenuItem) {

            if (this.postTableros.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay movimientos para rehacer");
                return;
            }

            this.rehacer = true;

            this.prevTableros.push(new Tablero(this.tablero));
            this.tablero = new Tablero(this.postTableros.pop());
            this.movimientos.add(this.postMovimientos.pop());

            for (int i = 0; i < this.tablero.getFilas(); i++) {
                for (int j = 0; j < this.tablero.getColumnas(); j++) {
                    if (this.tablero.getFichas()[i][j] == 'R') {
                        this.buttons[i * this.tablero.getColumnas() + j].setBackground(Color.RED);
                    } else if (this.tablero.getFichas()[i][j] == 'A') {
                        this.buttons[i * this.tablero.getColumnas() + j].setBackground(Color.BLUE);
                    } else if (this.tablero.getFichas()[i][j] == 'V') {
                        this.buttons[i * this.tablero.getColumnas() + j].setBackground(Color.GREEN);
                    } else {
                        this.buttons[i * this.tablero.getColumnas() + j].setBackground(null);
                    }
                }
            }
        }
    }

    public void setJuegoTerminado(boolean juegoTerminado) {
        this.juegoTerminado = juegoTerminado;
    }

    public Tablero getTablero() {
        return this.tablero;
    }

    public void procesarDatos(ArrayList<Movimiento> movs) {
        for (Movimiento mov : movs) {
            this.movimientos.add(mov);
        }
    }

    public void pushPrevTablero(Tablero tablero) {
        this.prevTableros.push(tablero);
    }

    public boolean getDeshacer() {
        return this.deshacer;
    }

    public boolean getRehacer() {
        return this.rehacer;
    }

    public void setDeshacer(boolean deshacer) {
        this.deshacer = deshacer;
    }

    public void setRehacer(boolean rehacer) {
        this.rehacer = rehacer;
    }

}