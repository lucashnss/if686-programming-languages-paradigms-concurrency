import java.util.Scanner;

public class GlobalCounterThread extends Thread{
    private static int counter = 0;
    private static int limit;
    private static final Object lock = new Object();

    public GlobalCounterThread(int limit){
        GlobalCounterThread.limit = limit;
    }

    @Override
    public void run(){
        while(true){
            synchronized (lock){
                if (counter >= limit){
                    break;
                }

                counter++;
                System.out.println(Thread.currentThread().getName() + ": " + counter);
            }
        }
    }

    public static void main (String[] args){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of threads: ");
        int numThreads = scanner.nextInt();

        System.out.println("Enter the limit ");
        int limit = scanner.nextInt();

        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++){
            threads[i] = new GlobalCounterThread(limit);
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
