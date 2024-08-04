import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountDownLatch {
    private int count;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public CountDownLatch(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count cannot be negative");
        }
        this.count = count;
    }

    public void await() throws InterruptedException {
        lock.lock();
        try {
            while (count > 0) {
                condition.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void countDown() {
        lock.lock();
        try {
            if (count > 0) {
                count--;
                if (count == 0) {
                    condition.signalAll();
                }
            }
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

    public static void main(String[] args) {
        int numThreads = 5;
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " is waiting");
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(Thread.currentThread().getName() + " is released");
            }).start();
        }

        try {
            Thread.sleep(2000); // Simulate some work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (int i = 0; i < numThreads; i++) {
            latch.countDown();
            System.out.println("Latch count down: " + latch.getCount());
            try {
                Thread.sleep(1000); // Simulate some work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

