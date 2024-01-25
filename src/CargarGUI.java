import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.GridLayout;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

public class CargarGUI extends JFrame implements ActionListener {
    private JMenuBar cargarMenuBar;
    private JMenu cargarArchivoMenu, cargarJugarMenu;
    private JMenuItem cargarGuardarMenuItem, cargarJugarMenuItem;

    private Tablero tablero;

    public CargarGUI(Tablero tablero) {

        cargarMenuBar = new JMenuBar();

        cargarArchivoMenu = new JMenu("Archivo");
        cargarJugarMenu = new JMenu("Jugar");

        cargarGuardarMenuItem = new JMenuItem("Guardar");
        cargarJugarMenuItem = new JMenuItem("Jugar");

        cargarGuardarMenuItem.addActionListener(this);
        cargarJugarMenuItem.addActionListener(this);

        cargarArchivoMenu.add(cargarGuardarMenuItem);
        cargarJugarMenu.add(cargarJugarMenuItem);

        cargarMenuBar.add(cargarArchivoMenu);
        cargarMenuBar.add(cargarJugarMenu);

        this.setSize(1000, 600);
        this.setTitle("cargar");
        this.setVisible(true);
        this.setResizable(false);

        this.setJMenuBar(cargarMenuBar);

        this.tablero = tablero;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == cargarJugarMenuItem) {

            this.dispose();

            JRootPane rootPane = this.getRootPane();
            Component[] components = rootPane.getContentPane().getComponents();
            JPanel mainPanel = (JPanel) components[0];
            Component[] buttons = mainPanel.getComponents();

            this.getContentPane().removeAll();
            this.revalidate();
            this.repaint();

            int filas = mainPanel.getComponentCount() / ((GridLayout) mainPanel.getLayout()).getColumns();
            int columnas = ((GridLayout) mainPanel.getLayout()).getColumns();

            char[][] fichas = new char[filas][columnas];

            for (int i = 0; i < filas * columnas; i++) {
                JButton button = (JButton) buttons[i];
                button.removeActionListener(button.getActionListeners()[0]);

                int row = i / columnas;
                int column = i % columnas;
                if (buttons[i].getBackground() == Color.RED) {
                    fichas[row][column] = 'R';
                } else if (buttons[i].getBackground() == Color.BLUE) {
                    fichas[row][column] = 'A';
                } else if (buttons[i].getBackground() == Color.GREEN) {
                    fichas[row][column] = 'V';
                }
            }

            ArrayList<Movimiento> movimientos = new ArrayList<>();
            this.tablero = new Tablero(fichas);
            JugarGUI jugarJFrame = new JugarGUI(this.tablero, movimientos, buttons);
            jugarJFrame.add(mainPanel);
            for (int i = 0; i < filas * columnas; i++) {
                JButton button = (JButton) buttons[i];
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        JButton actionedButton = (JButton) e.getSource();
                        JPanel jugarPanel = (JPanel) jugarJFrame.getContentPane().getComponents()[0];
                        Component[] buttons = jugarPanel.getComponents();

                        Color color = new Color(238, 238, 238);
                        if (actionedButton.getBackground().equals(color)) {
                            return;
                        }

                        int index = -1;
                        for (int i = 0; i < buttons.length; i++) {
                            if ((JButton) buttons[i] == actionedButton) {
                                index = i;
                                break;
                            }
                        }

                        if (index == -1) {
                            JOptionPane.showMessageDialog(null, "Error al obtener el botón pulsado.");
                            return; // no debería pasar
                        }

                        if (jugarJFrame.getDeshacer()) {
                            jugarJFrame.setDeshacer(false);
                            tablero = new Tablero(jugarJFrame.getTablero());
                        } else if (jugarJFrame.getRehacer()) {
                            jugarJFrame.setRehacer(false);
                            tablero = new Tablero(jugarJFrame.getTablero());
                        }

                        int fila = index / columnas;
                        int columna = index % columnas;

                        boolean[][] grupoFichas = new boolean[filas][columnas];
                        tablero.encontrarGrupo(grupoFichas, fila, columna, tablero.getFichas()[fila][columna]);
                        outerLoop: for (int i = filas - 1; i >= 0; i--) {
                            for (int j = 0; j < columnas; j++) {
                                if (grupoFichas[i][j]) {
                                    fila = i;
                                    columna = j;
                                    break outerLoop;
                                }

                            }
                        }

                        int oldNumFichas = tablero.getNumFichas();

                        Tablero copiaTablero = new Tablero(tablero);

                        jugarJFrame.pushPrevTablero(new Tablero(tablero));
                        int numFichasEliminadas = tablero.realizarMovimiento(fila, columna);

                        int newNumFichas = tablero.getNumFichas();

                        if (oldNumFichas == newNumFichas) {
                            JOptionPane.showMessageDialog(null,
                                    "No se puede destruir un grupo con una sola ficha.");
                            return;
                        }

                        Movimiento movimiento = new Movimiento(fila, columna,
                                copiaTablero.getFichas()[fila][columna],
                                numFichasEliminadas);
                        movimientos.add(movimiento);

                        JOptionPane.showMessageDialog(null,
                                "Movimiento " + movimientos.size() + " en (" + (filas - fila) + ", " + (columna + 1)
                                        + "): eliminó " + movimiento.getNumFichasEliminadas() + " fichas de color "
                                        + movimiento.getColor() + " y obtuvo " + movimiento.getScore()
                                        + " puntos.");

                        for (int i = 0; i < filas; i++) {
                            for (int j = 0; j < columnas; j++) {
                                if (tablero.getFichas()[i][j] == 'R') {
                                    ((JButton) buttons[i * columnas + j]).setBackground(Color.RED);
                                } else if (tablero.getFichas()[i][j] == 'A') {
                                    ((JButton) buttons[i * columnas + j]).setBackground(Color.BLUE);
                                } else if (tablero.getFichas()[i][j] == 'V') {
                                    ((JButton) buttons[i * columnas + j]).setBackground(Color.GREEN);
                                } else {
                                    ((JButton) buttons[i * columnas + j]).setBackground(null);
                                }
                            }
                        }

                        jugarJFrame.revalidate();
                        jugarJFrame.repaint();

                        if (tablero.fin()) {
                            JOptionPane.showMessageDialog(null, "Fin del juego.");
                            jugarJFrame.setJuegoTerminado(true);
                            for (int i = 0; i < filas * columnas; i++) {
                                JButton button = (JButton) buttons[i];
                                button.removeActionListener(button.getActionListeners()[0]);
                            }
                            return;
                        }

                    }
                });
            }

            jugarJFrame.setSize(1000, 600);
            jugarJFrame.setVisible(true);
            jugarJFrame.setResizable(false);

        } else if (e.getSource() == cargarGuardarMenuItem) {

            JRootPane rootPane = this.getRootPane();
            Component[] components = rootPane.getContentPane().getComponents();
            JPanel mainPanel = (JPanel) components[0];
            Component[] buttons = mainPanel.getComponents();
            for (Component button : buttons) {
                if (button.getBackground() != Color.RED && button.getBackground() != Color.BLUE
                        && button.getBackground() != Color.GREEN) {
                    JOptionPane.showMessageDialog(null, "No se puede guardar un tablero sin completar");
                    break;
                }
            }

            JFileChooser fileChooser = new JFileChooser(new File("juegos/"));
            int response = fileChooser.showSaveDialog(null);

            if (response == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    FileWriter writer = new FileWriter(file);
                    writer.write("1\n\n");

                    GridLayout layout = (GridLayout) mainPanel.getLayout();
                    int columns = layout.getColumns();
                    int currentRow = 0;

                    for (int i = 0; i < buttons.length; i++) {

                        int row = i / columns;

                        if (row != currentRow) {
                            writer.write("\n");
                            currentRow = row;
                        }

                        if (buttons[i].getBackground() == Color.RED) {
                            writer.write("R");
                        } else if (buttons[i].getBackground() == Color.BLUE) {
                            writer.write("A");
                        } else if (buttons[i].getBackground() == Color.GREEN) {
                            writer.write("V");
                        }
                    }

                    writer.close();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "Error al guardar en juego.");
                }
            }

        }
    }
}
