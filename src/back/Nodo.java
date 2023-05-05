
package back;

public class Nodo {
    public int Fila;
    public int columna;
    public int Distancia;
    public  boolean Visto;
    public Nodo Anterior;

    public Nodo(int row, int col) {
        this.Fila = row;
        this.columna = col;
        this.Distancia = Integer.MAX_VALUE;
        this.Visto = false;

    }
    
}
