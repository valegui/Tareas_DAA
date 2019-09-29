public class Experiments {

    public static void generateFile(int N, String directory){
        try {
            FileGen.genBlocks((int) (N/1024),"X_", directory);
            FileGen.genBlocks((int) (N/1024),"Y_", directory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cleanDirectory(String directory){

    }

    public static void experimentAdaptedRAM(int N, int m, String directory){
        generateFile(N, directory);
        cleanDirectory(directory);

    }

    public static void experimentGridRAM(int N, int m, String directory){
        generateFile(N, directory);
        cleanDirectory(directory);
    }
}
