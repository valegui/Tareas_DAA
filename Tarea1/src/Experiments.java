import java.io.File;

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

    public static void experimentAdaptedRAM(int N, int m, String directory){
        generateFile(N, directory);
        AdaptedRAM experiment = new AdaptedRAM(m, N);
        experiment.setDirRow(directory);
        experiment.setDirX(directory);
        experiment.setDirY(directory);
        int distance = experiment.calculateDistance();
        int IO = experiment.getIO();
        deleteListFilesOnFolder(new File(directory));

    }

    public static void experimentGridRAM(int N, int m, String directory){
        generateFile(N, directory);
        RAMConFronteras exp = new RAMConFronteras(directory, directory, directory, m, N);
        int distance = exp.calcAllDist();
        int IO = exp.getIO();
        deleteListFilesOnFolder(new File(directory));
    }
}
