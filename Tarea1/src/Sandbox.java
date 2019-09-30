import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Sandbox {
    public static class A implements Runnable{
        String prefix;

        A(String p){
            this.prefix = p;
        }
        @Override
        public void run() {
            System.out.println("WTF" + prefix);
        }
    }
    public static void main(String[] args) {
        int numTasks = 1024;
        int numThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for(int i = 1; i <= numTasks; i++)
            executorService.submit(new A(String.valueOf(i)));
            //runnable.add(new A(String.valueOf(i)));

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
