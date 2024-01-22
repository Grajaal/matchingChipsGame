import java.util.ArrayList;

public class MainFichasIguales {
    public static void main(String[] args) throws Exception {
        FichasIgualesUI ui = new FichasIgualesUI();
        ArrayList<Tablero> juegos = ui.init();
        FichasIguales fichasIguales = new FichasIguales(juegos);
        fichasIguales.jugar();

    }
}
