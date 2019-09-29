import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Experiments {

    public static void generateFile(int N, String directory){
        try {
            FileGen.genBlocks((int) (N/1024),"X_", directory);
            FileGen.genBlocks((int) (N/1024),"Y_", directory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void deleteListFilesOnFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                deleteListFilesOnFolder(fileEntry);
            }
            fileEntry.delete();
        }
    }

    public static void experimentAdaptedRAM(int N, int m, String directory, PrintWriter printWriter){
        generateFile(N, directory);
        AdaptedRAM experiment = new AdaptedRAM(m, N);
        experiment.setDirRow(directory);
        experiment.setDirX(directory);
        experiment.setDirY(directory);
        long startTime = System.nanoTime();
        int distance = experiment.calculateDistance();
        long endTime = System.nanoTime();
        int IO = experiment.getIO();
        int I = experiment.getI();
        int O = experiment.getO();
        long timeElapsed = endTime - startTime;
        printWriter.println(
                1 + ","+
                Integer.toString(N) + ","+
                Integer.toString(m) + ","+
                Integer.toString(I) + ","+
                Integer.toString(O) + ","+
                Integer.toString(IO) + ","+
                Long.toString(timeElapsed)
        );
        deleteListFilesOnFolder(new File(directory));

    }

    public static void experimentGridRAM(int N, int m, String directory, PrintWriter printWriter){
        generateFile(N, directory);
        RAMConFronteras experiment = new RAMConFronteras(directory, directory, directory, m, N);
        long startTime = System.nanoTime();
        int distance = experiment.calcAllDist();
        long endTime = System.nanoTime();
        int IO = experiment.getIO();
        int I = experiment.getI();
        int O = experiment.getO();
        long timeElapsed = endTime - startTime;
        printWriter.println(
                Integer.toString(2) + ","+
                        Integer.toString(N) + ","+
                        Integer.toString(m) + ","+
                        Integer.toString(I) + ","+
                        Integer.toString(O) + ","+
                        Integer.toString(IO) + ","+
                        Long.toString(timeElapsed)
        );
        deleteListFilesOnFolder(new File(directory));
    }

    public static void main(String[] args){
        int[] N1 = {1024, 2048, 4096, 8192};
        int[] N2 = {16384, 32768, 65536};
        int[] m = {20, 40, 80};
        String directory = System.getProperty("user.dir") + "/out/files/";
        String resultados = System.getProperty("user.dir") + "/out/resultados/";
        // el lento

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileWriter(resultados + "exp.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        printWriter.println("tipo de algoritmo,N,m,I,O,IO,time");
        for(int t: m){
            for(int n: N1){
                experimentAdaptedRAM(n, t, directory, printWriter);
            }
        }
        for(int t: m){
            for(int n: N1){
                experimentGridRAM(n, t, directory, printWriter);
            }
            for(int n: N2){
                experimentGridRAM(n, t, directory, printWriter);
            }
        }
    }
}
