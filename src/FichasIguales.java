import java.util.ArrayList;

public class FichasIguales {

    private Tablero[] juegos;
    private ArrayList<Movimiento> movimientosSolucion;
    private Tablero tableroSolucion;
    int numJuegos;

    public FichasIguales(Tablero[] juegos) {
        this.juegos = juegos;
        this.numJuegos = juegos.length;
    }

    public void jugar() {
        for (int i = 1; i <= this.numJuegos; i++) {
            this.tableroSolucion = new Tablero(juegos[i - 1]);
            this.movimientosSolucion = new ArrayList<>();
            System.out.println("Juego " + i + ":");
            buscarMejorMovimiento(this.juegos[i - 1], new ArrayList<>());
            for (int j = 1; j <= this.movimientosSolucion.size(); j++) {
                String fichasFormat, puntosFormat;
                if (this.movimientosSolucion.get(j - 1).getNumFichasEliminadas() == 1)
                    fichasFormat = "ficha";
                else
                    fichasFormat = "fichas";

                if (this.movimientosSolucion.get(j - 1).getScore() == 1)
                    puntosFormat = "punto";
                else
                    puntosFormat = "puntos";
                System.out.println("Movimiento " + j + " en (" +
                        (this.tableroSolucion.getFilas() - this.movimientosSolucion.get(j - 1).getFila()) + ", " +
                        (this.movimientosSolucion.get(j - 1).getColumna() + 1) +
                        "): eliminó " + this.movimientosSolucion.get(j - 1).getNumFichasEliminadas() + " "
                        + fichasFormat +
                        " de color " + this.movimientosSolucion.get(j - 1).getColor() +
                        " y obtuvo " + this.movimientosSolucion.get(j - 1).getScore() + " " + puntosFormat + ".");
            }
            String fichasFormat;
            if (this.tableroSolucion.getNumFichas() == 1)
                fichasFormat = "ficha";
            else
                fichasFormat = "fichas";
            System.out.println("Puntuación final: " + calcularScore(this.tableroSolucion, this.movimientosSolucion)
                    + ", quedando " + this.tableroSolucion.getNumFichas() + " " + fichasFormat + ".");
            if (i != this.numJuegos)
                System.out.println();
        }
    }

    public void buscarMejorMovimiento(Tablero tablero, ArrayList<Movimiento> movimientos) {
        if (tablero.fin()) {
            int score = calcularScore(tablero, movimientos);
            int bestScore = calcularScore(this.tableroSolucion, this.movimientosSolucion);
            if (bestScore == 0 || score > bestScore) {
                movimientosSolucion = new ArrayList<>(movimientos);
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

    // public void setTablero(Tablero origenTablero, Tablero nuevoTablero){
    // nuevoTablero.setFichas(origenTablero.copyOf().getFichas());
    // nuevoTablero.setScore(origenTablero.getScore());
    // nuevoTablero.setNumFichas(origenTablero.getNumFichas());
    // }
}
