import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SlimConcurrentCounter {
    private static final int NUM_SEGMENTS = 10;
    private final int[] count = new int[NUM_SEGMENTS];
    private final Lock[] locks = new Lock[NUM_SEGMENTS];

    public SlimConcurrentCounter() {
        for (int i = 0; i < NUM_SEGMENTS; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    public void increment() {
        int segment = (int) (Thread.currentThread().getId() % NUM_SEGMENTS);
        locks[segment].lock();
        try {
            count[segment]++;
        } finally {
            locks[segment].unlock();
        }
    }

    public int getCount() {
        int total = 0;
        for (int i = 0; i < NUM_SEGMENTS; i++) {
            locks[i].lock();
            try {
                total += count[i];
            } finally {
                locks[i].unlock();
            }
        }
        return total;
    }


    public static void main(String[] args) throws InterruptedException {
        ConcurrentCounter cc = new ConcurrentCounter();
        int numThread = 100;
        Thread[] threads = new Thread[numThread];

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numThread; i++) {
            threads[i] = new Thread (() -> {
                for (int j = 0; j < 10000; j++) {
                    cc.increment();
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < numThread; i++) {
            threads[i].join();
        }

        System.out.println("Valor final com travas finas: " + cc.getCount());
        long endTime = System.currentTimeMillis();
        System.out.println("Tempo de execução: " + (endTime - startTime) + " ms");
    }
}
