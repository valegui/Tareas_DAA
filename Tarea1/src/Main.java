public class Main {
    public static void main(String[] args){
        int N = 1024;
        int m = 20;
        String directory = System.getProperty("user.dir") + "/out/files/";;
        try {
            FileGen.genBlocks((int) (N/1024),"X_", directory);
            FileGen.genBlocks((int) (N/1024),"Y_", directory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RAMConFronteras experiment = new RAMConFronteras(directory, directory, directory, m, N);
        System.out.println(experiment.calcAllDist());
        AdaptedRAM experiment2 = new AdaptedRAM(N);
        experiment2.setDirRow(directory);
        experiment2.setDirX(directory);
        experiment2.setDirY(directory);
        System.out.println(experiment2.calculateDistance());
    }
}
