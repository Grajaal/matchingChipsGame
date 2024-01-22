import java.util.ArrayList;

public class FichasIguales {

    private ArrayList<Tablero> juegos;
    private ArrayList<ArrayList<Movimiento>> movimientosSoluciones;
    private Tablero tableroSolucion;
    int numJuegos;

    public FichasIguales(ArrayList<Tablero> juegos) {
        this.movimientosSoluciones = new ArrayList<>();
        this.juegos = juegos;
        this.numJuegos = juegos.size();
    }

    public void jugar() {
        for (int i = 1; i <= this.numJuegos; i++) {
            this.tableroSolucion = new Tablero(juegos.get(i - 1));
            movimientosSoluciones.add(new ArrayList<>());
            System.out.println("Juego " + i + ":");
            buscarMejorMovimiento(juegos.get(i - 1), new ArrayList<>());
            int nSoluciones = this.movimientosSoluciones.size();

            for (int j = 1; j <= this.movimientosSoluciones.size(); j++) {
                System.out.println("Solución " + j + " de " + nSoluciones + ":");
                for (int k = 1; k <= this.movimientosSoluciones.get(j - 1).size(); k++) {

                    String fichasFormat, puntosFormat;
                    if (this.movimientosSoluciones.get(j - 1).get(k - 1).getNumFichasEliminadas() == 1)
                        fichasFormat = "ficha";
                    else
                        fichasFormat = "fichas";

                    if (this.movimientosSoluciones.get(j - 1).get(k - 1).getScore() == 1)
                        puntosFormat = "punto";
                    else
                        puntosFormat = "puntos";

                    System.out.println("Movimiento " + k + " en (" +
                            (this.tableroSolucion.getFilas()
                                    - this.movimientosSoluciones.get(j - 1).get(k - 1).getFila())
                            + ", " +
                            (this.movimientosSoluciones.get(j - 1).get(k - 1).getColumna() + 1) +
                            "): eliminó " + this.movimientosSoluciones.get(j - 1).get(k - 1).getNumFichasEliminadas()
                            + " "
                            + fichasFormat +
                            " de color " + this.movimientosSoluciones.get(j - 1).get(k - 1).getColor() +
                            " y obtuvo " + this.movimientosSoluciones.get(j - 1).get(k - 1).getScore() + " "
                            + puntosFormat
                            + ".");
                }

                String fichasFormat;
                if (this.tableroSolucion.getNumFichas() == 1)
                    fichasFormat = "ficha";
                else
                    fichasFormat = "fichas";

                System.out.println("Puntuación final: "
                        + calcularScore(this.tableroSolucion, this.movimientosSoluciones.get(j - 1))
                        + ", quedando " + this.tableroSolucion.getNumFichas() + " " + fichasFormat
                        + ".");

            }
            if (i != this.numJuegos)
                System.out.println();

        }
    }

    public void buscarMejorMovimiento(Tablero tablero, ArrayList<Movimiento> movimientos) {
        if (tablero.fin()) {
            int score = calcularScore(tablero, movimientos);
            int bestScore = calcularScore(this.tableroSolucion, this.movimientosSoluciones.get(0));
            if (bestScore == 0 || score > bestScore) {
                if (movimientosSoluciones.size() > 0)
                    movimientosSoluciones.removeAll(movimientosSoluciones);
                movimientosSoluciones.add(new ArrayList<>(movimientos));
                tableroSolucion = new Tablero(tablero);
            }
            if (score == bestScore) {
                movimientosSoluciones.add(new ArrayList<>(movimientos));
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
