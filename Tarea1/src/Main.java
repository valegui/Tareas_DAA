import java.io.File;

public class Main {
    public static void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }

    public static int experimentAdaptedRAM(int N, int M, String directory){
        FileGen fileGen = new FileGen();
        try {
            fileGen.genBlocks((int) (N/1024),"X_", directory);
            fileGen.genBlocks((int) (N/1024),"Y_", directory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AdaptedRAM experiment = new AdaptedRAM(M, N);
        experiment.setDirRow(directory);
        experiment.setDirX(directory);
        experiment.setDirY(directory);
        return experiment.calculateDistance();
    }

    public static int experimentRAMConFronteras(int N, int m, String directory){
        FileGen fileGen = new FileGen();
        try {
            fileGen.genBlocks((int) (N/1024),"X_", directory);
            fileGen.genBlocks((int) (N/1024),"Y_", directory);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RAMConFronteras exp = new RAMConFronteras(directory, directory, directory, m, N);
        return exp.calcAllDist();
    }

    public static void main(String[] args){
        String directory = System.getProperty("user.dir") + "/out/files/";
        // int[] N = {1024, 2048, 4096, 8192};
        // int[] M = {10, 20};
        int N = 2048;
        int M = 10;
        int m = 20;
        int k = experimentRAMConFronteras(N, m, directory);
        System.out.println(k);

        AdaptedRAM experiment = new AdaptedRAM(M, N);
        experiment.setDirRow(directory);
        experiment.setDirX(directory);
        experiment.setDirY(directory);
        System.out.println(experiment.calculateDistance());

    }
}
