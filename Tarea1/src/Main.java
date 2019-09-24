import java.io.*;

public class Main {
    public static void main(String[] arguments) {
        int hola = 29292;
        int length = (int) (Math.log10(hola) + 1);
        //System.out.println(length + 1);

        DataOutputStream dataOutputStream = null;
        ByteArrayOutputStream baos = null;

        try{
            baos = new ByteArrayOutputStream();
            dataOutputStream = new DataOutputStream(new FileOutputStream("adios.txt"));
            dataOutputStream.writeInt(1000000);
            dataOutputStream.writeInt(2000000);
            dataOutputStream.writeInt(30);

        } catch (IOException e) {
            e.printStackTrace();
        }

        DataInputStream dataIn = null;
        try {
            dataIn = new DataInputStream(new FileInputStream("adios.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


            try {
                while(dataIn.available()>0){
                    int k = dataIn.readInt();
                    System.out.print(k+" ");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        // https://www.tutorialspoint.com/java/java_dataoutputstream.htm
    }
}
