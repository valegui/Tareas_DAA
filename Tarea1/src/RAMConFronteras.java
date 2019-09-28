import java.util.Arrays;

public class RAMConFronteras {
    
    public static class AsbtractBlock{
        int posX;
        int posY;
        int[] frontera_superior;
        int[] frontera_lateral;
        String x;
        String y;
        int[][] fronterasCalculadas;
        
        public AsbtractBlock(int posX, int posY, String x, String y, int[] frontera_lateral, int[] frontera_superior) throws Exception{
            this.posX = posX;
            this.posY = posY;
            this.frontera_superior = frontera_superior;
            this.frontera_lateral = frontera_lateral;
            this.x = x;
            this.y = y;
            fronterasCalculadas = calcDist(x, y, frontera_lateral, frontera_superior);
        }
        
    }
    
    public static int[] generarFrontera(int tamaño, int primerValor){
        int[] a = new int[tamaño];
        for(int i = 0; i < tamaño; i++){
            a[i] = primerValor;
            primerValor++;
        }
        return a;
    }

    public static int calcAllDist(String[] subStr_X, String[] subStr_Y) throws Exception{
        int total_X = subStr_X.length;
        int total_Y = subStr_Y.length;
        int cantidadCaracteres_X = 0;
        int cantidadCaracteres_Y = 0;

        AsbtractBlock[] blocks_anterior = new AsbtractBlock[total_X]; // Todos los bloques necesarios;

        // Primera iteración
        String str_X = subStr_X[0];
        String str_Y = subStr_Y[0];

        cantidadCaracteres_X += str_X.length();
        cantidadCaracteres_Y += str_Y.length();

        // Frontera izquierda
        int[] fronteraLateral = generarFrontera(cantidadCaracteres_X, 1);
        int[] fronteraSuperior = generarFrontera(cantidadCaracteres_Y + 1, 0);
        
        blocks_anterior[0] = new AsbtractBlock(0, 0, str_X, str_Y, fronteraLateral, fronteraSuperior);
        // Itero sobre filas de X
        for(int a = 1; a < total_X; a++){
            str_X = subStr_X[a]; // Saco String de A
            
            // Hago iteración especial de primera columna
            int[] izq = generarFrontera(str_X.length(), cantidadCaracteres_X + 1);
            cantidadCaracteres_X += str_X.length();

            int[] sup = blocks_anterior[0].fronterasCalculadas[0];

            blocks_anterior[0] = new AsbtractBlock(1, 0, str_X, str_Y, izq, sup); // Guardo en posición que no voy a usar mas


            // Itero sobre el resto de las columnas
            for(int j = 1; j < total_Y; j++){
                str_Y = subStr_Y[j]; // Saco nuevo substring

                // TODO: iteracion general
            }
        }
        
        
        // Resto de iteración
        



    }


    /**
     * Algoritmo principal: Itera fila por fila y retorna fronteras
     * @return En 0: Frontera inferior, 1: Frontera derecha
     */
    public static int[][] calcDist(String x, String y, int[] columnaIzquierda, int[] filaSuperior_Y) throws Exception{
        int largo_x = x.length();
        int largo_y = y.length();

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

        /***************************/
        // Computa para cada caracter de X
        for (int i = 0; i < largo_x; i++){
            computarFila(i, x, y, largo_y, filaSuperior_Y, columnaIzquierda);
        }
        // En ant queda la ultima fila y retorna esta como frontera superior, y
        System.out.println(Arrays.toString(filaSuperior_Y));
        System.out.println(Arrays.toString(columnaIzquierda));
        return new int[][] {filaSuperior_Y, columnaIzquierda};
    }

    /**
     * Computa una fila basado en los valores de 'ant' y columna lateral.
     * También actualiza el valore columna lateral de la mismafila por sú último valor recién calculado, para retornar al final
     * @param fila : fila actual
     */
    public static void computarFila(int fila, String x, String y, int largo_y, int[] ant, int[] columna_lateral) {
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
        //AlgoritmoRAMConFronteras a = new AlgoritmoRAMConFronteras("sunday","saturday", new int[] {1,2,3,4,5,6}, new int[] {0,1,2,3,4,5,6,7,8});
        calcDist("sunday","saturday", new int[] {1,2,3,4,5,6}, new int[] {0,1,2,3,4,5,6,7,8});
        //System.out.println(Arrays.toString(a.calcDist()[0])); // ultima fila
        //System.out.println(Arrays.toString(a.calcDist()[1])); // ultima columna
    }


}
