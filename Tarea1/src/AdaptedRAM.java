import java.io.*;

public class AdaptedRAM {
    private static int B = 1024;
    private static int SIZE_OF_INT = 4;
    private static int numberOfInts = B / SIZE_OF_INT;

    private int M;
    private int N;
    /*cosas en RAM */
    private int westValue; // 4
    private int northWestValue; // 4
    private int[] previousRow; // 1024
    private int[] actualRow; // 1024
    private String X; // 1024
    private String Y; // 1024
    
    /* directorios*/
    private String dir_x;
    private String dir_y;
    private String dir_row;

    public AdaptedRAM(int M, int N){
        this.M = M;
        this.N = N;

        this.westValue = 1;
        this.northWestValue = 0;

        for(int i = 0; i < numberOfInts; i++){
            this.previousRow[i] = i + 1;
        }
    }
    
    public void setDirX(String d_x) {
    		this.dir_x = d_x;
    }
    
    public void setDirY(String d_y) {
		this.dir_y = d_y;
    }
    
    public void setDirRow(String d_r) {
		this.dir_row = d_r;
    }

    // TODO pasarle el path a donde estan los files
    public int calculateDistance(){
        computeMatrix();
        return actualRow[numberOfInts - 1];
    }

    public void computeMatrix(){
        for(int i = 0; i < N ; i++){
            if((i % B) == 0) readIntoX(i/B);
            computeRow(i);
        }
    }

    public void computeRow(int row){
        for(int i = 0; i < N/B; i++){
            readIntoY(i);
            computeBlockOfFile(i, row);
        }
    }

    // computar el bloque del archivo
    public void computeBlockOfFile(int stringBlock, int row){
        for(int i = 0; i < SIZE_OF_INT; i++){
            computeBlockOfMatrix(i, stringBlock, row);
            if(stringBlock != N/B - 1 && row!= N - 1) writeActualRowToFile(i, stringBlock);
        }
    }

    public void actualizePreviousRow(int matrixSubBlock, int stringBlock, int row){
        if(row == 0){
            if(!(matrixSubBlock == 0 && stringBlock ==0)){
                for(int i = 0; i < numberOfInts; i++){
                    previousRow[i] = previousRow[i] + numberOfInts;
                }
            }
        } else {
            readIntoPreviousRow(matrixSubBlock, stringBlock);
        }
    }

    public void actualizeNWValues(int matrixSubBlock, int stringBlock, int row){
        if(matrixSubBlock == SIZE_OF_INT - 1 && stringBlock == N/B - 1){
            this.westValue = row + 1;
            this.northWestValue = row;
        }
        else {
            this.westValue = actualRow[numberOfInts - 1];
            this.northWestValue = row==0 ? numberOfInts * matrixSubBlock + B * stringBlock : previousRow[numberOfInts - 1];
        }
    }

    public void computeBlockOfMatrix(int matrixSubBlock, int stringBlock, int row){
        int nwVal, nVal, wVal;

        actualizePreviousRow(matrixSubBlock, stringBlock, row);

        wVal = westValue + 1;
        nVal = previousRow[0] + 1;
        nwVal = (Y.charAt(numberOfInts * matrixSubBlock) == X.charAt(row % B)) ?
                northWestValue : northWestValue + 1;
        actualRow[0] = Math.min(nwVal, Math.min(nVal, wVal));

        for(int i = 1; i < numberOfInts; i++){
            wVal = actualRow[i-1] + 1;
            nVal = previousRow[i] + 1;
            nwVal = (Y.charAt(numberOfInts * matrixSubBlock + i) == X.charAt(row % B)) ?
                    previousRow[i - 1] : previousRow[i - 1] + 1;
            actualRow[i] = Math.min(nwVal, Math.min(nVal, wVal));
        }

        actualizeNWValues(matrixSubBlock, stringBlock, row);
    }

    public void writeActualRowToFile(int matrixSubBlock, int stringBlock){
        String index = Integer.toString(matrixSubBlock + stringBlock * SIZE_OF_INT);
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(dir_row +"/B_"+ index +".wtf"));
            for(int i: actualRow){
                dataOutputStream.writeInt(i);
            }
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readIntoPreviousRow(int matrixSubBlock, int stringBlock){
        String index = Integer.toString(matrixSubBlock + stringBlock * SIZE_OF_INT);
        try{
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(dir_row +"/B_"+ index +".wtf"));
            for(int i = 0; i < numberOfInts; i++){
                previousRow[i] = dataInputStream.readInt();
            }
            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readIntoX(int block){
    		String index = Integer.toString(block);
        try{
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(dir_x +"/X_"+ index +".wtf"));
            int i = 0;
            char[] temp = new char[B];
            while( dataInputStream.available() > 0) {
            		char b = (char) dataInputStream.readByte();
            		temp[i] = b;
            		i++;	
            }
            this.X = new String(temp);
            
            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readIntoY(int block){
    		String index = Integer.toString(block);
        try{
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(dir_x +"/Y_"+ index +".wtf"));
            int i = 0;
            char[] temp = new char[B];
            while( dataInputStream.available() > 0) {
            		char b = (char) dataInputStream.readByte();
            		temp[i] = b;
            		i++;	
            }
            this.Y = new String(temp);
            
            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
