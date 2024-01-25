import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GUI extends JFrame implements ActionListener {

    private JMenuBar menuBar;
    private JMenu archivoMenu;
    private JMenuItem crearMenuItem, cargarMenuItem;

    private Tablero tablero;

    public GUI() {

        menuBar = new JMenuBar();

        archivoMenu = new JMenu("Archivo");

        crearMenuItem = new JMenuItem("Crear");
        cargarMenuItem = new JMenuItem("Cargar");

        crearMenuItem.addActionListener(this);
        cargarMenuItem.addActionListener(this);

        archivoMenu.add(crearMenuItem);
        archivoMenu.add(cargarMenuItem);

        menuBar.add(archivoMenu);

        this.setSize(1200, 800);
        this.setTitle("Fichas Iguales");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        this.setJMenuBar(menuBar);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == crearMenuItem) {

            this.getContentPane().removeAll();

            CrearGUI crearJFrame = new CrearGUI();

            String filasString = JOptionPane.showInputDialog("Introduce el número de filas de tu tablero: ");
            String columnasString = JOptionPane.showInputDialog("Introduce el número de columnas de tu tablero: ");
            int filas, columnas;

            try {
                filas = Integer.parseInt(filasString);
                columnas = Integer.parseInt(columnasString);
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(null, "Introduce números enteros.");
                crearJFrame.dispose();
                return;
            }

            if (filas < 1 || columnas < 1) {
                JOptionPane.showMessageDialog(null, "Introduce números positivos.");
                crearJFrame.dispose();
                return;
            }

            JPanel mainPanel = new JPanel(new GridLayout(filas, columnas));

            for (int i = 0; i < filas * columnas; i++) {
                JButton button = new JButton();
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        crearJFrame.pushPrevButton(button);
                        if (button.getBackground() == Color.RED) {
                            button.setBackground(Color.BLUE);
                        } else if (button.getBackground() == Color.BLUE) {
                            button.setBackground(Color.GREEN);
                        } else if (button.getBackground() == Color.GREEN) {
                            button.setBackground(null);
                        } else {
                            button.setBackground(Color.RED);
                        }
                    }
                });
                mainPanel.add(button);
            }

            crearJFrame.add(mainPanel);
            crearJFrame.revalidate();
            crearJFrame.repaint();

            crearJFrame.setSize(1000, 600);
            crearJFrame.setVisible(true);
            crearJFrame.setResizable(false);

            this.revalidate();
            this.repaint();

        } else if (e.getSource() == cargarMenuItem) {

            this.getContentPane().removeAll();

            CargarGUI cargarJFrame = new CargarGUI(this.tablero);

            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setCurrentDirectory(new File("juegos/"));
            int response = fileChooser.showOpenDialog(null);

            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(file.getAbsolutePath()));

                    String line;

                    line = br.readLine();
                    if (line.matches("\\d+")) { // comprueba si la línea es un número entero
                        int number = Integer.parseInt(line); // convierte la línea a un número
                        if (number < 1) { // comprueba si el número es positivo
                            JOptionPane.showMessageDialog(null, "El número de juegos es incorrecto.");
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "El formato del juego es incorrecto.");
                        return;
                    }
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

                    cargarJFrame.add(mainPanel);
                    this.revalidate();
                    this.repaint();

                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(null, "Error al cargar el juego.");
                }

                cargarJFrame.setSize(1000, 600);
                cargarJFrame.setVisible(true);
                cargarJFrame.setResizable(false);

            }

        }
    }
}
