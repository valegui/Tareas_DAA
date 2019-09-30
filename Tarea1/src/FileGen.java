import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class FileGen{
    private static int BLOCK_SIZE = 1024;

    public static void genBlockFromArray(String filePrefix, String directory, byte[] array, int numberOfBlock){
        String fileFullName = directory + filePrefix + "_" +Integer.toString(numberOfBlock) + ".wtf";
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileFullName);
            fileOutputStream.write(array);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void genBlockWithNDiff(int n, String[] filesPrefix, String directory, int numberOfBlock){
        byte[] array = new byte[BLOCK_SIZE];
        Random rd = new Random();
        rd.nextBytes(array);
        int p = 1;
        for(String filePrefix : filesPrefix){
            byte[] arrayForNewBlock = Arrays.copyOf(array, BLOCK_SIZE);
            int index = BLOCK_SIZE / n;
            for(int i = 0; i<n; i++){
                arrayForNewBlock[index*i] += p;
            }
            genBlockFromArray(filePrefix, directory, arrayForNewBlock,numberOfBlock);
            p++;
        }

    }

    public static void genBlocks(int numOfBlocks, String filePrefix, String directory) throws Exception{
        //FileWriter outputStream = null;
        //PrintWriter p = null;
        FileOutputStream f = null;
        //String baseDir = System.getProperty("user.dir") + "\\";
        String baseDir = directory;
        Random rd = new Random();
        byte[] arr = new byte[BLOCK_SIZE];
        String file = filePrefix;
        while(numOfBlocks != 0){
            try {
                file += (numOfBlocks - 1) + ".wtf";
                rd.nextBytes(arr); // Random de bytes
                //outputStream = new FileWriter(baseDir + file);
                //p = new PrintWriter(baseDir + file);
                f = new FileOutputStream(baseDir + file);
                int c = BLOCK_SIZE - 1;
                f.write(arr);
                /*while (c != -1 ) {
                    //outputStream.write(arr[c]);
                    p.print((char) arr[c]);
                    c--;
                }
                */
            } finally {
                /*if (outputStream != null) {
                    outputStream.close();
                }*/
                if ( f != null )
                {
                    f.close();
                }
            }
            numOfBlocks--;
            file = filePrefix;
        }
    }

    public static void main(String... args) throws Exception{
        //genBlocks(1,"X_", System.getProperty("user.dir") + "\\");
        //genBlocks(1,"Y_", System.getProperty("user.dir") + "\\");
        String[] prefix = {"X", "Y"};
        String dir = System.getProperty("user.dir") + "/out/files/";

        genBlockWithNDiff(2, prefix, dir,0);
    }

}