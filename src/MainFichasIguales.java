public class MainFichasIguales {
    public static void main(String[] args) throws Exception {

        GUI gui = new GUI();
        FichasIgualesUI uiText = new FichasIgualesUI();
        Tablero juego = uiText.init(gui);
        FichasIguales fichasIguales = new FichasIguales(juego);
        fichasIguales.jugar();

    }
}
