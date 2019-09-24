public class AdaptedRAM {
    private static int B = 1024;
    private static int SIZE_OF_INT = 4;

    private int M;
    private int N;
    private int numberOfInts;
    /*cosas en RAM */
    private int westValue;
    private int northWestValue;

    public AdaptedRAM(int M, int N, int maxInt){
        this.M = M;
        this.N = N;
        this.numberOfInts = B / SIZE_OF_INT;


    }
}
