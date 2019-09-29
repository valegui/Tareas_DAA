import java.io.*;

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

    public static void experimentAdaptedRAM(int N, int m, String directory, DataOutputStream dataOutputStream){
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
        try {
            dataOutputStream.writeChars(
                    Integer.toString(1) + ","+
                    Integer.toString(N) + ","+
                    Integer.toString(m) + ","+
                    Integer.toString(I) + ","+
                    Integer.toString(O) + ","+
                    Integer.toString(IO) + ","+
                    Long.toString(timeElapsed) + "\n"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        deleteListFilesOnFolder(new File(directory));

    }

    public static void experimentGridRAM(int N, int m, String directory, DataOutputStream dataOutputStream){
        generateFile(N, directory);
        RAMConFronteras experiment = new RAMConFronteras(directory, directory, directory, m, N);
        long startTime = System.nanoTime();
        int distance = experiment.calcAllDist();
        long endTime = System.nanoTime();
        int IO = experiment.getIO();
        int I = experiment.getI();
        int O = experiment.getO();
        long timeElapsed = endTime - startTime;
        try {
            dataOutputStream.writeUTF(
                    Integer.toString(2) + ","+
                            Integer.toString(N) + ","+
                            Integer.toString(m) + ","+
                            Integer.toString(I) + ","+
                            Integer.toString(O) + ","+
                            Integer.toString(IO) + ","+
                            Long.toString(timeElapsed) + "\n"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        deleteListFilesOnFolder(new File(directory));
    }

    public static void main(String[] args){
        int[] N1 = {1024, 2048, 4096, 8192};
        int[] N2 = {16384, 32768, 65536};
        int[] m = {20, 40, 80};
        String directory = System.getProperty("user.dir") + "/out/files/";
        String resultados = System.getProperty("user.dir") + "/out/resultados/";
        // el lento

        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(new FileOutputStream(resultados + "exp.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            dataOutputStream.writeUTF("tipo de algoritmo,N,m,I,O,IO,time\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int t: m){
            for(int n: N1){
                experimentAdaptedRAM(n, t, directory, dataOutputStream);
            }
        }
        for(int t: m){
            for(int n: N1){
                experimentGridRAM(n, t, directory, dataOutputStream);
            }
            for(int n: N2){
                experimentGridRAM(n, t, directory, dataOutputStream);
            }
        }
    }
}
