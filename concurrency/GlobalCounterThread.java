import java.util.Scanner;

public class GlobalCounterThread extends Thread{
    private static int globalCounter = 0;
    private static boolean finished = false;
    private int limit;
    private int localCounter;

    public GlobalCounterThread(int limit){
        this.limit = limit;
        this.localCounter = 0;
    }

    @Override
    public void run(){
        try {
            while (localCounter < limit && !finished) {
                synchronized (GlobalCounterThread.class) {
                    if (finished) {
                        break;
                    }
                    globalCounter++;
                    localCounter++;
                }
                System.out.print(Thread.currentThread().getName() + ": " + localCounter);
                Thread.sleep(1);
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        if (!finished){
            finished = true;
            System.out.println(Thread.currentThread().getName() + " finished first with " + localCounter + " iterations. ");
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

        System.out.println("Global counter value: " + globalCounter);
    }
}
