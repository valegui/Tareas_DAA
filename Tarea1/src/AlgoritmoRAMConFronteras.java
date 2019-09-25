import java.util.Arrays;

public class AlgoritmoRAMConFronteras {
    private String x;
    private String y;
    private int largo_x;
    private int largo_y;
    private int[] ant; // Fila anterior, se usa mientras corre el algoritmo | Anterior al iniciar algoritmo, se reemplaza por resultado de frontera inferior al finalizar algoritmo
    private int[] columna_lateral; // Columna anterior al iniciar algoritmo, se reemplaza por reultado de frontera derecha al terminar

    public AlgoritmoRAMConFronteras(String x, String y, int[] columnaIzquierda, int[] filaSuperior_Y) throws Exception{
        this.x = x;
        this.y = y;
        this.largo_x = x.length();
        this.largo_y = y.length();
        this.ant = filaSuperior_Y;
        this.columna_lateral = columnaIzquierda;
        int l, p;
        // Verificación de largos de filas proporcionadas y largos de Strings
        if(largo_x != (l = columnaIzquierda.length)){
            System.out.println("Frontera columna izquierda anterior administrada no coincide con el largo de string X: " + largo_x + " (largo real) vs " + l);
            throw new Exception();
        }
        if(largo_y + 1 != (p = filaSuperior_Y.length)){ // +1 porque contiene el valor de la diagonal
            System.out.println("Frontera superior anterior administrada no coincide con el largo de string Y: " + largo_y + " (largo real) vs " + p);
            throw new Exception();
        }

    }

    /**
     * Algoritmo principal: Itera fila por fila y retorna fronteras
     * @return En 0: Frontera inferior, 1: Frontera derecha
     */
    public int[][] calcDist(){
        // Computa para cada caracter de X
        for (int i = 0; i < largo_x; i++){
            computarFila(i);
        }
        // En ant queda la ultima fila y retorna esta como frontera superior, y
        System.out.println(Arrays.toString(ant));
        System.out.println(Arrays.toString(columna_lateral));
        return new int[][] {ant, columna_lateral};
    }

    /**
     * Computa una fila basado en los valores de 'ant' y columna lateral.
     * También actualiza el valore columna lateral de la mismafila por sú último valor recién calculado, para retornar al final
     * @param fila : fila actual
     */
    public void computarFila(int fila) {
        char s = x.charAt(fila); // caracter de x
        char z; // caracter de y actual

        // VALORES
        int NW = 1; // North West (diagonal) | 1 hasta que se diga lo contrario
        int NW_val;
        int W_val;
        int N_val;

        int [] nueva = new int[largo_y + 1]; // nueva Fila
        nueva[0] = columna_lateral[fila]; // La frontera lateral tiene tantos valores como caracteres de X; no repite el valor superior que ya contiene la primera fila

        // Recorremos la nueva fila
        for(int j = 1; j <= largo_y; j++){
            z = y.charAt(j-1); // caracter de Y

            NW = 1; // Reset de North West
            if(s == z) // Si son el mismo caracter, diagonal vale 0, sino 1
                NW = 0;
            NW_val = ant[j-1] + NW;
            N_val = ant[j] + 1;
            W_val = nueva[j-1] + 1;

            nueva[j] = Math.min(NW_val, Math.min(N_val, W_val));
        }
        //System.out.println(Arrays.toString(nueva)); // ultima fila
        //System.out.println(nueva[largo_y]);
        // Guardamos el ultimo valor en el array que ya no vamos a usar, generamos la frontera derecha, (izquierda) para siguiente iteracion
        columna_lateral[fila] = nueva[largo_y];

        // Nueva fila pasa a ser la anterior
        System.arraycopy(nueva, 0, ant, 0, largo_y + 1); // +1 porque guarda el diagonal

    }

    public static void main(String... args) throws Exception {
        AlgoritmoRAMConFronteras a = new AlgoritmoRAMConFronteras("sunday","saturday", new int[] {1,2,3,4,5,6}, new int[] {0,1,2,3,4,5,6,7,8});
        a.calcDist();
        //System.out.println(Arrays.toString(a.calcDist()[0])); // ultima fila
        //System.out.println(Arrays.toString(a.calcDist()[1])); // ultima columna
    }


}
