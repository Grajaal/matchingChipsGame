import java.util.ArrayList;

public class FichasIguales {

    private Tablero juego;
    private ArrayList<Movimiento> movimientosSolucion;
    private Tablero tableroSolucion;

    public FichasIguales(Tablero juego) {
        this.movimientosSolucion = new ArrayList<>();
        this.juego = juego;
    }

    public void jugar() {
        this.tableroSolucion = new Tablero(juego);
        buscarMejorMovimiento(juego, new ArrayList<>());
    }

    public void buscarMejorMovimiento(Tablero tablero, ArrayList<Movimiento> movimientos) {
        if (tablero.fin()) {
            int score = calcularScore(tablero, movimientos);
            int bestScore = calcularScore(this.tableroSolucion, this.movimientosSolucion);
            if (bestScore == 0 || score > bestScore) {
                movimientosSolucion = new ArrayList<Movimiento>(movimientos);
                tableroSolucion = new Tablero(tablero);
            }
        }

        for (Movimiento movimiento : obtenerPosiblesMovimientos(tablero)) {
            Tablero copiaTablero = new Tablero(tablero);
            copiaTablero.realizarMovimiento(movimiento.getFila(), movimiento.getColumna());
            movimientos.add(movimiento);
            buscarMejorMovimiento(copiaTablero, movimientos);
            movimientos.remove(movimiento);
        }
    }

    public ArrayList<Movimiento> obtenerPosiblesMovimientos(Tablero tablero) {
        ArrayList<Movimiento> movimientos = new ArrayList<>();
        boolean grupoFichas[][] = new boolean[tablero.getFilas()][tablero.getColumnas()];

        for (int i = tablero.getFilas() - 1; i >= 0; i--) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                if (tablero.getFichas()[i][j] != ' ') {
                    char color = tablero.getFichas()[i][j];
                    int numFichasMismoGrupo = new Tablero(tablero).realizarMovimiento(i, j);
                    if (numFichasMismoGrupo > 1 && grupoFichas[i][j] == false) {
                        movimientos.add(new Movimiento(i, j, color, numFichasMismoGrupo));
                        tablero.encontrarGrupo(grupoFichas, i, j, color);
                    }
                }
            }
        }

        return movimientos;
    }

    public int calcularScore(Tablero tablero, ArrayList<Movimiento> movimientos) {
        int score = 0;
        for (Movimiento movimiento : movimientos) {
            score += movimiento.calcularScore();
        }
        if (tablero.getNumFichas() == 0)
            score += 1000;
        return score;
    }

    public Tablero getTableroSolucion() {

        return this.tableroSolucion;
    }

    public ArrayList<Movimiento> getMovimientosSolucion() {
        return this.movimientosSolucion;
    }

}
