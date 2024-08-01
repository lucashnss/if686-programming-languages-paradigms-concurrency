import java.util.Scanner;

public class LocalCounterThread extends Thread {
    private int limit;
    private int counter;

    public LocalCounterThread(int limit){
        this.limit = limit;
        this.counter = 0;
    }

    @Override
    public void run() {
        while (counter < limit){
            counter++;
            System.out.println(Thread.currentThread().getName() + ": " + counter);
        }
    }
    public static void main(String[]args){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of threads: ");
        int numThreads = scanner.nextInt();

        System.out.println("Enter the limit ");
        int limit = scanner.nextInt();

        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++){
            threads[i] = new LocalCounterThread(limit);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
