import java.io.*;
import java.util.Arrays;

public class RAMConFronteras {
    public static int B = 1024;
    public static int SIZE_OF_INT = 4;
    private int I;
    private int O;
    private int f;
    private int N;
    private byte[] X;
    private byte[] Y;
    private String dir_x;
    private String dir_y;
    private String dir_output;
    private int[] newFrontierRow;
    private int[] newFrontierColumn;
    private int[] previousFrontierRow;
    private int[] previousFrontierColumn;
    private int[] diagValues;
    private int cantSub;


    public RAMConFronteras(String dir_x, String dir_y, String dir_output, int m, int N){
        this.I = 0;
        this.O = 0;
        this.N = N;
        this.dir_x = dir_x;
        this.dir_y = dir_y;
        this.dir_output = dir_output;
        this.f = N/B < m/20 ? N/B : m/20;
        this.newFrontierColumn = new int[f*B];
        this.newFrontierRow = new int[f*B];
        this.previousFrontierColumn = new int[f*B];
        this.previousFrontierRow = new int[f*B];
        this.cantSub = (int) Math.ceil(N/(B*f));
        this.diagValues = new int[cantSub];
        for(int i = 0; i < cantSub; i++){
            diagValues[i] = B * f * i;
        }



        this.X = new byte[B*f];
        this.Y = new byte[B*f];
    }

    public int getI(){
        return I;
    }

    public int getO(){
        return O;
    }

    public int getIO(){
        return I + O;
    }
    
    public static int[] generarFrontera(int tamaño, int primerValor) {
        int[] a = new int[tamaño];
        for (int i = 0; i < tamaño; i++) {
            a[i] = primerValor;
            primerValor++;
        }
        return a;
    }

    public int calcAllDist(){

        // Itero sobre filas de X
        for(int a = 0; a < this.cantSub; a++){
            // Aquí leer string x correspondiente a la submatriz fila a
            readIntoX(a);


            // Itero sobre columnas de Y
            for(int j = 0; j < this.cantSub; j++){
                // Aquí lleer string y correspondiente a la submatriz columna j
                readIntoY(j);


                // Primera columna es especial para X: generar frontera lateral de X
                if( j == 0){
                    this.previousFrontierColumn = generarFrontera(B * f, B* f* a + 1);

                }else{ // Sino, leer frontera lateral de archivo
                    readIntoFrontierColumn(j);
                }

                // En primera fila se generan fronteras superiores
                if( a == 0){
                    this.previousFrontierRow = generarFrontera(B * f, B* f* j + 1); // Empieza desde el largo anterior

                }else{ // Sino leer frontera superior de archivo
                    readIntoFrontierRow(j);
                }

                // En este punto ya tengo ambas fronteras y strings definidas, calculo nuevas fronteras y sobreescribo
                calcDist(j,a);
            }
        }
        // Aquí ya basta retornar el último valor de la última fila del último archivo, que es la frontera superior del archivo j-esimo
        return newFrontierRow[B*f - 1];
    }

    public void calcDist(int j, int a){
        int largo = B * f; // Para no recalcular
        int l, p; // Para no recalcular

        // Verificación de largos de filas proporcionadas y largos de Strings
        if(largo != (l = previousFrontierRow.length)){
            System.out.println("Frontera columna izquierda anterior administrada no coincide con el largo de string X: " + largo + " (largo real) vs " + l);
        }
        if(largo != (p = previousFrontierColumn.length)){ // +1 porque contiene el valor de la diagonal
            System.out.println("Frontera superior anterior administrada no coincide con el largo de string Y: " + largo + " (largo real) vs " + p);
        }


        //Computar filas
        for (int i = 0; i < largo; i++){
            computarFilaB(i, j);
        }
        diagValues[j] = previousFrontierColumn[B * f - 1];

        if(j == cantSub - 1){
            if(a != cantSub - 1) writeFrontierRowToFile(j);
        }
        else{
            if(a == cantSub - 1){
                writeFrontierColumnToFile(j+1);
            }
            else {
                writeFrontierRowToFile(j);
                writeFrontierColumnToFile(j + 1);
            }
        }
    }

