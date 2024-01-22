import java.util.ArrayList;
import java.util.Scanner;

public class FichasIgualesUI {
    public ArrayList<Tablero> init() {
        Scanner scanner = new Scanner(System.in);

        // Lee el número de juegos que el jugador quiere jugar.
        int numJuegos = Integer.parseInt(scanner.nextLine());
        if (numJuegos < 1)
            System.exit(0);
        if (scanner.nextLine().length() != 0)
            System.exit(0);

        String fila;
        int columnasObjetivo;
        ArrayList<ArrayList<String>> entradasUsuario = new ArrayList<>();
        for (int i = 0; i < numJuegos; i++)
            entradasUsuario.add(new ArrayList<>());
        ArrayList<Tablero> juegos = new ArrayList<>();

        boolean isCorrect = true;
        for (int i = 0; i < numJuegos; i++) {
            if (isCorrect) {
                fila = scanner.nextLine();
                if (!coloresCorrectos(fila))
                    isCorrect = false;
                columnasObjetivo = fila.length();
                if (columnasObjetivo > 20 || columnasObjetivo == 0)
                    isCorrect = false;
                entradasUsuario.get(i).add(fila);

                while (scanner.hasNextLine() && !(fila = scanner.nextLine()).isEmpty()) {
                    if (isCorrect && fila.length() != columnasObjetivo || !coloresCorrectos(fila))
                        isCorrect = false;

                    entradasUsuario.get(i).add(fila);
                }
            } else {
                while (scanner.hasNextLine() && !(fila = scanner.nextLine()).isEmpty()) {
                }
            }

            if (isCorrect)
                juegos.add(new Tablero(convertirArrayChar(entradasUsuario.get(i))));
        }
        scanner.close();
        return juegos;
    }

    public boolean coloresCorrectos(String fila) {
        for (char color : fila.toCharArray()) {
            if (color != 'A' && color != 'V' && color != 'R')
                return false;
        }
        return true;
    }

    public char[][] convertirArrayChar(ArrayList<String> entradaUsuario) {
        int filas = entradaUsuario.size();
        int columnas = entradaUsuario.get(0).length();
        char[][] tablero = new char[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                tablero[i][j] = entradaUsuario.get(i).charAt(j);
            }
        }

        return tablero;
    }
}
