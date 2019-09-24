import java.io.FileWriter;
import java.util.Random;

public class FileGen{
    public static void genBlocks(int numOfBlocks, int BLOCK_SIZE, String filePrefix) throws Exception{
        FileWriter outputStream = null;
        String baseDir = System.getProperty("user.dir") + "\\";
        Random rd = new Random();
        byte[] arr = new byte[BLOCK_SIZE];
        String file = filePrefix;
        while(numOfBlocks != 0){
            try {
                file += (numOfBlocks - 1) + ".wtf";
                rd.nextBytes(arr); // Random de bytes
                outputStream = new FileWriter(baseDir + file);
                int c = BLOCK_SIZE - 1;
                while (c != -1 ) {
                    outputStream.write(arr[c]);
                    c--;
                }
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
            numOfBlocks--;
            file = filePrefix;
        }
    }

    public static void main(String... args){
        genBlocks(2, 1024, "X_");
        genBlocks(3, 1024, "Y_");
    }

}