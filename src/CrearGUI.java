

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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class CrearGUI extends JFrame implements ActionListener {

    private JMenuBar crearMenuBar;
    private JMenu crearArchivoMenu, crearEdicionMenu;
    private JMenuItem crearGuardarMenuItem, crearDeshacerMenuItem, crearRehacerMenuItem;

    private Stack<JButton> prevButtons, postButtons;

    public CrearGUI() {

        crearMenuBar = new JMenuBar();

        crearArchivoMenu = new JMenu("Archivo");
        crearEdicionMenu = new JMenu("Edicion");

        crearGuardarMenuItem = new JMenuItem("Guardar");
        crearDeshacerMenuItem = new JMenuItem("Deshacer");
        crearRehacerMenuItem = new JMenuItem("Rehacer");

        crearGuardarMenuItem.addActionListener(this);
        crearDeshacerMenuItem.addActionListener(this);
        crearRehacerMenuItem.addActionListener(this);

        crearArchivoMenu.add(crearGuardarMenuItem);
        crearEdicionMenu.add(crearDeshacerMenuItem);
        crearEdicionMenu.add(crearRehacerMenuItem);

        crearMenuBar.add(crearArchivoMenu);
        crearMenuBar.add(crearEdicionMenu);

        this.setSize(1000, 600);
        this.setTitle("crear");
        this.setVisible(true);
        this.setResizable(false);

        this.setJMenuBar(crearMenuBar);

        this.prevButtons = new Stack<>();
        this.postButtons = new Stack<>();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == crearGuardarMenuItem) {
            JRootPane rootPane = this.getRootPane();
            Component[] components = rootPane.getContentPane().getComponents();
            JPanel mainPanel = (JPanel) components[0];
            Component[] buttons = mainPanel.getComponents();
            for (Component button : buttons) {
                if (button.getBackground() != Color.RED && button.getBackground() != Color.BLUE
                        && button.getBackground() != Color.GREEN) {
                    JOptionPane.showMessageDialog(null, "No se puede guardar un tablero sin completar");
                    return;
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
        } else if (e.getSource() == crearDeshacerMenuItem) {

            if (this.prevButtons.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay movimientos para deshacer");
                return;
            }

            JButton button = this.prevButtons.pop();
            this.postButtons.push(button);
            if (button.getBackground() == Color.RED) {
                button.setBackground(null);
            } else if (button.getBackground() == Color.BLUE) {
                button.setBackground(Color.RED);
            } else if (button.getBackground() == Color.GREEN) {
                button.setBackground(Color.BLUE);
            } else {
                button.setBackground(Color.GREEN);
            }

        } else if (e.getSource() == crearRehacerMenuItem) {

            if (this.postButtons.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay movimientos para rehacer");
                return;
            }

            JButton button = this.postButtons.pop();
            this.prevButtons.push(button);
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

    }

    public void pushPrevButton(JButton button) {
        this.prevButtons.push(button);
    }
}
