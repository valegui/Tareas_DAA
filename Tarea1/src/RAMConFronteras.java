import java.io.*;
import java.util.Arrays;

public class RAMConFronteras {
    public static int B = 1024;
    public static int SIZE_OF_INT = 4;
    private int I;
    private int O;
    private int f;
    private int N;
    private String X;
    private String Y;
    private String dir_x;
    private String dir_y;
    private String dir_output;
    private int[] newFrontierRow;
    private int[] newFrontierColumn;
    private int[] previousFrontierRow;
    private int[] previousFrontierColumn;


    public RAMConFronteras(String dir_x, String dir_y, String dir_output, int m, int N){
        this.I = 0;
        this.O = 0;
        this.N = N;
        this.dir_x = dir_x;
        this.dir_y = dir_y;
        this.dir_output = dir_output;
        this.f = m/20;
        this.newFrontierColumn = new int[f*B];
        this.newFrontierRow = new int[f*B];
        this.previousFrontierColumn = new int[f*B];
        this.previousFrontierRow = new int[f*B];
        this.X = "";
        this.Y = "";
    }
    
    public static int[] generarFrontera(int tamaño, int primerValor) {
        int[] a = new int[tamaño];
        for (int i = 0; i < tamaño; i++) {
            a[i] = primerValor;
            primerValor++;
        }
        return a;
    }

