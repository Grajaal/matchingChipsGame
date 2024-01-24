import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

public class GUI extends JFrame implements ActionListener {

    JMenuBar menuBar;
    JMenu archivoMenu, jugarMenu, ayudaMenu, edicionMenu;
    JMenuItem crearMenuItem, guardarMenuItem, cargarMenuItem, deshacerMenuItem, rehacerMenuItem, jugarMenuItem;

    boolean cargarButton;

    public GUI() {

        menuBar = new JMenuBar();

        archivoMenu = new JMenu("Archivo");
        jugarMenu = new JMenu("Jugar");
        ayudaMenu = new JMenu("Ayuda");
        edicionMenu = new JMenu("Edicion");

        crearMenuItem = new JMenuItem("Crear");
        guardarMenuItem = new JMenuItem("Guardar");
        cargarMenuItem = new JMenuItem("Cargar");
        deshacerMenuItem = new JMenuItem("Deshacer");
        rehacerMenuItem = new JMenuItem("Rehacer");
        jugarMenuItem = new JMenuItem("Jugar");

        crearMenuItem.addActionListener(this);
        guardarMenuItem.addActionListener(this);
        cargarMenuItem.addActionListener(this);
        deshacerMenuItem.addActionListener(this);
        rehacerMenuItem.addActionListener(this);
        jugarMenuItem.addActionListener(this);

        archivoMenu.add(crearMenuItem);
        archivoMenu.add(guardarMenuItem);
        archivoMenu.add(cargarMenuItem);
        edicionMenu.add(deshacerMenuItem);
        edicionMenu.add(rehacerMenuItem);
        jugarMenu.add(jugarMenuItem);

        menuBar.add(archivoMenu);
        menuBar.add(edicionMenu);
        menuBar.add(jugarMenu);
        menuBar.add(ayudaMenu);

        this.setSize(1200, 800);
        this.setTitle("Fichas Iguales");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        this.setJMenuBar(menuBar);

        cargarButton = false;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == crearMenuItem) {

            this.cargarButton = false;

            this.getContentPane().removeAll();

            int filas = Integer.parseInt(JOptionPane.showInputDialog("Introduce el número de filas de tu tablero: "));
            int columnas = Integer
                    .parseInt(JOptionPane.showInputDialog("Introduce el número de columnas de tu tablero: "));

            JPanel mainPanel = new JPanel(new GridLayout(filas, columnas));

            for (int i = 0; i < filas * columnas; i++) {
                JButton button = new JButton();
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (button.getBackground() == Color.RED) {
                            button.setBackground(Color.BLUE);
                        } else if (button.getBackground() == Color.BLUE) {
                            button.setBackground(Color.GREEN);
                        } else {
                            button.setBackground(Color.RED);
                        }
                    }
                });
                mainPanel.add(button);
            }

            this.add(mainPanel);
            this.revalidate();
            this.repaint();

        } else if (e.getSource() == guardarMenuItem) {

            this.cargarButton = false;

            JRootPane rootPane = this.getRootPane();
            Component[] components = rootPane.getContentPane().getComponents();
            JPanel mainPanel = (JPanel) components[0];
            Component[] buttons = mainPanel.getComponents();
            for (Component button : buttons) {
                System.out.println(button.getBackground());
                if (button.getBackground() != Color.RED && button.getBackground() != Color.BLUE
                        && button.getBackground() != Color.GREEN) {
                    JOptionPane.showMessageDialog(null, "No se puede guardar un tablero sin completar");
                    break;
                }
            }

            String nombre = JOptionPane.showInputDialog("Introduce el nombre del juego: ");
            File file = new File("juegos/" + nombre + ".txt");

            try {
                if (file.createNewFile()) {
                    PrintWriter writer = new PrintWriter(file);
                    writer.println(1);
                    writer.println();

                    GridLayout layout = (GridLayout) mainPanel.getLayout();
                    int columns = layout.getColumns();
                    int currentRow = 0;

                    for (int i = 0; i < buttons.length; i++) {

                        int row = i / columns;

                        if (row != currentRow) {
                            writer.println();
                            currentRow = row;
                        }

                        if (buttons[i].getBackground() == Color.RED) {
                            writer.print("R");
                        } else if (buttons[i].getBackground() == Color.BLUE) {
                            writer.print("A");
                        } else if (buttons[i].getBackground() == Color.GREEN) {
                            writer.print("V");
                        }
                    }

                    writer.close();
                } else {
                    JOptionPane.showMessageDialog(null, "Ya existe un juego con ese nombre. Introduce otro nombre.");
                }
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, "Error al guardar en juego.");
            }

        } else if (e.getSource() == cargarMenuItem) {

            this.getContentPane().removeAll();

            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setCurrentDirectory(new File("juegos/"));
            int response = fileChooser.showOpenDialog(null);

            if (response == JFileChooser.APPROVE_OPTION) {
                this.cargarButton = true;
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(file.getAbsolutePath()));

                    String line;

                    br.readLine();
                    br.readLine();

                    int filas = 1, columnas;

                    line = br.readLine();
                    columnas = line.length();

                    while ((line = br.readLine()) != null) {
                        filas++;
                    }

                    br.close();
                    br = new BufferedReader(new FileReader(file.getAbsolutePath()));
                    br.readLine();
                    br.readLine();

                    JPanel mainPanel = new JPanel(new GridLayout(filas, columnas));
                    for (int i = 0; i < filas; i++) {
                        line = br.readLine();
                        for (int j = 0; j < columnas; j++) {
                            JButton button = new JButton();
                            if (line.charAt(j) == 'R') {
                                button.setBackground(Color.RED);
                            } else if (line.charAt(j) == 'A') {
                                button.setBackground(Color.BLUE);
                            } else if (line.charAt(j) == 'V') {
                                button.setBackground(Color.GREEN);
                            }
                            button.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (button.getBackground() == Color.RED) {
                                        button.setBackground(Color.BLUE);
                                    } else if (button.getBackground() == Color.BLUE) {
                                        button.setBackground(Color.GREEN);
                                    } else {
                                        button.setBackground(Color.RED);
                                    }
                                }
                            });
                            mainPanel.add(button);
                        }
                    }

                    this.add(mainPanel);
                    this.revalidate();
                    this.repaint();

                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(null, "Error al cargar el juego.");
                }
            }

        } else if (e.getSource() == deshacerMenuItem) {
            System.out.println("Deshacer");
        } else if (e.getSource() == rehacerMenuItem) {
            System.out.println("Rehacer");
        } else if (e.getSource() == jugarMenuItem) {

            if (this.cargarButton) {

                JFrame jugarJFrame = new JFrame("Jugar");

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

                jugarJFrame.add(mainPanel);

                ArrayList<Movimiento> movimientos = new ArrayList<>();
                Tablero tablero = new Tablero(fichas);
                for (int i = 0; i < filas * columnas; i++) {
                    JButton button = (JButton) buttons[i];
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            JButton actionedButton = (JButton) e.getSource();
                            JPanel jugarPanel = (JPanel) jugarJFrame.getContentPane().getComponents()[0];
                            Component[] buttons = jugarPanel.getComponents();

                            int index = -1;
                            for (int i = 0; i < buttons.length; i++) {
                                if ((JButton) buttons[i] == actionedButton) {
                                    index = i;
                                    break;
                                }
                            }

                            if (index == -1) {
                                JOptionPane.showMessageDialog(null, "Error al obtener el botón pulsado.");
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
                            int numFichasEliminadas = tablero.realizarMovimiento(fila, columna);
                            int newNumFichas = tablero.getNumFichas();

                            if (oldNumFichas == newNumFichas) {
                                JOptionPane.showMessageDialog(null,
                                        "No se puede destruir un grupo con una sola ficha.");
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

                this.cargarButton = false;
            } else {
                JOptionPane.showMessageDialog(null, "Primero carga un juego.");
            }
        }
    }

}
