import java.util.List;
import java.util.concurrent.ExecutionException;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class Resolver extends SwingWorker<ArrayList<Movimiento>, Movimiento> {

    private JugarGUI gui;
    private Component[] buttons;
    private Tablero tablero;
    private ArrayList<Movimiento> movimientos;

    public Resolver(JugarGUI gui, Component[] buttons, Tablero tablero, ArrayList<Movimiento> movimientos) {
        this.gui = gui;
        this.buttons = buttons;
        this.tablero = tablero;
        this.movimientos = movimientos;
    }

    @Override
    protected ArrayList<Movimiento> doInBackground() throws Exception {

        FichasIguales fichasIguales = new FichasIguales(tablero);
        fichasIguales.jugar();
        this.movimientos = new ArrayList<Movimiento>(fichasIguales.getMovimientosSolucion());
        int numFichas = tablero.getFilas() * tablero.getColumnas();

        for (int i = 0; i < this.movimientos.size(); i++) {
            Thread.sleep(10000 / numFichas);
            this.tablero.realizarMovimiento(this.movimientos.get(i).getFila(), this.movimientos.get(i).getColumna());
            publish(this.movimientos.get(i));
        }

        return this.movimientos;
    }

    @Override
    protected void done() {
        try {
            this.gui.procesarDatos(get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Juego terminado");
    }

    @Override
    protected void process(List<Movimiento> movs) {
        for (Movimiento lastMov : movs) {
            int movFila = lastMov.getFila();
            int movColumna = lastMov.getColumna();

            int index = -1;
            for (int i = 0; i < buttons.length; i++) {
                if ((JButton) buttons[i] == (JButton) buttons[movFila * tablero.getColumnas() + movColumna]) {
                    index = i;
                    break;
                }
            }

            if (index == -1) {
                JOptionPane.showMessageDialog(null, "Error al obtener el botón pulsado.");
            }

            JButton button = (JButton) buttons[index];
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < tablero.getFilas(); i++) {
                        for (int j = 0; j < tablero.getColumnas(); j++) {
                            if (tablero.getFichas()[i][j] == 'R') {
                                ((JButton) buttons[i * tablero.getColumnas() + j]).setBackground(Color.RED);
                            } else if (tablero.getFichas()[i][j] == 'A') {
                                ((JButton) buttons[i * tablero.getColumnas() + j]).setBackground(Color.BLUE);
                            } else if (tablero.getFichas()[i][j] == 'V') {
                                ((JButton) buttons[i * tablero.getColumnas() + j]).setBackground(Color.GREEN);
                            } else {
                                ((JButton) buttons[i * tablero.getColumnas() + j]).setBackground(null);
                            }
                        }
                    }
                }
            });
            button.doClick();
            button.removeActionListener(button.getActionListeners()[0]);

        }
    }

}
