import java.util.ArrayList;

public class MainFichasIguales {
    public static void main(String[] args) throws Exception {

        new GUI();
        FichasIgualesUI uiText = new FichasIgualesUI();
        ArrayList<Tablero> juegos = uiText.init();
        FichasIguales fichasIguales = new FichasIguales(juegos);
        fichasIguales.jugar();

    }
}