    public void computarFilaB(int i, int subMatrix) {
        int nwVal, wVal, nVal;

        wVal = previousFrontierColumn[i] + 1;
        nVal = previousFrontierRow[0] + 1;
        if(i == 0){
            nwVal = (Y[0] == X[i]) ? diagValues[subMatrix] : diagValues[subMatrix] + 1;
        } else {
            // wn saca de prevfrntiercolumn
            nwVal = (Y[0] == X[i]) ? previousFrontierColumn[i - 1] : previousFrontierColumn[i - 1] + 1;
        }

        newFrontierRow[0] = Math.min(nwVal, Math.min(nVal, wVal));

        // Recorremos el resto de la fila
        for(int j = 1; j < B * f; j++){
            wVal = newFrontierRow[j-1] + 1;
            nVal = previousFrontierRow[j] + 1;
            nwVal = (Y[j] == X[i]) ? previousFrontierRow[j - 1] : previousFrontierRow[j- 1] + 1;

            newFrontierRow[j] = Math.min(nwVal, Math.min(nVal, wVal));
        }

        newFrontierColumn[i] = newFrontierRow[B * f - 1];

        System.arraycopy(newFrontierRow, 0, previousFrontierRow, 0, B * f);

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
        // m=20; f = 1; i = {0, 1, 2, 3}
        // m=40; f = 2; i = {0, 1, 2, 3, 4, 5, 6, 7}
        // m=80; f = 4; i = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}

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
        byte[] temp = new byte[B];
        String index = Integer.toString(subMatrixID);
        for(int i = 0; i < SIZE_OF_INT * f; i++){
            String subIndex = Integer.toString(i);
            try{
                DataInputStream dataInputStream = new DataInputStream(
                        new FileInputStream(dir_output + "R_" + index + "_" + subIndex + ".wtf")
                );
                dataInputStream.read(temp);
                System.arraycopy(Utils.convertByteToIntArray(temp), 0, previousFrontierRow, i * B / SIZE_OF_INT, B / SIZE_OF_INT);
                /*
                for(int j = 0; j < B / SIZE_OF_INT; j++){
                    previousFrontierRow[i * B / SIZE_OF_INT + j] = dataInputStream.readInt();
                }
                */
                this.I++;
                dataInputStream.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    public void readIntoFrontierColumn(int subMatrixID){
        String index = Integer.toString(subMatrixID);
        byte[] temp = new byte[B];
        for(int i = 0; i < SIZE_OF_INT * f; i++){
            String subIndex = Integer.toString(i);
            try{
                DataInputStream dataInputStream = new DataInputStream(
                        new FileInputStream(dir_output + "C_" + index + "_" + subIndex + ".wtf")
                );
                dataInputStream.read(temp);
                System.arraycopy(Utils.convertByteToIntArray(temp), 0, previousFrontierColumn, i * B / SIZE_OF_INT, B / SIZE_OF_INT);
                /*
                for(int j = 0; j < B / SIZE_OF_INT; j++){
                    previousFrontierColumn[i * B / SIZE_OF_INT + j] = dataInputStream.readInt();
                }
                */
                this.I++;
                dataInputStream.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void readIntoX(int subMatrixID){
        String index;
        try{
            //int k = 0;
            byte[] temp = new byte[B];
            for(int i = 0; i < f; i++){
                index = Integer.toString(subMatrixID * f + i);
                DataInputStream dataInputStream = new DataInputStream(
                        new FileInputStream(dir_x + "X_" + index + ".wtf")
                );
                dataInputStream.read(temp);
                System.arraycopy(temp, 0, X, i*B, B);

                /*
                while( dataInputStream.available() > 0) {
                    X[k] = dataInputStream.readByte();
                    k++;
                }
                */
                I++;
                dataInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readIntoY(int subMatrixID){
        String index;
        try{
            //int k = 0;
            byte[] temp = new byte[B];
            for(int i = 0; i < f; i++){
                index = Integer.toString(subMatrixID * f + i);
                DataInputStream dataInputStream = new DataInputStream(
                        new FileInputStream(dir_y + "Y_" + index + ".wtf")
                );
                dataInputStream.read(temp);
                System.arraycopy(temp, 0, Y, i*B, B);


                /*
                while( dataInputStream.available() > 0) {
                    this.Y[k] = dataInputStream.readByte();
                    k++;
                }*/
                I++;
                dataInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