    public static int calcAllDist(String[] subStr_X, String[] subStr_Y) throws Exception{
        int total_X = subStr_X.length;int total_Y = subStr_Y.length; // Cantidad de archivos
        int[][][] frontiersInFiles = new int[total_Y][][]; // Todos los archivos de fronteras necesarios
        int cantidadCaracteres_X = 0, cantidadCaracteres_Y = 0; // Contador para generar fronteras superior e izquierdas en primera columna y primera fila
        String str_X, str_Y; // Substrings actuales
        int[] izq, sup; // Fronteras actuales

        // Itero sobre filas de X
        for(int a = 0; a < total_X; a++){
            // TODO: Auí leer archivo a-ésimo de X
            str_X = subStr_X[a]; // Saco String de A


            // Itero sobre columnas de Y
            for(int j = 0; j < total_Y; j++){
                // TODO: Aquí leer archivo j-ésimo de Y
                str_Y = subStr_Y[j]; // Saco nuevo substring


                // Primera columna es especial para X: generar frontera lateral de X
                if( j == 0){
                    izq = generarFrontera(str_X.length(), cantidadCaracteres_X + 1);
                    cantidadCaracteres_X += str_X.length();

                }else{ // Sino, leer frontera lateral
                    izq = frontiersInFiles[j-1][1];
                    // TODO: Aquí leer frontera lateral de archivo
                }

                // En primera fila se generan fronteras superiores
                if( a == 0){
                    sup = generarFrontera(str_Y.length() + 1, cantidadCaracteres_Y); // Empieza desde el largo anterior
                    cantidadCaracteres_Y += str_Y.length();

                }else{ // Sino leer frontera superior
                    sup = frontiersInFiles[j][0];
                    // TODO: Aquí leer la frontera superior de archivo
                }

                // En este punto ya tengo ambas fronteras y strings definidas, calculo nuevas fronteras y sobreescribo
                frontiersInFiles[j] = calcDist(str_X, str_Y, izq, sup); // Guardo en posición que no voy a usar mas
                // TODO: Aquí guardar ambas fronteras en el archivo j-ésimo
            }
        }
        // Aquí ya basta retornar el último valor de la última fila del último archivo, que es la frontera superior del archivo j-esimo
        return frontiersInFiles[total_Y - 1][0][frontiersInFiles[total_Y - 1][0].length - 1];
        // TODO: Aqui leer las fronteras del último archivo y extraer último valor
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
        //System.out.println(Arrays.toString(filaSuperior_Y));
        //System.out.println(Arrays.toString(columnaIzquierda));
        //System.out.print("\n");
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

    public void writeFrontierColumnToFile(int subMatrixID){
        // subMatrixID va de 0 a 15 en el peor caso
        // m=20; f = 1; subMatrixID = {0, 1, 2, 3}
        // m=40; f = 2; subMatrixID = {0, 1, 2, 3, 4, 5, 6, 7}
        // m=80; f = 4; subMatrixID = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}

        String index = Integer.toString(subMatrixID);
        for(int i = 0; i < SIZE_OF_INT * f ; i++){
            // i: numero de bloque en la submatriz
            String subIndex = Integer.toString(i);
            try{
                DataOutputStream dataOutputStream = new DataOutputStream(
                        new FileOutputStream(dir_output + "C_" + index + "_" + subIndex + ".wtf"));
                for(int j = 0; j < B / SIZE_OF_INT; j++){
                    dataOutputStream.writeInt(newFrontierColumn[i * B / SIZE_OF_INT + j]);
                }
                this.O++;
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeFrontierRowToFile(int subMatrixID){
        // subMatrixID va de 0 a 15 en el peor caso
        // m=20; f = 1; subMatrixID = {0, 1, 2, 3}
        // m=40; f = 2; subMatrixID = {0, 1, 2, 3, 4, 5, 6, 7}
        // m=80; f = 4; subMatrixID = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}

        String index = Integer.toString(subMatrixID);
        for(int i = 0; i < SIZE_OF_INT * f ; i++){
            // i: numero de bloque en la submatriz
            String subIndex = Integer.toString(i);
            try{
                DataOutputStream dataOutputStream = new DataOutputStream(
                        new FileOutputStream(dir_output + "R_" + index + "_" + subIndex + ".wtf")
                );
                for(int j = 0; j < B / SIZE_OF_INT; j++){
                    dataOutputStream.writeInt(newFrontierRow[i * B / SIZE_OF_INT + j]);
                }
                this.O++;
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readIntoFrontierRow(int subMatrixID){
        String index = Integer.toString(subMatrixID);
        for(int i = 0; i < SIZE_OF_INT * f; i++){
            String subIndex = Integer.toString(i);
            try{
                DataInputStream dataInputStream = new DataInputStream(
                        new FileInputStream(dir_output + "R_" + index + "_" + subIndex + ".wtf")
                );
                for(int j = 0; j < B / SIZE_OF_INT; i++){
                    previousFrontierRow[i * B / SIZE_OF_INT + j] = dataInputStream.readInt();
                }
                this.I++;
                dataInputStream.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    public void readIntoFrontierColumn(int subMatrixID){
        String index = Integer.toString(subMatrixID);
        for(int i = 0; i < SIZE_OF_INT * f; i++){
            String subIndex = Integer.toString(i);
            try{
                DataInputStream dataInputStream = new DataInputStream(
                        new FileInputStream(dir_output + "C_" + index + "_" + subIndex + ".wtf")
                );
                for(int j = 0; j < B / SIZE_OF_INT; i++){
                    previousFrontierColumn[i * B / SIZE_OF_INT + j] = dataInputStream.readInt();
                }
                this.I++;
                dataInputStream.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String... args) throws Exception {
        //AlgoritmoRAMConFronteras a = new AlgoritmoRAMConFronteras("sunday","saturday", new int[] {1,2,3,4,5,6}, new int[] {0,1,2,3,4,5,6,7,8});
        //calcDist("sunday","saturday", new int[] {1,2,3,4,5,6}, new int[] {0,1,2,3,4,5,6,7,8});
        int r = calcAllDist(new String[] {"sun", "da", "y"}, new String[] {"sat", "ur", "day"});
        System.out.println(r);
        //System.out.println(Arrays.toString(a.calcDist()[0])); // ultima fila
        //System.out.println(Arrays.toString(a.calcDist()[1])); // ultima columna
    }


}
