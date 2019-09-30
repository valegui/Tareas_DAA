import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Experiments implements Runnable{
    private String workingDir;
    private String resultDir;
    private int M;
    private int N;
    private int type; // 0: Adapted RAM | 1: GridRAM
    private int ID;

    public Experiments(int N, int M, String workingDir, String resultDir, int type){
        this.workingDir = workingDir;
        this.M = M;
        this.N = N;
        this.type = type;
        this.resultDir = resultDir;
        this.ID = M+N+type;
    }

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
            dataOutputStream.writeUTF(
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
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int cantidadDeExperimentosCompletos = 10;
        int[] N1 = {1024, 2048, 4096, 8192};
        int[] N2 = {16384, 32768, 65536};
        int[] m = {20, 40, 80};
        String Wdirectory;
        String ResultDir;
        for(int a = 0; a < cantidadDeExperimentosCompletos; a++) {
            // el lento
            for (int mm = 0; mm < m.length; mm++) {
                for (int nn = 0; nn < N1.length; nn++) {
                    Wdirectory = System.getProperty("user.dir") + "/w_" + a + "/" + mm + "_" + nn + "_" + Integer.toString(0) + "/";
                    ResultDir = System.getProperty("user.dir")  + "/r_" + a + "/" + mm + "_" + nn + "_" + Integer.toString(0) + "/";
                    File f1 = new File(Wdirectory);f1.mkdirs();
                    File f2 = new File(ResultDir);f2.mkdirs();
                    executorService.submit(new Experiments(N1[nn], m[mm], Wdirectory, ResultDir, 0));
                }
            }
            // el rapido
            for (int mm = 0; mm < m.length; mm++) {
                for (int nn = 0; nn < N1.length; nn++) {
                    Wdirectory = System.getProperty("user.dir") + "/w_" + a + "/" + mm + "_" + nn + "_" + Integer.toString(1) + "/";
                    ResultDir = System.getProperty("user.dir")  + "/r_" + a + "/" + mm + "_" + nn + "_" + Integer.toString(1) + "/";
                    File f1 = new File(Wdirectory);f1.mkdirs();
                    File f2 = new File(ResultDir);f2.mkdirs();
                    executorService.submit(new Experiments(N1[nn], m[mm], Wdirectory, ResultDir, 1));
                }
            }
            for (int mm = 0; mm < m.length; mm++) {
                for (int nn = 0; nn < N2.length; nn++) {
                    Wdirectory = System.getProperty("user.dir") + "/w_" + a + "/" + mm + "_" + nn + "_" + Integer.toString(1) + "/";
                    ResultDir = System.getProperty("user.dir")  + "/r_" + a + "/" + mm + "_" + nn + "_" + Integer.toString(1) + "/";
                    File f1 = new File(Wdirectory);f1.mkdirs();
                    File f2 = new File(ResultDir);f2.mkdirs();
                    executorService.submit(new Experiments(N2[nn], m[mm], Wdirectory, ResultDir, 1));
                }
            }
        }
        // Apagar ejecutor
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    @Override
    public void run() {
        System.out.println("ID: " + ID + " | Ejecutando algoritmo " + this.type +" M: " + this.M + ", N: " + this.N + " dir: " + workingDir);
        DataOutputStream dataOutputStream = null;
        try {
            // Crear PrintWriter
            dataOutputStream = new DataOutputStream(new FileOutputStream(this.resultDir + "exp.csv"));
            /*try {
                dataOutputStream.writeUTF("tipo de algoritmo,N,m,I,O,IO,time\n");
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            // Ejecutar experimento
            if (this.type == 0){
                experimentAdaptedRAM(this.N, this.M, this.workingDir, dataOutputStream);
            }else{
                experimentGridRAM(this.N, this.M, this.workingDir, dataOutputStream);
            }
            // Cerrar outputstream
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: PrintWriter no creado");
        }finally {
            System.out.println("ID: " + ID + " | Experimento terminado + " + this.type +" M: " + this.M + ", N: " + this.N + " dir: " + resultDir);
        }



    }
}
