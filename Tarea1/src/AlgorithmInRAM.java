import static jdk.nashorn.internal.objects.NativeMath.min;

public class AlgorithmInRAM {
    private String x;
    private String y;
    private int largo_x;
    private int largo_y;
    private int[] ant;

    public AlgorithmInRAM(String x, String y){
        int lx = x.length();
        int ly = y.length();
        if(lx > ly){
            this.x = x;
            this.y = y;
            this.largo_x = lx;
            this.largo_y = ly;
        }else{
            this.x = y;
            this.y = x;
            this.largo_x = ly;
            this.largo_y = lx;
        }
        this.ant = new int[this.largo_y + 1];

    }

    public int calcDist(){
        // Primera fila
        for (int i = 0; i <= largo_y; i++){
            ant[i] = i;
        }

        for (int i = 0; i < largo_x; i++){
            computarFila(i);
        }
        return ant[largo_y];
    }

    public void computarFila(int fila) {
        char s = x.charAt(fila); // caracter de x
        char z;

        // 1 o 0
        int NW = 1;

        // Val celdas
        int NW_val;
        int W_val;
        int N_val;

        int [] nueva = new int[largo_y + 1];
        nueva[0] = fila + 1;

        // Recorremos la nueva fila
        for(int j = 1; j <= largo_y; j++){
            z = y.charAt(j-1); // caracter de Y

            NW = 1;
            if(s == z)
                NW = 0;
            NW_val = ant[j-1] + NW;
            N_val = ant[j] + 1;
            W_val = nueva[j-1] + 1;

            nueva[j] = (int) Math.min(NW_val, Math.min(N_val, W_val));
        }
        if (largo_y + 1 >= 0) System.arraycopy(nueva, 0, ant, 0, largo_y + 1);

    }
}
