import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentCounter {
    private int count = 0;
    private Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        ConcurrentCounter cc = new ConcurrentCounter();
        int numThreads = 100;
        Thread[] threads = new Thread[numThreads];

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    cc.increment();
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }

        System.out.println("Valor final do contador com trava global: " + cc.getCount());

        long endTime = System.currentTimeMillis();
        System.out.println("Tempo de execução: " + (endTime - startTime) + " ms");
    }


}
