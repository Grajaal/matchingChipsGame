import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == crearMenuItem) {
            int filas = Integer.parseInt(JOptionPane.showInputDialog("Introduce el número de filas de tu tablero: "));
            int columnas = Integer
                    .parseInt(JOptionPane.showInputDialog("Introduce el número de columnas de tu tablero: "));

            JPanel mainPanel = new JPanel(new GridLayout(filas, columnas));
            Random rand = new Random();

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

        } else if (e.getSource() == cargarMenuItem) {
            System.out.println("Cargar");
        } else if (e.getSource() == deshacerMenuItem) {
            System.out.println("Deshacer");
        } else if (e.getSource() == rehacerMenuItem) {
            System.out.println("Rehacer");
        } else if (e.getSource() == jugarMenuItem) {
            System.out.println("Jugar");
        }
    }

}
