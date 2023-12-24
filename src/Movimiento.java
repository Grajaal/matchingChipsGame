public class Movimiento {
    private int fila; 
    private int columna;
    private char color; 
    private int numFichasEliminadas; 
    private int score;
    

    public Movimiento(int fila, int columna, char color, int numFichasEliminadas){
        this.fila = fila; 
        this.columna = columna; 
        this.color = color; 
        this.numFichasEliminadas = numFichasEliminadas; 
        this.score = calcularScore(); 
    }

    public int getFila(){
        return this.fila; 
    }

    public int getColumna(){
        return this.columna; 
    }

    public char getColor(){
        return this.color; 
    }

    public int getNumFichasEliminadas(){
        return this.numFichasEliminadas; 
    }

    public int getScore(){
        return this.score; 
    }

    public int calcularScore(){
        return (int) Math.pow(this.numFichasEliminadas - 2, 2); 
    }
}
