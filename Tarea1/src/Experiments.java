import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Experiments{
    private String workingDir;
    private String resultDir;
    private int M;
    private int N;
    private int type; // 0: Adapted RAM | 1: GridRAM
    private int ID;


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
        AdaptedRAM experiment = new AdaptedRAM(N);
        experiment.setDirRow(directory);
        experiment.setDirX(directory);
        experiment.setDirY(directory);
        long startTime = System.currentTimeMillis();
        int distance = experiment.calculateDistance();
        long endTime = System.currentTimeMillis();
        int IO = experiment.getIO();
        int I = experiment.getI();
        int O = experiment.getO();
        long timeElapsed = endTime - startTime;
        try {
            dataOutputStream.writeUTF("AdaptedRAM,"+
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
        long startTime = System.currentTimeMillis();
        int distance = experiment.calcAllDist();
        long endTime = System.currentTimeMillis();
        int IO = experiment.getIO();
        int I = experiment.getI();
        int O = experiment.getO();
        long timeElapsed = endTime - startTime;
        try {
            dataOutputStream.writeUTF("GridRAM,"+
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
        int totalExperiments = 15;
        int[] N1 = {1024, 2048, 4096, 8192};
        int[] N2 = {1024, 2048, 4096, 8192, 16384, 32768, 65536};
        int[] m = {20, 40, 80};
        String directory = System.getProperty("user.dir") + "/out/files/";;
        String result = System.getProperty("user.dir") + "/out/results/";

        DataOutputStream dataOutputStream = null;

        for(int i = 0; i < totalExperiments; i++){
            for(int n: N1){
                try {
                    dataOutputStream = new DataOutputStream(
                            new FileOutputStream(result + "T_1_N_" + n +"_m_" + 40 + "_exp_" + i + ".csv")
                    );
                    System.out.println("Comienzo experimento nº" + i +
                            " - Tipo AdaptedRAM - N: " + n +"\tm:" + 40);
                    experimentAdaptedRAM(n, 40, directory, dataOutputStream);
                    dataOutputStream.close();
                    System.out.println("| Experimento terminado + Experimento nº" + i +
                            " - Tipo AdaptedRAM - N: " + n +"\tm:" + 40);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            for(int t: m){
                for(int n: N2){
                    try {
                        dataOutputStream = new DataOutputStream(
                                new FileOutputStream(result + "T_2_N_" + n +"_m_" + t + "_exp_" + i + ".csv")
                        );
                        System.out.println("Comienzo experimento nº" + i +
                                " - Tipo GridRAM - N: " + n +"\tm:" + t);
                        experimentGridRAM(n, t, directory, dataOutputStream);
                        dataOutputStream.close();
                        System.out.println("| Experimento terminado + Experimento nº" + i +
                                " - Tipo GridRAM - N: " + n +"\tm:" + t);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
