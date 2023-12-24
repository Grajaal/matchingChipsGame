public class MainFichasIguales {
    public static void main(String[] args) throws Exception {
        FichasIgualesUI ui = new FichasIgualesUI(); 
        Tablero[] juegos = ui.init();
        FichasIguales fichasIguales = new FichasIguales(juegos); 
        fichasIguales.jugar(); 

    }
}
